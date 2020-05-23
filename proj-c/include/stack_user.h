#ifndef __STACK_USER_H__
#define __STACK_USER_H__

#include <stack_post.h>
#include <glib.h>

typedef struct user* User;

User init_user(char* name, char* bio, long id, int reputation);

char* get_user_name(User u);

char* get_user_bio(User u);

long get_user_id(User u);

long* get_user_id_ptr(User u);

int get_user_reputation(User u);

int get_user_count(User u);

GSList* get_user_posts(User u);

void add_user_post(User u, Post p);

void free_stack_user(User u);


#endif
