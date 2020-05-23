package engine;

import common.Pair;
import li3.TADCommunity;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;



/** Classe Community que guarda a base de dados(Model na abordagem MVC)
 *  Contém as estruturas que armazenam os diversos tipos de dados(Post,User,Tags), e inicialização das mesmas
 *  Estão definidas aqui as diferentes interrogações
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class Community implements TADCommunity {
    /** Map de Posts: usa como key o ID */
    private Map<Long, Post> posts;
    /** Map de Users: usa como key o ID */
    private Map<Long, User> users;
    /** Map de Tags: usa como key a string da Tag */
    private Map<String, Long> tags;
    /** Map de Listas de Posts: usa como key a data do post */
    private Map<LocalDate, ArrayList<Post>> dates;

    /**
     * Inicializa as estruturas da classe
     */
    public void init() {
        this.posts = new HashMap<>();
        this.users = new HashMap<>();
        this.tags = new HashMap<>();
        this.dates = new TreeMap<>();
    }

    /** Carrega a informação dos ficheiros .xml para as respetivas estruturas
     * @param dumpPath Caminho dos ficheiros a serem carregados
     */
    public void load(String dumpPath) {

        Parser p = new Parser();

        this.users = p.loadUsers(dumpPath);
        this.posts = p.loadPosts(dumpPath);
        this.tags = p.loadTags(dumpPath);
        this.dates = p.getMapDates();
    }

    /** Query 1: Info acerca de um Post
     *
     * @param id Id de um Post
     * @return  Par: Título do Post, Autor do Post
     * @throws PostDNEException Excepção quando o Post não existe
     */
    public Pair<String,String> infoFromPost(long id) throws PostDNEException {

        String title = "";

        if(!this.posts.containsKey(id)) throw new PostDNEException(id);

        Post p = this.posts.get(id);
        long pid = p.getIdParent();
        User u = this.users.get(p.getIdUser());

        String user_name = u.getName();

        if(p instanceof Question) title = ((Question) p).getTitle();
        if(p instanceof Answer) {
            if (!this.posts.containsKey(pid)) throw new PostDNEException(pid);
            p = this.posts.get(pid);
            title = ((Question) p).getTitle();
        }

        return new Pair<>(title, user_name);
    }

    /** Query 2: Top Users Mais Activos
     *
     * @param N Número de Users
     * @return Lista de Id's
     */
    public List<Long> topMostActive(int N) {

        List<Long> top = this.users.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(new UserCountComparator()))
                .limit(N)
                .map(x -> x.getValue().getID())
                .collect(Collectors.toList());

        return top;
    }

    /** Query 3: Total de Posts num Intervalo de Datas
     *
     * @param begin Data de Início
     * @param end Data do Fim
     * @return Par: Número de Answers, Número de Questions
     */
    public Pair<Long,Long> totalPosts(LocalDate begin, LocalDate end) {

        long answers, questions;

        answers = this.dates.entrySet().stream()
                .filter(x -> x.getKey().isEqual(begin) || x.getKey().isEqual(end) || (x.getKey().isAfter(begin) && x.getKey().isBefore(end)))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .filter(q -> q instanceof Answer)
                .count();

        questions = this.dates.entrySet().stream()
                .filter(x -> x.getKey().isEqual(begin) || x.getKey().isEqual(end) || (x.getKey().isAfter(begin) && x.getKey().isBefore(end)))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .filter(q -> q instanceof Question)
                .count();

        return new Pair<>(questions,answers);
    }

    /** Query 4: Questões num intervalo de tempo que contenham uma dada tag
     *
     * @param tag Tag a procurar
     * @param begin Data de Início
     * @param end Data do Fim
     * @return Lista com os id's das perguntas
     */
    public List<Long> questionsWithTag(String tag, LocalDate begin, LocalDate end) {

        List<Long> list =  this.dates.entrySet().stream()
                .filter(x -> x.getKey().isEqual(begin) || x.getKey().isEqual(end) || (x.getKey().isAfter(begin) && x.getKey().isBefore(end)))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .filter(q -> q instanceof Question)
                .filter(x -> ((Question) x).containsTag(tag))
                .map(x -> x.getId())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        return list;
    }

    /** Query 5: Obter Informação de um utilizador
     *
     * @param id Id do Utilizador
     * @return Par: Biografia, Lista dos últimos 10 posts
     * @throws UserDNEException Excepção quando um User não existe
     */
    public Pair<String, List<Long>> getUserInfo(long id) throws UserDNEException {

        if(!this.users.containsKey(id)) throw new UserDNEException(id);

        User u = this.users.get(id);

        String shortBio = u.getBio();

        List<Long> ids = u.getPosts().stream()
                .sorted(Comparator.comparing(Post::getDate).thenComparing(Post::getId).reversed())
                .limit(10)
                .map(x -> x.getId())
                .collect(Collectors.toList());

        return new Pair<>(shortBio,ids);
    }

    /** Query 6: Respostas mais votadas
     *
     * @param N Número de respostas
     * @param begin Data Inicial
     * @param end Data Final
     * @return Lista das N respostas mais votadas
     */
    public List<Long> mostVotedAnswers(int N, LocalDate begin, LocalDate end) {

        List<Long> list = this.posts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(new PostScoreComparator()))
                .map(Map.Entry::getValue)
                .filter(p -> p.inRange(begin, end))
                .filter(q -> q instanceof Answer)
                .limit(N)
                .map(x -> x.getId())
                .collect(Collectors.toList());

        return list;
    }

    /** Query 7: Questões com mais respostas
     *
     * @param N Número de questões
     * @param begin Data Inicial
     * @param end Data Final
     * @return Lista de Id's das Questões
     */
    public List<Long> mostAnsweredQuestions(int N, LocalDate begin, LocalDate end) {

        List<Long> list = this.posts.entrySet().stream()
                .filter(x -> x.getValue() instanceof Question)
                .sorted(Map.Entry.comparingByValue(new QuestionNAnswersComparator()))
                .map(Map.Entry::getValue)
                .filter(p -> p.inRange(begin, end))
                .limit(N)
                .map(x -> x.getId())
                .collect(Collectors.toList());

        return list;
    }

    /** Query 8: Questões que contenham uma dada palavra no título
     *
     * @param N Número de questões a retornar
     * @param word Palavra a procurar
     * @return Lista com os ID's das perguntas
     */
    public List<Long> containsWord(int N, String word) {

        List<Long> list = this.posts.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(x -> x instanceof Question)
                .filter(x -> ((Question) x).getTitle().contains(word))
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .limit(N)
                .map(x -> x.getId())
                .collect(Collectors.toList());

        return list;
    }

    /** Query 9: Posts em que dois users ambos participaram
     *
     * @param N Número de posts
     * @param id1 Id do User 1
     * @param id2 Id do User 2
     * @return Lista dos ID's das perguntas que ambos participaram
     */
    public List<Long> bothParticipated(int N, long id1, long id2) {

        List<Long> u1 = this.users.entrySet().stream()
                .filter(x -> x.getValue().getID() == id1)
                .map(Map.Entry::getValue)
                .map(x -> x.getPosts())
                .flatMap(List::stream)
                .map(Post::getIdParent)
                .collect(Collectors.toList());

        List<Long> u2 = this.users.entrySet().stream()
                .filter(x -> x.getValue().getID() == id2)
                .map(Map.Entry::getValue)
                .map(x -> x.getPosts())
                .flatMap(List::stream)
                .map(Post::getIdParent)
                .collect(Collectors.toList());

        u1.retainAll(u2);

        List<Long> result = u1.stream()
                .distinct()
                .map(x -> this.posts.get(x))
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .map(Post::getId)
                .limit(N)
                .collect(Collectors.toList());

        return result;
    }

    /** Query 10: Melhor resposta de uma dada pergunta
     *
     * @param id ID da pergunta
     * @return ID da melhor resposta
     * @throws PostDNEException Excepção Post Não Existe
     */
    public long betterAnswer(long id) throws PostDNEException, NotQuestionException {

        if(!this.posts.containsKey(id)) throw new PostDNEException(id);

        if(this.posts.get(id) instanceof Answer) throw new NotQuestionException(id);

        Long better_answer = this.posts.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(x -> x instanceof Answer)
                .filter(x -> ((Answer) x).getIdParent() == id)
                .sorted(new AnswerScoreComparator().reversed())
                .limit(1)
                .map(Post::getId)
                .findAny()
                .get();


        return better_answer;
    }

    /** Query 11: Tags mais usadas pelos Users com melhor reputação
     *
     * @param N Número de Users com mais reputação, e também número de tags a retornar
     * @param begin Data Inicial
     * @param end Data Final
     * @return Lista dos Identificadores das Tags mais usadas
     */
    public List<Long> mostUsedBestRep(int N, LocalDate begin, LocalDate end) {

        List<String> top = this.users.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(new UserRepComparator()))
                .limit(N)
                .map(Map.Entry::getValue)
                .map(x -> x.getPosts())
                .flatMap(List::stream)
                .filter(d -> d.inRange(begin, end))
                .filter(q -> q instanceof Question)
                .map(x -> ((Question) x).getTags())
                .flatMap(List::stream)
                .collect(Collectors.toList());


        Map<String, Long> count = top.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Long> result = count.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(N)
                .map(Map.Entry::getKey)
                .map(x -> this.tags.get(x))
                .collect(Collectors.toList());

        return result;
    }

    public void clear(){
        this.users.clear();
        this.posts.clear();
        this.tags.clear();
        this.dates.clear();
    }
}
