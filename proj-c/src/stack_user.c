/** \file stack_user.c
 * Módulo da estrutura User
 * Contém a definição de User e funções relativas a esta estrutura
 * @author Grupo69
 *
 * @date 01/05/2018
 */
#include <stack_user.h>
#include <common.h>

struct user{
	char* name;
	char* bio;
	long id;
	int reputation;
	int postcount;
	GSList* posts;
};

User init_user(char* name, char* bio, long id, int rep){

	User u = malloc(sizeof(struct user));
	u->posts = NULL;
	u->name = mystrdup(name);
	u->bio = mystrdup(bio);
	u->id = id;
	u->reputation = rep;
	u->postcount = 0;

	return u;
}

// =========Funções de get==========
char* get_user_name(User u){
	if(u)
		return u->name;
	return NULL;
}

char* get_user_bio(User u){
	if(u)
		return u->bio;
	return NULL;
}

long get_user_id(User u){
	if(u)
		return u->id;
	return -1;
}

long* get_user_id_ptr(User u){
	if(u)
		return &(u->id);
	return NULL;
}

int get_user_reputation(User u){
	if(u)
		return u->reputation;
	return 0;
}

int get_user_count(User u){
	if(u)
		return u->postcount;
	return 0;
}

GSList* get_user_posts(User u){
	if(u)
		return u->posts;
	return NULL;
}

// Funções de set
void add_user_post(User u, Post p){
	 u->posts = g_slist_append(u->posts, p);
	 u->postcount += 1;

}

// Free
void free_stack_user(User u){
	if(u){
		free (u->name);
		free (u->bio);
		g_slist_free (u->posts);
		free(u);
	}
}
