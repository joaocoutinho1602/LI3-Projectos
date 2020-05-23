package engine;

import java.time.LocalDate;
import java.util.ArrayList;

/** Classe Question - Subclass de Post
 *  Contém as variáveis de instância específicas ao tipo de Post: Question
 *  Assim como os métodos default: construtores, gets e sets
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class Question extends Post {
    /** Título do Post */
    private String title;
    /** Número de Answers */
    private int answers;
    /** Lista de tags do Post */
    private ArrayList<String> tags;

    /** Construtor por omissão*/
    public Question(){
        super();
        this.title = "";
        this.answers = 0;
        this.tags = new ArrayList<>();
    }
    /** Construtor de cópia
    * @param q Uma question
    */
    public Question(Question q){
        super(q);
        this.title = q.getTitle();
        this.answers = q.getAnswers();
        this.tags = q.getTags();
    }
    /** Construtor Parametrizado
    * @param id ID do Post
    * @param type Tipo de Post
    * @param id_user ID do autor
    * @param score Score do Post
    * @param date Data do Post
    * @param title Título do Post
    * @param answers Número de respostas
    * @param tags Lista de Tags
    */
    public Question(long id, int type, long id_user, int score, LocalDate date, int rep, String title, int answers, ArrayList<String> tags){
        super(id, type, id_user, score, date, rep);
        this.title = title;
        this.answers = answers;
        this.tags = new ArrayList<>();
        setTags(tags);
    }

    /** Dá o valor do título de uma Question
     *
     * @return String: Título de uma Question
     */
    public String getTitle(){

        return this.title;
    }

    /** Dá o valor do número de Answers a uma Question
     *
     * @return int: Valor do número de Answers a uma Question
     */
    public int getAnswers(){

        return this.answers;
    }

    /** Dá uma lista com as tags de uma Question
     *
     * @return ArrayList<String>: Lista das tags de uma Question
     */
    public ArrayList<String> getTags(){
        ArrayList<String> tags = new ArrayList<>();
        for(String s : this.tags){
            tags.add(s);
        }

        return tags;
    }

    /** Dá o valor do ID de uma Question. O nome foi escolhido de forma a ser um método comum a todas as subclasses de Post.
     *
     * @return long: ID de uma Question
     */
    public long getIdParent(){
        return super.getId();
    }

    /** Guarda o valor do título de uma Question
     *
     * @param title Título de uma Question
     */
    public void setTitle(String title){

        this.title = title;
    }

    /** Guarda o valor do número de Answers a uma Question
     *
     * @param answers Valor do número de Answers a uma Question
     */
    public void setAnswers(int answers){

        this.answers = answers;
    }

    /** Guarda a lista de tags de uma Question
     *
     * @param ArrayList<String> Lista de tags a ser guardada
     */
    public void setTags(ArrayList<String> tags){
        for(String s : tags){
            this.tags.add(s);
        }
    }

    /** Verifica se uma Question contém uma dada tag
    * @param tag Tag cuja existência numa Question vai ser verificada
    * @return true: A Question contém a tag
    */
    public boolean containsTag(String tag){
        boolean contains = this.tags.stream()
                .anyMatch(t -> t.equals(tag));

        return contains;
    }

    /** Implementação do método toString de Question
     *
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Title: " + this.title + "\n");
        sb.append(super.toString());
        sb.append("Answer Count: " + this.answers + "\n");
        sb.append("Tags: ");
        for(String s : this.tags){
            sb.append(s + "; ");
        }
        sb.append("\n\n");

        return sb.toString();
    }

    /** Implementação do método clone de Question
     *
     */
    public Question clone(){

        return new Question(this);
    }

}
