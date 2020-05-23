package engine;

import java.time.LocalDate;

/** Classe Answer - Subclass de Post
 *  Contém as variáveis de instância específicas ao tipo de Post: Answer
 *  Assim como os métodos default, construtores, gets e sets
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class Answer extends Post {
    /** ID do Post ao qual é resposta */
    private long id_parent;
    /** Número de comentários à resposta */
    private int cmts;

    /**
     * Construtor por omissão de Answer.
     */
    public Answer(){
        super();
        this.id_parent = 0;
        this.cmts = 0;
    }

    /**
     * Construtor cópia de Answer.
     * @param a Answer de que vai ser feita cópia
     */
    public Answer(Answer a){
        super(a);
        this.id_parent = a.getIdParent();
        this.cmts = a.getComments();
    }

    /**
     * Construtor parametrizado de Answer.
     * @param id ID da Answer
     * @param type Tipo de Post, como é uma Answer será sempre 2
     * @param id_user ID do utilizador dono da Answer
     * @param score Pontuação da Answer
     * @param date Data da Answer
     * @param rep Reputação do utilizador dono da Answer
     * @param id_parent ID da Question a que a Answer responde
     * @param cmts Número de comentários feitos à Answer
     */
    public Answer(long id, int type, long id_user, int score, LocalDate date, int rep, long id_parent, int cmts){
        super(id, type, id_user, score, date, rep);
        this.id_parent = id_parent;
        this.cmts = cmts;
    }

    /**
     * Retorna o valor do ID do parent de uma Answer
     * @return long: ID do post parent da Answer
     */
    public long getIdParent(){
        return this.id_parent;
    }

    /**
     * Guarda o valor do ID do parent de uma Answer
     */
    public void setIdParent(long id_parent){
        this.id_parent = id_parent;
    }

    /**
     * Retorna o número de comentários a uma Answer
     * @return int: Número de comentários feitos a uma Answer
     */
    public int getComments() {
        return this.cmts;
    }

    /**
     * Guarda o número de comentários a uma Answer
     */
    public void setComments(int cmts) {
        this.cmts = cmts;
    }

    /**
     * Implementação do método toString de Answer
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("ID_Parent: " + this.id_parent + "\n\n");

        return sb.toString();
    }

    /**
     * Implementação do método clone de Answer
     */
    public Answer clone() {
        return new Answer(this);
    }

}
