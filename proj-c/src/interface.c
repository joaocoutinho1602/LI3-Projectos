/** \file interface.c
 * Módulo Interface
 * Contém as funções de inicialização, queries e free
 * @author Grupo69
 * @date 01/05/2018
 */

#include <interface.h>
#include <glib.h>
#include <loader.h>
#include <search.h>
#include <stack_user.h>
#include <stack_post.h>

struct TCD_community{
	GHashTable *posts;
	GHashTable *users;
	GHashTable *dates;
	GHashTable *tags;
};

//TAD Init
TAD_community init(){

  TAD_community com = (TAD_community) malloc(sizeof(struct TCD_community));

	com->posts = g_hash_table_new_full(g_int_hash, g_int_equal, NULL,(GDestroyNotify) free_post);
	com->users = g_hash_table_new_full(g_int_hash, g_int_equal, NULL,(GDestroyNotify) free_stack_user);
	com->dates = g_hash_table_new_full(g_date_time_hash, g_date_time_equal, (GDestroyNotify) g_date_time_unref,(GDestroyNotify)g_tree_destroy );
	com->tags  = g_hash_table_new_full(g_str_hash, g_str_equal, (GDestroyNotify)free, NULL);

	return com;
}

//TAD Load
TAD_community load(TAD_community com, char* dump_path) {

	load_tags(com->tags, dump_path);
	load_users(com->users, dump_path);
	load_posts(com->posts, com->users, com->dates, com->tags,  dump_path);

	return com;
}

//Query 1
STR_pair info_from_post(TAD_community com, long id){
	STR_pair resultado = get_post_info(com->posts, com->users, id);
	return resultado;
}

//Query 2
LONG_list top_most_active(TAD_community com, int N){
	int i;
	long id_user;
	GSList* top;

	top = get_top_users(com->users, N);
	top = g_slist_reverse(top);

	LONG_list top_users = create_list(N);

	for (i = 0; i < N; i++) {
		id_user = get_user_id(g_slist_nth_data (top, i));
  	set_list(top_users, i, id_user);
 	}
	g_slist_free(top);

	return top_users;
}

//Query 3
LONG_pair total_posts(TAD_community com, Date begin, Date end){
	LONG_pair posts = create_long_pair(0,0);
	if(valid_date(begin) && valid_date(end))
		f_between_dates(com->dates, begin, end, count_posts, posts);

	return posts;
}

//Query 4
LONG_list questions_with_tag(TAD_community com, char* tag, Date begin, Date end){
	LONG_list list = NULL;

	if(valid_date(begin) && valid_date(end)){

		int i, size;
		long tag_id;

		Post p;


		tag_id = (long) g_hash_table_lookup(com->tags, tag);
		if(tag_id == 0) return list;

		GPtrArray* posts = g_ptr_array_new();
		g_ptr_array_add(posts, (long *) tag_id);
		f_between_dates(com->dates, begin, end, post_tags, posts);

		g_ptr_array_remove_index(posts, 0);

		size = posts->len;

		list = create_list(size);

		for(i = 0; i < size; i++){
			p = g_ptr_array_index(posts, i);
			set_list(list, size-i-1, get_post_id(p));
		}
		g_ptr_array_free(posts,TRUE);
	}
	return list;
}

//Query 5
USER get_user_info(TAD_community com, long id){
	USER u;
	char *bio = get_shortbio(com->users, id);

	long *posts = get_post_history(com->users, id);

	u = create_user(bio, posts);
	free(posts);

	return u;
}

//Query 6
LONG_list most_voted_answers(TAD_community com, int N, Date begin, Date end){
	LONG_list most_voted = create_list(N);

	if(valid_date(begin) && valid_date(end)){
		int i;
		Post p;

		GPtrArray* array = g_ptr_array_sized_new (N);

		init_array(array, N);
		f_between_dates(com->dates, begin, end, most_score, array);


		for(i = 0; i < N; i++){
			p = g_ptr_array_index(array,N-i-1);
			set_list(most_voted, i, get_post_id(p));
		}
		g_ptr_array_free(array,TRUE);
	}
	return most_voted;
}

//Query 7
LONG_list most_answered_questions(TAD_community com, int N, Date begin, Date end){
	LONG_list posts = create_list(N);

	if(valid_date(begin) && valid_date(end)){
		int i;
		Post p;
		GPtrArray* array = g_ptr_array_sized_new (N);
		init_array(array, N);


		f_between_dates(com->dates, begin, end, most_answers, array);

		for(i = 0; i < N; i++){
			p = g_ptr_array_index(array,N-i-1);
			set_list(posts, i, get_post_id(p));
		}
		g_ptr_array_free(array,TRUE);
	}
	return posts;
}

//Query 8
LONG_list contains_word(TAD_community com, char word[], int N) {

	int i;
	long id;

	GSList *posts = NULL;
	posts = posts_with_word(com->posts, word, N);
	posts = g_slist_reverse(posts);

	LONG_list res = create_list(N);

	for(i = 0; i < N; i++){
		id = get_post_id(g_slist_nth_data(posts, i));
		set_list(res, i, id);
	}
	g_slist_free (posts);
	return res;
}

//Query 9
LONG_list both_participated(TAD_community com, long id1, long id2, int N){
	int i;
	long id;

	LONG_list posts = create_list(N);

	GSList *result = get_common_posts(com->users, com->posts, id1, id2, N);
	result = g_slist_reverse(result);

	for(i = 0; i < N; i++){
		id = get_post_id(g_slist_nth_data(result, i));
		set_list(posts, i, id);
	}
	g_slist_free (result);
	return posts;
}

//Query 10
long better_answer(TAD_community com, long id){
	long res = get_best_answer(com->posts, com->users, id);
	return res;
}

//Query 11
LONG_list most_used_best_rep(TAD_community com, int N, Date begin, Date end){
	LONG_list result = create_list(N);

	if(valid_date(begin) && valid_date(end)){

		int i;
		long tag_id;
		LONG_pair par;

		GSList* top_users_list = NULL;
		GSList* posts_list = NULL;
		GSList* tags_list = NULL;

		top_users_list = get_top_users_rep(com->users, N);
		posts_list = get_top_users_posts(top_users_list, begin, end);

		tags_list = tag_counter(posts_list);

		for(i = 0; i < N; i++){
			par = g_slist_nth_data(tags_list, i);
			tag_id = get_fst_long(par);
			set_list(result, i, tag_id);
		}

		g_slist_free(top_users_list);
		g_slist_free(posts_list);
		g_slist_free_full (tags_list,(GDestroyNotify) free_long_pair);

	}
	return result;
}

//Clean
TAD_community clean(TAD_community com){

	g_hash_table_destroy(com->users);

	g_hash_table_destroy(com->posts);

	g_hash_table_destroy(com->dates);

	g_hash_table_destroy(com->tags);

	free(com);

	return com;
}
