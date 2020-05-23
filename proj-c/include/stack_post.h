#ifndef __STACK_POST_H__
#define __STACK_POST_H__
#include <glib.h>

typedef struct post* Post;

Post init_post(long post_id, int post_type, long id_user, int score, GDateTime* date);

//funções get
long get_post_id(Post p);

long* get_post_id_ptr(Post p);

long get_post_user(Post p);

long get_post_pid(Post p);

char* get_post_title(Post p);

int get_post_type(Post p);

int get_post_votes(Post p);

int get_post_score(Post p);

int get_post_answers(Post p);

GSList* get_post_tags(Post p);

GDateTime* get_post_date(Post p);

//funções set
void set_post_title(Post p, char* title);

void set_post_parent(Post p, long parent_id);

void set_post_answers();

void set_post_date(Post p, GDateTime* date);

void add_post_vote(Post p, int v);

void add_post_tags(Post p, GSList* tags);

void free_post(Post p);

#endif
