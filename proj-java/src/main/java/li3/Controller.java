package li3;

import common.Pair;
import common.MyLog;
import engine.NotQuestionException;
import engine.Post;
import engine.PostDNEException;
import engine.UserDNEException;
import li3.TADCommunity;
import li3.View;

import java.time.LocalDate;
import java.util.List;

/** Classe Controller que faz a ligação entre o módulo View e Community
 *  É responsável pela interacção do Utilizador com a base de dados, através das várias queries.
 *  Guarda também os registos de tempo e resultados das várias interrogações.
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class Controller {
    /** Instância da classe View referente à componente com o mesmo nome do MVC */
    private View view;
    /** Variável que armazena a componente Model do MVC */
    private TADCommunity com;
    /** Variável de registo dos resultados das queries */
    private MyLog log;
    /** Variável de registo dos tempos de execução do programa */
    private MyLog logtime;
    /** Variáveis de armazenamento de tempos de execução */
    private long before, after;

    /** Construtor da componente Controller do MVC
     *
     */
    public Controller(){
        log = new MyLog("results");
        logtime = new MyLog("times");
    }

    /** Guarda o valor da componente View do MVC
     *
     * @param v Valor de View a ser guardado
     */
    public void setView(View v){
        this.view = v;
    }

    /** Guarda o valor da componente Model do MVC
     *
     * @param com Valor de Model a ser guardado
     */
    public void setModel(TADCommunity com){
        this.com = com;
    }

    public void init(String path){
        try {

            this.before = System.currentTimeMillis();
            this.com.init();
            this.com.load(path);
            this.after = System.currentTimeMillis();
            this.logtime.writeLog("LOAD -> "+(after-before)+" ms");
        } catch(IndexOutOfBoundsException e){
            System.out.println("Deve passar o caminho do dump como argumento.");
        }
        do{
            this.selection(this.view.menu());
        }while(true);
    }

    public void selection(int selection){
            switch (selection) {
                case 1:
                    this.query1();
                    break;
                case 2:
                    this.query2();
                    break;
                case 3:
                    this.query3();
                    break;
                case 4:
                    this.query4();
                    break;
                case 5:
                    this.query5();
                    break;
                case 6:
                    this.query6();
                    break;
                case 7:
                    this.query7();
                    break;
                case 8:
                    this.query8();
                    break;
                case 9:
                    this.query9();
                    break;
                case 10:
                    this.query10();
                    break;
                case 11:
                    this.query11();
                    break;
                case 12:
                    this.clean(com);
                    this.exit();
                    break;
                case -1:
                    this.view.printError("Escolha ", "Inválida");
                    break;
                default:
                    this.selection(this.view.menu());
                    break;

            }

    }

    private void query1(){
        try {
            long post = view.askLong("Insira o ID de um post:");
            before = System.currentTimeMillis();
            Pair p = com.infoFromPost(post);
            after = System.currentTimeMillis();
            logtime.writeLog("Query 1: -> "+(after-before)+" ms");
            log.writeLog("Query1 -> " + p);
            view.printPair(p, "Título: ", "Utilizador: ");
        }
        catch (PostDNEException e) {
            view.printError("Post não encontrado: ", e.getMessage());
        }
    }

    private void query2(){
        int top = view.askInt("Insira o número do top:");
        before = System.currentTimeMillis();
        List top_users = com.topMostActive(top);
        after = System.currentTimeMillis();
        logtime.writeLog("Query 2: -> "+(after-before)+" ms");
        log.writeLog("Query2 -> " + top_users);
        view.printList(top_users);
    }

    private void query3(){
        Pair<LocalDate, LocalDate> p = view.askTwoDates();
        before = System.currentTimeMillis();
        Pair<Long, Long> result = com.totalPosts(p.getFst(), p.getSnd());
        after = System.currentTimeMillis();
        logtime.writeLog("Query 3: -> "+(after-before)+" ms");
        log.writeLog("Query3 -> " + result);
        view.printPair(result, "Posts: ", "Answers: ");

    }

    private void query4(){
        String tag = view.askString("Insira uma tag para procurar:");
        Pair<LocalDate, LocalDate> p = view.askTwoDates();
        before = System.currentTimeMillis();
        List <Long> l = com.questionsWithTag(tag, p.getFst(), p.getSnd());
        after = System.currentTimeMillis();
        logtime.writeLog("Query 4: -> "+(after-before)+" ms");
        log.writeLog("Query4 -> " + l);
        view.printList(l);
    }

    private void query5(){
        try {
            long id = view.askLong("Insira o ID de um user:");
            before = System.currentTimeMillis();
            Pair<String, List<Long>> p = com.getUserInfo(id);
            after = System.currentTimeMillis();
            logtime.writeLog("Query 5: -> " + (after - before) + " ms");
            log.writeLog("Query5 -> " + p);
            view.printBio(p.getFst());
            view.printList(p.getSnd());
        }
        catch (UserDNEException e){
            view.printError("User não encontrado: ", e.getMessage());
        }
    }

    private void query6(){
        int n = view.askInt("Insira um número de respostas:");
        Pair<LocalDate, LocalDate> p = view.askTwoDates();
        before = System.currentTimeMillis();
        List <Long> l = com.mostVotedAnswers(n, p.getFst(), p.getSnd());
        after = System.currentTimeMillis();
        logtime.writeLog("Query 6: -> "+(after-before)+" ms");
        log.writeLog("Query6 -> " + l);
        view.printList(l);
    }

    private void query7(){
        int n = view.askInt("Insira o número de perguntas:");
        Pair<LocalDate, LocalDate> p = view.askTwoDates();
        before = System.currentTimeMillis();
        List <Long> l = com.mostAnsweredQuestions(n, p.getFst(), p.getSnd());
        after = System.currentTimeMillis();
        logtime.writeLog("Query 7: -> "+(after-before)+" ms");
        log.writeLog("Query7 -> " + l);
        view.printList(l);
    }

    private void query8(){
        String w = view.askString("Insira uma palavra para procurar:");
        int n = view.askInt("Insira um número:");
        before = System.currentTimeMillis();
        List <Long> l = com.containsWord(n, w);
        after = System.currentTimeMillis();
        logtime.writeLog("Query 8: -> "+(after-before)+" ms");
        log.writeLog("Query8 -> " + l);
        view.printList(l);
    }

    private void query9(){
        long u1 = view.askLong("Insira o ID do User 1:");
        long u2 = view.askLong("Insira o ID do User 2:");
        int n = view.askInt("Insira o número de posts:");
        before = System.currentTimeMillis();
        List<Long> l = com.bothParticipated(n, u1, u2);
        after = System.currentTimeMillis();
        logtime.writeLog("Query 9: -> "+(after-before)+" ms");
        log.writeLog("Query9 -> " + l);
        view.printList(l);
    }

    private void query10(){
        try {
            long id = view.askLong("Insira o ID de um Post:");
            before = System.currentTimeMillis();
            long ida = com.betterAnswer(id);
            after = System.currentTimeMillis();
            logtime.writeLog("Query 10: -> " + (after - before) + " ms");
            log.writeLog("Query11 -> " + ida);
            view.printValue(ida, "Best Answer: ");
        }
        catch (PostDNEException e){
            view.printError("Post não existe: ", e.getMessage());
        }
        catch (NotQuestionException e){
            view.printError("Post é uma Answer: ", e.getMessage());
        }
    }

    private void query11(){
        int num_tags = view.askInt("Insira o número de tags:");
        Pair<LocalDate, LocalDate> p = view.askTwoDates();
        before = System.currentTimeMillis();
        List<Long> l = com.mostUsedBestRep(num_tags, p.getFst(), p.getSnd());
        after = System.currentTimeMillis();
        logtime.writeLog("Query 11: -> "+(after-before)+" ms");
        log.writeLog("Query11 -> " + l);
        view.printList(l);
    }

    private void clean(TADCommunity com){
    before = System.currentTimeMillis();
    com.clear();
    after = System.currentTimeMillis();
    logtime.writeLog("CLEAN -> "+(after-before)+" ms");
    }

    private void exit() {
        view.exit();
        System.exit(1);
    }
}
