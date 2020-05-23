/** \file stack_post.c
 * Módulo da estrutura Post
 * Contém a definição de Post e funções relativas a esta estrutura
 * @author Grupo69
 *
 * @date 01/05/2018
 */
#include <stack_post.h>
#include <common.h>

struct post{
	long  id;
	int  type;
	long  id_parent;
	long  id_user;
	char* title;
	int score;
	int answers;
	GDateTime* date;
	GSList* tags;
};

Post init_post(long post_id, int post_type, long id_user, int score, GDateTime* date){

	Post p = malloc(sizeof(struct post));
	p->title = NULL;
	p->id_parent = -1;
  p->id = post_id;
  p->type = post_type;
  p->id_user = id_user;
  p->score = score;
	p->date = date;
	p->answers = 0;
	p->tags = NULL;
	return p;
}
//getters
long* get_post_id_ptr(Post p){
	if(p)
		return &(p->id);
	return NULL;
}

long get_post_id(Post p){
	if(p)
		return p->id;
	return -1;
}

long get_post_user(Post p){
  if(p)
    return p->id_user;
  return 0;
}

int get_post_type(Post p){
	if(p)
		return p->type;
	return -1;
}

int get_post_score(Post p){
	if(p)
		return p->score;
	return -1;
}


long get_post_pid(Post p){
  if(p) {
  if(p->type == 1) return p->id;
	if(p->type == 2) return p->id_parent;
  }
  return -1;
}

GDateTime* get_post_date(Post p){
  if(p)
		return p->date;
  return NULL;
}

char* get_post_title(Post p){
	if(p)
		return p->title;
	return NULL;
}

int get_post_answers(Post p){
	if(p && p->type == 1)
		return p->answers;
	return 0;
}

GSList* get_post_tags(Post p){
	if(p->type == 1)
		return p->tags;
	return NULL;
}

//setters

void set_post_title(Post p, char* title){
  p->title = mystrdup(title);
}

void set_post_parent(Post p, long parent_id){
  p->id_parent = parent_id;
}

void set_post_answers(Post p, int answers){
	p->answers = answers;
}

void set_post_date(Post p, GDateTime* date){
	g_date_time_unref (p->date);
	p->date = date;
}

void add_post_tags(Post p, GSList* tags){
	if(p) p->tags = tags;
}

//free
void free_post(Post p){
	if(p){
		free (p->title);
		g_slist_free(p->tags);
		free(p);
	}
}
