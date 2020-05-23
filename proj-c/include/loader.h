/** \file loader.h
 * Módulo de Load(e parse)
 * Contém as assinaturas das funções de parse
 * @author Grupo69
 * @date 01/05/2018
 */
#ifndef __LOADER_H__
#define __LOADER_H__
#include <glib.h>


void load_users(GHashTable *users, char* path);

void load_posts(GHashTable *posts, GHashTable *users, GHashTable *dates, GHashTable *tags, char* path);

void load_votes(GHashTable *posts, char *path);

void load_tags(GHashTable *tags, char *path);

#endif
