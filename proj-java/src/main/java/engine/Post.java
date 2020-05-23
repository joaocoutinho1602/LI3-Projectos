package engine;

import java.time.LocalDate;

/** Classe Abstracta Post
 *  Contém os dados comuns entre os posts do tipo Question e Answer, sendo portanto a superclasse
 *  destes duas subclasses.
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public abstract class Post {
    /** ID de um Post */
    private long id;
    /** Tipo de um Post */
    private int type;
    /** ID do autor */
    private long id_user;
    /** Score do Post */
    private int score;
    /** Data do Post */
    private LocalDate date;
    /** Reputação do poster**/
    private int rep;

    /** Construtos vazio de Post
     *
     */
    public Post(){
        this.id = -1;
        this.type = -1;
        this.id_user = -1;
        this.score = -1;
        this.rep = -1;
    }

    /** Construtor cópia de Post
     *
     * @param p Post de que vai ser feito cópia
     */
    public Post(Post p){
        this.id = p.getId();
        this.type = p.getType();
        this.id_user = p.getIdUser();
        this.score = p.getScore();
        this.rep = p.getRep();
    }

    /** Construtor parametrizado de Post
     *
     * @param id Id de um post
     * @param type Tipo de post (1 para Question, 2 para Answer)
     * @param id_user ID do utilizador dono do post
     * @param score Pontuação do post
     * @param date Data em que foi feito o post
     * @param rep Reputação do utilizador dono do post
     */
    public Post(long id, int type, long id_user, int score, LocalDate date, int rep){
        this.id = id;
        this.type = type;
        this.id_user = id_user;
        this.score = score;
        this.date = date;
        this.rep = rep;
    }

    /** Dá o valor do ID de um post
     *
     * @return long: ID do post
     */
    public long getId(){

        return this.id;
    }

    /** Dá o valor do tipo de um post
     *
     * @return int: Tipo de post
     */
    public int getType(){

        return this.type;
    }

    /** Dá o valor do ID do utilizador dono de um post
     *
     * @return long: ID do utilizador dono de um post
     */
    public long getIdUser(){

        return this.id_user;
    }

    /** Dá o valor da pontuação de um post
     *
     * @return int: Pontuação de um post
     */
    public int getScore(){

        return this.score;
    }

    /** Dá o valor da data em que foi feito um post
     *
     * @return LocalDate: Data em que foi feito um post
     */
    public LocalDate getDate(){

        return this.date;
    }

    /** Dá o valor da reputação do utilizador dono de um post
     *
     * @return  int: Reputação do utilizador dono de um post
     */
    public int getRep(){
        return this.rep;
    }

    /** Guarda o valor do ID de um post
     *
     * @param id ID de um post
     */
    public void setId(long id){

        this.id = id;
    }

    /** Guarda o valor do tipo de um post
     *
     * @param type Tipo de um post
     */
    public void setType(int type){

        this.type = type;
    }

    /** Guarda o valor do ID do utilizador dono de um post
     *
     * @param id_user: Valor do ID do utilizador dono de um post
     */
    public void setIdUser(long id_user){

        this.id_user = id_user;
    }

    /** Guarda o valor da pontuação de um post
     *
     * @param score: Pontuação de um post
     */
    public void setScore(int score){

        this.score = score;
    }

    /** Guarda o valor da data em que o post foi feito
     *
     * @param date: Data em que um post foi feito
     */
    public void setDate(LocalDate date){

        this.date = date;
    }

    /** Guarda a reputação do utilizador dono de um post
     *
     * @param rep: Reputação do utilizador dono de um post
     */
    public void setRep(int rep){

        this.rep = rep;
    }

    /** Verifica se um post está entre duas datas
     *
     * @param start: Data de início do intervalo de tempo a verificar
     * @param end: Data de fim do intervalo de tempo a verificar
     * @return boolean: true se o post está entre as duas datas dadas
     */
    public boolean inRange(LocalDate start, LocalDate end){
        if(this.date.isAfter(start) && this.date.isBefore(end)) return true;
        else if(this.date.isEqual(start) || this.date.isEqual(end)) return true;
        else return false;
    }

    /** Implementação do método equals de Post
     *
     */
    public boolean equals(Object obj){
	if(obj == this) return true;
	if(obj == null || obj.getClass() != this.getClass()) return false;
	else{
            Post o = (Post) obj;
            return o.getId() == this.getId();
	}
    }

    @Override

    /** Declaração do método abstrato clone de Post a ser implementado pelas suas subclasses
     *
     */
    public abstract Post clone();

    /** Declaração do método abstrato que dá o valor do parent de um post a ser implementado pelas suas subclasses
     *
     */
    public abstract long getIdParent();

    @Override

    /** Implementação do método toString de Post
     *
     */
    public String toString(){
	    StringBuilder sb = new StringBuilder();
	    sb.append("ID: ").append(this.id).append("\n");
	    sb.append("Type: ").append(this.type).append("\n");
	    sb.append("ID User: ").append(this.id_user).append("\n");
        sb.append("Score: ").append(this.score).append("\n");
        sb.append("Date: ").append(this.date ).append("\n");

	    return sb.toString();
    }

}
