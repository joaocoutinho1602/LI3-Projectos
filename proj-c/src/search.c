/** \file search.c
 * Módulo Search
 * Contém funções auxiliares de procura, usadas pelo módulo Interface
 * @author Grupo69
 * @date 01/05/2018
 */

#include <search.h>
#include <stdio.h>
#include <common.h>

typedef gboolean (*Iter_function)(void *, void *, void *);


//Validação de datas
int valid_date(Date date){
	GDateTime *gdate = g_date_time_new (g_time_zone_new_utc (),get_year(date),get_month(date),get_day(date),0,0,0);
	if(gdate) {g_date_time_unref (gdate);return 1;}
	else perror("WRONG DATE");

	g_date_time_unref(gdate);

	return 0;
}

/* FUNÇÔES ÚTEIS */

static Post get_post(GHashTable *posts, long id){
	Post p = g_hash_table_lookup(posts, &id);
	return p;
}

static User get_user(GHashTable *users, long id){
	User u = g_hash_table_lookup(users, &id);
	return u;
}

//Função que transforma uma data em GDateTime
GDateTime* date_to_gdate(Date date){
	GDateTime *gdate;
	gdate = g_date_time_new (g_time_zone_new_utc (),get_year(date),get_month(date),get_day(date),0,0,0);

	return gdate;
}

GPtrArray* init_array(GPtrArray* array, int N){
	while(array->len < N){
			g_ptr_array_add(array,NULL);
	}
	return array;
}

/* FUNÇÕES DE COMPARE */

//Função que verifica se uma data está dentro de um range
static int between_dates(GDateTime* given, GDateTime* begin, GDateTime* end){

	if(g_date_time_compare(given, begin) >= 0 && g_date_time_compare(given, end) <= 0) return 1;

	return 0;
}

static gint compare_date(gconstpointer a, gconstpointer b){
	return g_date_time_compare(get_post_date((Post) a),get_post_date((Post) b));
}

static int compare_count(User u1, User u2){
  int diff = (get_user_count(u1)) - (get_user_count(u2));
  return diff;
}

static int compare_score(gpointer a, gpointer b){
	Post* p1 = a;
	Post* p2 = b;

  int diff = (get_post_score(*p1)) - (get_post_score(*p2));

  return diff;
}

static int compare_rep(User a, User b){
	int diff = get_user_reputation(a) - get_user_reputation(b);
	return diff;
}

static int compare_tag_count(gconstpointer a, gconstpointer b){
	long count1 =	get_snd_long((LONG_pair) a);
	long count2 =	get_snd_long((LONG_pair) b);
	long res = count1 - count2;

	if(res == 0){
		return get_fst_long((LONG_pair) a) - get_fst_long((LONG_pair) b);
	}

	return res;
}

static int compare_tag_id(gconstpointer a, gconstpointer b){
	long id1 =	get_fst_long((LONG_pair) a);
	long id2 =	get_fst_long((LONG_pair) b);

	return id2 - id1;
}

static int compare_answers(gpointer a, gpointer b){
	Post* p1 = a;
	Post* p2 = b;
  int diff = (get_post_answers(*p1)) - (get_post_answers(*p2));
	if (diff == 0) return ((get_post_id(*p1))-get_post_id(*p2));
  return diff;
}


/**
\brief Função que percorre a HashTable de datas, e cada árvore num dado dia
@param dates Hashtable de Datas
@param bottom Data inicial
@param top Data final
@param f Função de iteração
@param apontador para uma estrutura que permita armazenar dados
*/
static GDateTime* add_day(GDateTime* date){
	GDateTime* tmp = g_date_time_add_days(date, 1);
	g_date_time_unref(date);
	return tmp;
}
void f_between_dates(GHashTable* dates, Date bottom, Date top, Iter_function f , gpointer data){
	GDateTime* begin = date_to_gdate(bottom);
	GDateTime* end = date_to_gdate(top);
	GDateTime* iter;
	GTree* tree;

	for(iter = begin;g_date_time_compare(iter,end)<= 0; iter = add_day(iter)){
		tree = g_hash_table_lookup(dates, iter);
		if (tree) g_tree_foreach(tree,(GTraverseFunc) f,data);
	}
	g_date_time_unref(end);
	g_date_time_unref(iter);
}

/* FUNÇÕES DAS QUERIES */

//QUERY 1

STR_pair get_post_info(GHashTable* posts, GHashTable* users, int id){
  STR_pair info;
  long post_id, user_id;

  Post p =  g_hash_table_lookup(posts, &id);

  if (p == NULL) return create_str_pair(NULL,NULL);

  if(get_post_type(p) == 2){
    post_id = get_post_pid(p);
    p = g_hash_table_lookup(posts, &post_id);

  }
  user_id = get_post_user(p);
  User u = g_hash_table_lookup(users, &user_id);

  info = create_str_pair(get_post_title(p),get_user_name(u));

  return info;
}

//QUERY 2

static GSList* add_user_top(User u, GSList* top){

  if(compare_count(u,top->data) > 0){
    top = g_slist_insert_sorted (top, u ,(GCompareFunc)compare_count);
    top = g_slist_remove(top,top->data);
  }
  return top;
}

GSList* get_top_users(GHashTable *users, int N){
  GList* l;
	GList* k;
  GSList* top = NULL;
  User u;
  int i = 1;
  l = g_hash_table_get_keys(users);
	k = l;
  while(l != NULL ){

    u = g_hash_table_lookup(users, l->data);
    if(i<=N) {top = g_slist_append(top,u);  top = g_slist_sort(top, (GCompareFunc) compare_count);}
    else {top = add_user_top(u, top);}
    i++;

    l = l->next;
  }
	g_list_free(k);
  return top;
}

//QUERY 3

gboolean count_posts(gpointer key, gpointer value, gpointer data) {
	Post p = (Post) value;
	long post, answer;
	int type = get_post_type(p);
	LONG_pair posts = (LONG_pair) data;
	if(type == 1) {post = get_fst_long(posts); set_fst_long(posts, post+1);}
	if(type == 2) {answer = get_snd_long(posts);set_snd_long(posts, answer+1);}
	return FALSE;
}

//QUERY 4

static int has_tag(long tag, GSList* post_tags){

	while(post_tags != NULL){
		if(tag == (long) post_tags->data) return 1;
		post_tags = post_tags->next;
	}
	return 0;
}

gboolean post_tags(gpointer key, gpointer value, gpointer data){
	GPtrArray* posts = (GPtrArray * ) data;

	long tag_id = (long) g_ptr_array_index(posts, 0);

	Post p = (Post) value;

	if(has_tag(tag_id, get_post_tags(p))) g_ptr_array_add(posts, p);

	return FALSE;
}



//QUERY 5

static long* get_last_10(GSList *post_list, int size){
  long i;
  long *posts;
  long id_post;

  posts = malloc(10*sizeof(long));

  for(i = 0; i < 10; i++){
    id_post = get_post_id(g_slist_nth_data (post_list,(guint) i));
    posts[i] = id_post; // ordem cronológica inversa
  }
  return posts;
}

char* get_shortbio(GHashTable *users, long id){
  User u = g_hash_table_lookup(users, &id);
	char* bio = mystrdup(get_user_bio(u));
	return bio;
}

long* get_post_history(GHashTable *users, long id){
  long *posts;
  GSList* post_list;
	User u = g_hash_table_lookup(users, &id);
  post_list = get_user_posts(u);
  post_list = g_slist_reverse(post_list);

  posts = get_last_10(post_list, get_user_count(u));

  return posts;
}

//QUERY 6

gboolean most_score(gpointer key, gpointer value, gpointer data) {

	GPtrArray* array = (GPtrArray*) data;
	Post p1 = (Post) value;
	Post p2 = (Post) g_ptr_array_index(array,0);

	if((get_post_type(p1) == 2) && get_post_score(p1) > get_post_score(p2)){
		g_ptr_array_remove(array,p2);
		g_ptr_array_add(array,p1);
		g_ptr_array_sort (array,(GCompareFunc) compare_score);
	}
	return FALSE;
}

//QUERY 7

gboolean most_answers(gpointer key, gpointer value, gpointer data){

	GPtrArray* array = (GPtrArray*) data;
	Post p1 = (Post) value;
	Post p2 = (Post) g_ptr_array_index(array,0);
	if(compare_answers(&p1,&p2)>0){
		g_ptr_array_remove(array,p2);
		g_ptr_array_add(array,p1);
		g_ptr_array_sort (array,(GCompareFunc) compare_answers);
	}
	return FALSE;
}

//QUERY 8

GSList* posts_with_word(GHashTable *posts, char word[], int N){
	int i = 1;

	Post p;

	GList *l;
	GList *k;
	l = g_hash_table_get_keys(posts); //lista de IDs de posts
	k = l;
	GSList *post_list = NULL;

	while(l != NULL) {
		p = g_hash_table_lookup(posts, l->data);
		if(get_post_type(p) == 1 && strstr(get_post_title(p), word)){
			if(i <= N) {
				post_list = g_slist_append(post_list,p);
				post_list = g_slist_sort(post_list,(GCompareFunc) compare_date);
			}
		  else {
				post_list = g_slist_insert_sorted(post_list, p, (GCompareFunc) compare_date);
			}
			i++;
		}
		l = l->next;
	}
	g_list_free(k);
	return post_list;
}

//QUERY 9

static GSList* add_common_post(GHashTable *posts, GSList* list, long id){
	Post p =  get_post(posts, id);

	if (!p) return list;

	list = g_slist_remove(list, p);
	list = g_slist_append(list, p);

	return list;
}


GSList* get_common_posts(GHashTable *users, GHashTable *posts, long id1, long id2, int N){

	int i, j, count1, count2;

  Post p1, p2;

	long id_parent1, id_parent2;

	GSList* post_list = NULL;

	User u1 = get_user(users, id1);
	if(!u1) return post_list;

	User u2 = get_user(users, id2);
	if(!u2) return post_list;

  count1 = get_user_count(u1);
  if(!count1) return post_list;
  count2 = get_user_count(u2);
  if(!count2) return post_list;

	for(i = 0; i < count1; i++){
		p1 = g_slist_nth_data(get_user_posts(u1), i);
		id_parent1 = get_post_pid(p1);

		for(j = 0; j < count2; j++){
				p2 = g_slist_nth_data(get_user_posts(u2), j);
				id_parent2 = get_post_pid(p2);
				if (id_parent1 == id_parent2 && id_parent1 != -1){
					post_list = add_common_post(posts, post_list, id_parent1);
				}
		}
	}
	post_list = g_slist_sort(post_list, (GCompareFunc)compare_date);
	return post_list;
}

//QUERY 10

static LONG_pair calc_score(Post p, User u, LONG_pair max){
	long this_score = (get_post_score(p) * 0.45) + (get_user_reputation(u) * 0.25) + (get_post_score(p) * 0.2) + (get_post_answers(p) * 0.1);

	if(this_score >= get_snd_long(max)){
		set_fst_long(max, get_post_id(p));
		set_snd_long(max, this_score);
	}
	return max;
}

long get_best_answer(GHashTable *posts, GHashTable *users, long idp){
	LONG_pair max = create_long_pair(0,0);

	Post p;
	User u;

	GList* l;
	GList* k;
	l = g_hash_table_get_keys(posts);
	k = l;
	while(l != NULL){
		p = g_hash_table_lookup(posts, l->data);

		if(get_post_type(p) == 2){
			if(get_post_pid(p) == idp){
				u = get_user(users, get_post_user(p));
				max = calc_score(p, u, max);
			}
		}
	l = l->next;
	}
	g_list_free(k);
	return get_fst_long(max);
}


//QUERY 11

GSList* get_top_users_rep(GHashTable *users, int N){
  GList* l;
	GList* k;
  GSList* top = NULL;
  User u;
  int i = 1;

	l = g_hash_table_get_keys(users);
	k = l;
  while(l != NULL ){

    u = g_hash_table_lookup(users, l->data);
    if(i<=N){
			top = g_slist_insert_sorted (top, u ,(GCompareFunc)compare_rep);
		}
    else {
			if(compare_rep(u,top->data) > 0){
				top = g_slist_remove(top,top->data);
				top = g_slist_insert_sorted (top, u ,(GCompareFunc)compare_rep);
			}
		}
    i++;

    l = l->next;
  }
	g_list_free(k);
  return top;
}

GSList* filter_posts(GSList* user_posts, Date begin, Date end){
	GSList* filtered = NULL;
	Post p;

	int countT1 = 0;
	int countT2 = 0;
	GDateTime* bottom = date_to_gdate(begin);
	GDateTime* top = date_to_gdate(end);

	while(user_posts != NULL){
		p = user_posts->data;
		if(get_post_type(p) == 2) countT2++;
		if((get_post_type(p) == 1) && between_dates(get_post_date(p), bottom, top)) {
			filtered = g_slist_append(filtered, p);
			countT1++;
		}
		user_posts = user_posts->next;
	}
	g_date_time_unref(top);
	g_date_time_unref(bottom);
	return filtered;
}

GSList* get_top_users_posts(GSList* users, Date begin, Date end){
	GSList* filtered_posts = NULL;
	GSList* user_posts = NULL;
	User u;

	while(users != NULL){
		u = users->data;
		user_posts = filter_posts(get_user_posts(u), begin, end);

		filtered_posts = g_slist_concat(filtered_posts, user_posts);

		user_posts = NULL;

		users = users->next;
	}

	return filtered_posts;
}



GSList* tag_adder(GSList* tag_count, GSList* tags){

	long tag_id;
	GSList* temp = NULL;
	LONG_pair res;

	while(tags != NULL){
		tag_id = (long) tags->data;
		LONG_pair tc = create_long_pair(tag_id, 1);

		temp = g_slist_find_custom(tag_count, tc, compare_tag_id);
		if(temp){
			res = (LONG_pair) temp->data;

			long count = 0;
			count = get_snd_long(res);
			count = count + 1;
			set_snd_long(res, count);
		}
		else{
			tag_count = g_slist_append(tag_count, tc);
		}


		tags = tags->next;
	}

	return tag_count;
}

GSList* tag_counter(GSList* posts){

	GSList* tag_count = NULL;
	Post p;

	while(posts != NULL){
		p = posts->data;

		tag_count = tag_adder(tag_count, get_post_tags(p));
		tag_count = g_slist_sort(tag_count,compare_tag_count);

		posts = posts->next;
	}

	tag_count = g_slist_reverse(tag_count);

	return tag_count;
}
