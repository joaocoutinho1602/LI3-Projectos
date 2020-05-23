#include <date.h>
#include <stdio.h>
#include <interface.h>
#include <test.h>

int main(){

  int n;

  TAD_community com = init();
  com = load(com, "./dump/ubuntu/");

  //imprime_users(com);
  //imprime_posts(com);
  //imprime_datas(com);
  //imprime_tags(com);

  while(1){
  printf("\tQuerys:\n");
  printf("*************************************\n");
  printf("1)  - info from post..........(DONE)\n");
  printf("2)  - top most active.........(DONE)\n");
  printf("3)  - total posts.............(DONE)\n");
  printf("4)  - questions with tag......(DONE)\n");
  printf("5)  - get user info...........(DONE)\n");
  printf("6)  - most voted answers......(DONE)\n");
  printf("7)  - most answered questions.(DONE)\n");
  printf("8)  - contains word...........(DONE)\n");
  printf("9)  - both participated.......(DONE)\n");
  printf("10) - better answer...........(DONE)\n");
  printf("11) - most used best rep......(DONE)\n");
  printf("------------------------------------\n");
  printf("12) - imprime posts                 \n");
  printf("13) - imprime users                 \n");
  printf("14) - imprime dates                 \n");
  printf("15) - post info                     \n");
  printf("16) - user info                     \n");
  printf("17) - tag info                      \n");
  printf("18) - run all queries               \n");
  printf("20) - exit                          \n");
  printf("*************************************\n");
  scanf("%d",&n);

  switch(n) {

   case 1  :
      query1(com);
      break;
   case 2  :
      query2(com);
      break;
   case 3  :
      query3(com);
      break;
   case 4 :
      query4(com);
      break;
   case 5 :
      query5(com);
      break;
   case 6 :
       query6(com);
       break;
   case 7 :
       query7(com);
       break;
   case 8 :
       query8(com);
       break;
   case 9 :
      query9(com);
      break;
   case 10 :
      query10(com);
      break;
   case 11 :
      query11(com);
      break;
   case 12 :
      imprime_posts(com);
      break;
   case 13 :
      imprime_users(com);
      break;
   case 14 :
      imprime_datas(com);
      break;
   case 15 :
      post_info(com);
      break;
    case 16 :
      user_info(com);
      break;
    case 17 :
      tag_info(com);
      break;
    case 18 :
    //run_all_queries(com);
    break;
   case 20 :
      clean(com);
      return 1;
      break;

   default :
      printf("Error! operator is not correct\n");
      break;
  }
}
  clean(com);
  return 0;
}
