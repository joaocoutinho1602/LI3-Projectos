package li3;

import common.Pair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** Classe View da implementação MVC
 *  É únicamente responsável por gerar outputs, que têm como destino o Utilizador.
 *  Contém a representação visual do menu das queries.
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class View {
  
    private Scanner input = new Scanner(System.in);

    public int menu() {
            System.out.println("*********************");
            System.out.println(
                            "Seleccione uma Query: \n" +
                            " 1) Get info from Post\n" +
                            " 2) Top Most Active Users\n" +
                            " 3) Total Posts\n" +
                            " 4) Questions with Tag\n" +
                            " 5) Get User Info\n" +
                            " 6) Most Voted Answers\n" +
                            " 7) Most Answered Questions\n" +
                            " 8) Contains Word\n" +
                            " 9) Both Participated\n" +
                            "10) Better Answer\n" +
                            "11) Most Used Tags by Best Reputation Users\n" +
                            "-----------------------\n" +
                            "12) Sair\n"
            );
            do {
                int selection = input.nextInt();
                input.nextLine();
                switch (selection) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    case 3:
                        return 3;
                    case 4:
                        return 4;
                    case 5:
                        return 5;
                    case 6:
                        return 6;
                    case 7:
                        return 7;
                    case 8:
                        return 8;
                    case 9:
                        return 9;
                    case 10:
                        return 10;
                    case 11:
                        return 11;
                    case 12:
                        return 12;
                    default:
                        return -1;
                }
            }while(true);
    }

    public int askInt(String s){
        System.out.println(s+"\n");
        int n = input.nextInt();
        input.nextLine();
        return n;
    }

    public long askLong(String s){
        System.out.println(s+"\n");
        long n = input.nextLong();
        input.nextLine();
        return n;
    }

    public String askString(String s){
        System.out.println(s+"\n");
        String res = input.nextLine();
        return res;
    }

    public Pair askTwoDates(){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            System.out.println("Insira a data inicial(aaaa-mm-dd): ");
            String data1 = input.nextLine();
            LocalDate date1 = LocalDate.parse(data1, formatter);
            System.out.println("Insira a data final(aaaa-mm-dd): ");
            String data2 = input.nextLine();
            LocalDate date2 = LocalDate.parse(data2, formatter);
            Pair<LocalDate,LocalDate> p = new Pair<>(date1, date2);
            return p;
    }


    public void exit() {

        System.out.println("A Sair..");
    }

    public void printPair(Pair p, String s1, String s2){
        StringBuilder sb = new StringBuilder();
        if(p.getFst() != null) sb.append(s1 + p.getFst());
        else sb.append(s1 + "NULL ");
        sb.append(s2 + p.getSnd());
        System.out.println(s1 + p.getFst().toString()+"\t" + s2 + p.getSnd().toString());
    }

    public void printList(List l){

        System.out.println(Arrays.toString(l.toArray()));
    }

    public void printBio(String s){

        System.out.println(s);
    }

    public void printValue(Long i, String s){

        System.out.println(s + i);
    }

    public void printError(String s, String id){

        System.out.println(s+id);
    }

}
