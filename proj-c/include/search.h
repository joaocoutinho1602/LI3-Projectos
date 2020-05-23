#define _GNU_SOURCE
#ifndef __SEARCH_H__
#define __SEARCH_H__
#include <string.h>
#include <glib.h>
#include <pair.h>
#include <date.h>
#include <stack_post.h>
#include <stack_user.h>

//auxiliares
GPtrArray* init_array(GPtrArray* array, int N);

void f_between_dates(GHashTable* dates, Date bottom, Date top, gboolean (*f)(void *, void *, void *), gpointer data);

int valid_date(Date date);

//query 1
STR_pair get_post_info(GHashTable* posts, GHashTable* users, int id);

//query 2
GSList* get_top_users(GHashTable *users, int N);

//query 3
gboolean count_posts(gpointer key, gpointer value, gpointer data);

//query 4
gboolean post_tags(gpointer key, gpointer value, gpointer data);

//query 5
char* get_shortbio(GHashTable *users, long id);

long* get_post_history(GHashTable *users, long id);

//query 6
gboolean most_score(gpointer key, gpointer value, gpointer data);

//query 7
gboolean most_answers(gpointer key, gpointer value, gpointer data);

//query 8
GSList* posts_with_word(GHashTable *posts, char* word, int N);

//query 9
GSList* get_common_posts(GHashTable *users, GHashTable *posts, long id1, long id2, int N);


//query 10
long get_best_answer(GHashTable *posts, GHashTable *users, long idp);

//query 11
GSList* get_top_users_rep(GHashTable *users, int N);

LONG_pair post_counter(GHashTable *dates, Date begin, Date end);

GSList* tag_counter(GSList* posts);

GSList* get_top_users_posts(GSList* users, Date begin, Date end);

#endif
