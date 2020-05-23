/** \file loader.c
 * Módulo de Load(e parse)
 * Contém as funções de carregamento da estrutura
 * @author Grupo69
 * @date 01/05/2018
 */

#include <loader.h>
#include <glib.h>
#include <string.h>
#include <common.h>
#include <libxml/xmlmemory.h>
#include <libxml/parser.h>
#include <stack_user.h>
#include <stack_post.h>

/* FUNÇÕES DE VALIDAÇÃO DO XML */

static int validDoc(xmlDocPtr doc){
	if (doc == NULL ) {
		fprintf(stderr,"Document not parsed successfully. \n");
		return 0;
	}
	return 1;
}

static int validCur(xmlNodePtr cur, char* keyword){
	if (cur == NULL) {
		fprintf(stderr,"empty document\n");
		return 0;
	}
	if (xmlStrcmp(cur->name, (const xmlChar *) keyword)) {
		fprintf(stderr,"document of the wrong type, root node != %s\n", keyword);
		return 0;
	}

	return 1;
}

/* FUNÇÕES AUXILIARES */

static void add_post(GHashTable* users, long user_id, Post post){
	User u;
	u = g_hash_table_lookup(users, &user_id);
	if(u) add_user_post(u, post);
}

static GSList* add_tags(GHashTable* tags, char* tag_list){
	GSList* list = NULL;
	long id_tag;
	char* tmp = mystrdup(tag_list);
	char* tag = strtok(tmp, "<>");

	while(tag != NULL){
		id_tag = (long) g_hash_table_lookup(tags, tag);
		if(id_tag){
			list = g_slist_append(list, (long *) id_tag);
		}
		tag = strtok(NULL, "<>");
	}
	free(tmp);
	return list;
}


static int date_time_compare (gconstpointer dt1, gconstpointer dt2, gpointer user_data){
	return g_date_time_compare(dt1,dt2);
}

static void insert_hash_date(GHashTable* dates, Post p, GDateTime* d){
	GDateTime* date = g_date_time_new(g_time_zone_new_utc(), g_date_time_get_year (d), g_date_time_get_month (d), g_date_time_get_day_of_month (d), 0, 0, 0);

	GTree* date_time = NULL;
	gpointer* test = NULL;
	date_time = g_hash_table_lookup(dates, date);

	if(date_time) {	//se o dia já existe na hashtable
		test =  g_tree_lookup (date_time, d);
		if(test){
			d = g_date_time_add_seconds(d,0.001);
			set_post_date(p,d);
		}
		g_tree_insert (date_time, d, p);
		g_date_time_unref(date);
		}
else{
	date_time = g_tree_new_full((GCompareDataFunc) date_time_compare,NULL,(GDestroyNotify)g_date_time_unref,NULL);
	g_hash_table_insert(dates, date, date_time);
	g_tree_insert (date_time, d, p);
}

}

/* FUNÇÕES DE PARSE */

//Função de Load dos Users
void load_users(GHashTable* users, char* path) {

	char filepath[100];
	char* filename = "Users.xml";
	sprintf(filepath, "%s%s", path, filename);

	xmlChar *id, *name, *bio, *rep;

  long user_id;
  int reputation;

  User u;

	clock_t begin = clock();

	xmlDocPtr doc = xmlParseFile(filepath);
	if(!validDoc(doc)) return;

	xmlNodePtr cur = xmlDocGetRootElement(doc);
	if(!validCur(cur,"users")){xmlFreeDoc(doc); return;}


	cur = cur->xmlChildrenNode;
	while (cur != NULL) {
		if ((!xmlStrcmp(cur->name, (const xmlChar *)"row"))){

			id = xmlGetProp(cur, (const xmlChar *) "Id");
			user_id = strtol((const char *)id, NULL, 0);

			rep = xmlGetProp(cur, (const xmlChar *) "Reputation");
			reputation = atoi((const char *)rep);

			name = xmlGetProp(cur, (const xmlChar *) "DisplayName");

			bio = xmlGetProp(cur, (const xmlChar *) "AboutMe");

      u = init_user((char*) name, (char*)bio, user_id, reputation);

			g_hash_table_insert(users, get_user_id_ptr(u), u);

			xmlFree(id);
			xmlFree(name);
			xmlFree(bio);
			xmlFree(rep);
		}
		cur = cur->next;
	}

	clock_t end = clock();
	double time_spent = (double) (end - begin) / CLOCKS_PER_SEC;

	xmlFreeDoc(doc);
	printf("%s loaded successfully\n",filepath);

	printf("users_load_time: %lfs\n", time_spent);
}

//Função de Load dos Posts
void load_posts(GHashTable *posts, GHashTable *users, GHashTable *dates, GHashTable *tags, char* path) {

	char filepath[100];
	char* filename = "Posts.xml";
	sprintf(filepath, "%s%s", path, filename);

	xmlChar *id, *title, *id_user, *type, *id_parent, *date, *score, *tag_list, *answers;

	long post_id, user_id, parent_id;

	int post_type , post_score, post_answers;
	GDateTime *post_date;

	Post p;

	clock_t begin = clock();

	xmlDocPtr doc = xmlParseFile(filepath);
	if(!validDoc(doc)) return;

	xmlNodePtr cur = xmlDocGetRootElement(doc);
	if(!validCur(cur,"posts")){xmlFreeDoc(doc); return;}

	cur = cur->xmlChildrenNode;
	while (cur != NULL) {
		if ((!xmlStrcmp(cur->name, (const xmlChar *)"row"))){

			type = xmlGetProp(cur, (const xmlChar *) "PostTypeId");
			post_type = atoi((const char *)type);

			if(post_type == 1 || post_type == 2){

				id        = xmlGetProp(cur,(const xmlChar *) "Id");
				date      = xmlGetProp(cur, (const xmlChar *) "CreationDate");
				score     = xmlGetProp(cur, (const xmlChar *) "Score");
				id_parent = xmlGetProp(cur, (const xmlChar *) "ParentId");
				id_user   = xmlGetProp(cur, (const xmlChar *) "OwnerUserId");
				title     = xmlGetProp(cur,(const xmlChar *) "Title");
				tag_list  = xmlGetProp(cur, (const xmlChar *) "Tags");
				answers   = xmlGetProp(cur,(const xmlChar *) "AnswerCount");

				post_id = strtol((const char *)id, NULL, 0);
				post_date = g_date_time_new_from_iso8601((gchar *) date, g_time_zone_new_utc());
				post_score = strtol((const char *)score, NULL, 0);
				user_id = strtol((const char *)id_user, NULL, 0);

				p = init_post(post_id, post_type, user_id, post_score, post_date);

				insert_hash_date(dates, p, post_date);
				if (post_type == 1){
					set_post_title(p, (char *)title);
					add_post_tags(p, add_tags(tags, (char*) tag_list));
					post_answers = atoi((const char *)answers);
					set_post_answers(p, post_answers);
				}

				if(post_type == 2){
					parent_id = strtol((const char *)id_parent, NULL, 0);
					set_post_parent(p, parent_id);
				}

				add_post(users, get_post_user(p), p);

				g_hash_table_insert(posts, get_post_id_ptr(p), p);

				xmlFree(id);
				xmlFree(date);
				xmlFree(id_parent);
				xmlFree(id_user);
				xmlFree(score);
				xmlFree(title);
				xmlFree(tag_list);
				xmlFree(answers);
			}
			xmlFree(type);
		}
		cur = cur->next;
	}

	clock_t end = clock();

	double time_spent = (double) (end - begin) / CLOCKS_PER_SEC;

	xmlFreeDoc(doc);
	printf("%s loaded successfully!\n",filepath);
	printf("posts_load_time: %lfs\n", time_spent);
}

//Função de Load das Tags
void load_tags(GHashTable* tags, char* path) {

	char filepath[100];
	char* filename = "Tags.xml";
	sprintf(filepath, "%s%s", path, filename);

	xmlChar *id, *tag;

	char* tag_name;

	clock_t begin = clock();

	xmlDocPtr doc = xmlParseFile(filepath);
	if(!validDoc(doc)) return;

	xmlNodePtr cur = xmlDocGetRootElement(doc);
	if(!validCur(cur,"tags")){xmlFreeDoc(doc); return;}


	cur = cur->xmlChildrenNode;
	while (cur != NULL) {
		if ((!xmlStrcmp(cur->name, (const xmlChar *)"row"))){
			long tag_id;

			id = xmlGetProp(cur, (const xmlChar *) "Id");
			tag_id = strtol((const char *)id, NULL, 0);

			tag = xmlGetProp(cur, (const xmlChar *) "TagName");
			tag_name = mystrdup((char*) tag);

			g_hash_table_insert(tags, tag_name, (long *) tag_id);

			xmlFree(id);
			xmlFree(tag);
		}
		cur = cur->next;
	}

	clock_t end = clock();
	double time_spent = (double) (end - begin) / CLOCKS_PER_SEC;

	xmlFreeDoc(doc);
	printf("%s loaded successfully\n",filepath);

	printf("tags_load_time: %lfs\n", time_spent);
}
