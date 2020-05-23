/** \file test.c
 * Módulo Teste
 * Contém funções usadas unicamente para testar outputs e imprimir valores
 * @author Grupo69
 *
 * @date 01/05/2018
 */
#include <test.h>
#include <stdio.h>
#include <stack_user.h>
#include <stack_post.h>

struct llist {
  int size;
  long * list;
};

struct user{
	char* name;
	char* bio;
	long id;
	int reputation;
	int postcount;
	GSList* posts;
};

struct TCD_community{
	GHashTable *posts;
	GHashTable *users;
	GHashTable *dates;
	GHashTable *tags;
};

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

void imprime_users(TAD_community com){
	GList *l;
	User u;

	l = g_hash_table_get_keys(com->users);
	while(l != NULL){
		u = (User) g_hash_table_lookup(com->users, l->data);
		if(u->reputation > 20000){
			printf("\nName: %s\n", u->name);
			printf("Id: %ld\n", u->id);
			printf("PostCount: %d\n", u->postcount);
			printf("Reputation: %d\n", u->reputation);
			//print_list(u->posts);
		}
		l = l->next;
	}
}


void imprime_tags(TAD_community com){
	GList *l;
	long id;

	l = g_hash_table_get_keys(com->tags);
	while(l != NULL){
		if(1){
      id = (long) g_hash_table_lookup(com->tags, l->data);
			printf("\nId: %ld,  Tag: %s\n",id ,(char *) l->data);
		}
		l = l->next;
	}
}

void printGSList(GSList *tags){

	GSList* iterator = NULL;
  long tag_id;

	for (iterator = tags; iterator; iterator = iterator->next) {
    tag_id = (long) iterator->data;
  	printf("Current item is '%ld'\n", tag_id);
 	}

}

void imprime_posts(TAD_community com){
	GList *l;
	Post p;
	int i=0;

	l = g_hash_table_get_keys(com->posts);
	while(l != NULL){
		p = (Post) g_hash_table_lookup(com->posts, l->data);
		if(1){

			printf("\nID: %ld\n", p->id);
			printf("Type: %d\n", p->type);
			if(p->type == 2) printf("Parent ID: %ld\n", p->id_parent);
			printf("User ID: %ld\n", p->id_user);
			printf("Score: %d\n", p->score);
			printf("Answers: %d\n", p->answers);
			printf("Data: %s\n", g_date_time_format (p->date, "%F"));
			if(p->type == 1) printGSList(p->tags);
			i++;
		}
		l = l->next;
	}
  //l = g_list_first(l);
	printf("%d posts printed...\n",i);
  printf("%d posts na lista...\n",g_list_length(l));
	g_list_free(l);
}

gboolean iter_all(gpointer key, gpointer value, gpointer data) {
  long* count = (long*) data;
  (*count)++;
	return FALSE;
}

void print_tree(GTree* t,long* count){
	g_tree_foreach(t, (GTraverseFunc)iter_all, count);

}

void imprime_datas(TAD_community com){
	GList* l;
	//GDateTime* d;
	GTree* t;
  long* count = malloc(sizeof(long*));
  *count = 0;
	l = g_hash_table_get_keys(com->dates);
	while(l != NULL){
		printf("%s\n", g_date_time_format (l->data, "%F %T" ));
		t = g_hash_table_lookup(com->dates, l->data);
		print_tree(t,count);
		//printf("%s\n", g_date_time_format (d, "%F %T" ));

		l = l->next;
		}
    printf("%ld posts....\n",*count);
}

void post_info(TAD_community com){
  long *id = malloc(sizeof(long));
  printf("\nId do Post:\n");
  scanf("%ld", id);
  Post p = (Post) g_hash_table_lookup(com->posts, id);
  if(p){
    printf("\nID: %ld\n", p->id);
    printf("Type: %d\n", p->type);
    if(p->type == 2) printf("Parent ID: %ld\n", p->id_parent);
    printf("User ID: %ld\n", p->id_user);
    printf("Score: %d\n", p->score);
    printf("Answers: %d\n", p->answers);
    printf("Data: %s\n", g_date_time_format (p->date, "%F"));
    //if(p->type == 1) printGSList(p->tags);
  }
}

void user_info(TAD_community com){
  long *id = malloc(sizeof(long));
  printf("\nId do User:\n");
  scanf("%ld", id);
  User u = (User) g_hash_table_lookup(com->users, id);
  if(u){
    printf("\nID: %ld\n", u->id);
    printf("Name: %s\n", u->name);
    printf("Bio: %s\n", u->bio);
    printf("PostCount: %d\n", u->postcount);
    //print_posts(u->posts);
  }
}

void tag_info(TAD_community com){
  char* tag = malloc(sizeof(char));
  long id;
  printf("\nTag: ");
  scanf("%s", tag);
  id = (long) g_hash_table_lookup(com->tags, tag);

  printf("\nTag: <%s>  ID: %ld\n", tag, id);
}

void print_g_ptr_array(GPtrArray* array){
  long len = array->len;
  char tag[64];

  int i = 0;

  for(i = 0; i < len; i++){
    strcpy(tag, g_ptr_array_index(array, i));
    puts(tag);
  }
}

//query 1
void query1(TAD_community com){
	long id;
	STR_pair resultado;

	clock_t begin = clock();

	//imprime_users(com);

		printf("Insira o ID de um post: ");
		scanf("%ld",&id);
		resultado=info_from_post(com,id);
		printf("Titulo Post: %s  OwnerUserId: %s\n",get_fst_str(resultado), get_snd_str(resultado));

	clock_t end = clock();
	double time_spent = (double) (end - begin) / CLOCKS_PER_SEC;

	printf("time_spent: %lfs\n", time_spent);
}

//query 2
void query2(TAD_community com){
	LONG_list l;
	long i;
	long id;
	int N;

		printf("Insira o tamanho do top : ");
		scanf("%d",&N);
		clock_t begin = clock();

		l = top_most_active(com, N);

		for (i = 0; i < N; i++){
			id = get_list(l, i);
			printf("TOP %ld - %ld \n",i+1 ,id);
		}
		clock_t end = clock();
		double time_spent = (double) (end - begin) / CLOCKS_PER_SEC;

		printf("time_spent: %lfs\n", time_spent);



}

//query 3
void query3(TAD_community com){
	int dia, mes, ano;
	int dia2, mes2, ano2;
	printf("Insira a data de inicio:\n");

	scanf("%4d-%2d-%2d", &ano, &mes, &dia);
	Date data1 = createDate(dia, mes, ano);

	printf("Insira a data do fim:\n");
	scanf("%4d-%2d-%2d", &ano2, &mes2, &dia2);
	Date data2 = createDate(dia2, mes2, ano2);

	LONG_pair l = total_posts(com, data1, data2);
	printf("%ld posts, %ld answers\n",get_fst_long(l),get_snd_long(l));
}

//query 4
void query4(TAD_community com){
	char tag[64];

	printf("Insira uma tag:\n");

	scanf("%s", tag);

  int dia, mes, ano;
  int dia2, mes2, ano2;
  printf("Insira a data de inicio:\n");

  scanf("%4d-%2d-%2d", &ano, &mes, &dia);
  Date data1 = createDate(dia, mes, ano);

  printf("Insira a data do fim:\n");
  scanf("%4d-%2d-%2d", &ano2, &mes2, &dia2);
  Date data2 = createDate(dia2, mes2, ano2);
	LONG_list list = questions_with_tag(com, tag, data1, data2);

	int i;
	long p;
  if (list){
	for (i = 0; i < list->size; i++){
		p = get_list(list, i);
		printf("ID: %ld\n", p);
	}
  }
}

//query 5
void query5(TAD_community com){
	int num;
	int i;
	long *posts;
	USER u;

	clock_t begin = clock();

	printf("Insira um número de um user: ");
	scanf("%d", &num);

	u = get_user_info(com, (long)num);

	posts = get_10_latest_posts(u);
	for(i = 0; i < 10; i++)
	printf("\n %ld", posts[i]);

	printf("\n%s", get_bio(u));

	clock_t end = clock();
	double time_spent = (double) (end - begin) / CLOCKS_PER_SEC;

	printf("time_spent: %lfs\n", time_spent);
}

//query 6
void query6(TAD_community com){
	int i, N;

	printf("Quantas respostas mais votadas?\n");
	scanf("%d",&N);

  int dia, mes, ano;
	int dia2, mes2, ano2;
	printf("Insira a data de inicio:\n");

	scanf("%4d-%2d-%2d", &ano, &mes, &dia);
	Date data1 = createDate(dia, mes, ano);

	printf("Insira a data do fim:\n");
	scanf("%4d-%2d-%2d", &ano2, &mes2, &dia2);
	Date data2 = createDate(dia2, mes2, ano2);
	clock_t begin = clock();

	LONG_list lista = most_voted_answers(com, N, data1, data2);

	for(i = 0; i < N; i++)
	printf("\n %ld \n", get_list(lista, i));

	clock_t end = clock();
	double time_spent = (double) (end - begin) / CLOCKS_PER_SEC;

	printf("time_spent: %lfs\n", time_spent);
}

//query 7
void query7(TAD_community com){
	int i, N;


	printf("Quantas respostas com mais perguntas?\n");
	scanf("%d",&N);

  int dia, mes, ano;
	int dia2, mes2, ano2;
	printf("Insira a data de inicio:\n");

	scanf("%4d-%2d-%2d", &ano, &mes, &dia);
	Date data1 = createDate(dia, mes, ano);

	printf("Insira a data do fim:\n");
	scanf("%4d-%2d-%2d", &ano2, &mes2, &dia2);
	Date data2 = createDate(dia2, mes2, ano2);

  clock_t begin = clock();

	LONG_list lista = most_answered_questions(com, N, data1, data2);

	for(i = 0; i < N; i++)
	printf("\n %ld \n", get_list(lista, i));

	clock_t end = clock();
	double time_spent = (double) (end - begin) / CLOCKS_PER_SEC;

	printf("time_spent: %lfs\n", time_spent);
}

//query 8

void query8(TAD_community com){
	char word[256];
	int n, i;

	printf("Qual a palavra a procurar?\n");
	scanf("%s", word);
  printf("a palavra é %s\n", word);
	printf("Quantos posts a apresentar?\n");
	scanf("%d", &n);

	LONG_list res = create_list(n);

	res = contains_word(com, word, n);

	for(i = 0; i < n; i++){
		printf("%ld\n", get_list(res, i));
	}
}

//query 9
void query9(TAD_community com){
	long num1,num2;
	int i,N;
	LONG_list lista;

	while(1){
	printf("Insira um número de um user: ");
	scanf("%ld", &num1);
	printf("Insira um número de outro user: ");
	scanf("%ld", &num2);

	printf("Insira o N: ");
	scanf("%d",&N);

	clock_t begin = clock();

	lista = both_participated(com, num1, num2, N);

	for(i = 0; i < N; i++)
	printf("\n %ld\n", get_list(lista, i));

	clock_t end = clock();
	double time_spent = (double) (end - begin) / CLOCKS_PER_SEC;

	printf("time_spent: %lfs\n", time_spent);
	}
}

//query10
void query10(TAD_community com){
	long id, id2;


	printf("Insira o ID de um post: ");
  scanf("%ld", &id);

	id2 = better_answer(com, id);

	printf("%ld\n", id2);
}

//query 11

void query11(TAD_community com){
  int N;
  int i;

  printf("Insira o N: ");
	scanf("%d",&N);
  int dia, mes, ano;
	int dia2, mes2, ano2;
	printf("Insira a data de inicio:\n");

	scanf("%4d-%2d-%2d", &ano, &mes, &dia);
	Date data1 = createDate(dia, mes, ano);

	printf("Insira a data do fim:\n");
	scanf("%4d-%2d-%2d", &ano2, &mes2, &dia2);
	Date data2 = createDate(dia2, mes2, ano2);

  LONG_list res = most_used_best_rep(com, N, data1, data2);

  for(i = 0; i < N; i++)
	printf("\ntag: %ld\n", get_list(res, i));

  //imprimir res
}
/*
void run_all_queries(TAD_community com){
  Date begin = createDate(01,01,2014);
  Date end = createDate(31,12,2014);

  STR_pair q1 = info_from_post(com, 10);
  LONG_list q2 = top_most_active(com, 20);
  LONG_pair q3 = total_posts(com, begin, end);
  LONG_list q4 = questions_with_tag(com, "sms", begin, end);
  USER q5 = get_user_info(com, 1);
  LONG_list q6 = most_voted_answers(com, 20, begin, end);
  LONG_list q7 = most_answered_questions(com, 20, begin, end);
  LONG_list q8 = contains_word(com, "processor", 20);
  LONG_list q9 = both_participated(com, 15, 19, 20);
  long q10 = better_answer(com, 10);
  LONG_list q11 = most_used_best_rep(com, 20, begin, end);
}*/
