package engine;

import java.util.ArrayList;
import java.util.List;

/** Classe User
 * Contém os dados relativos aos utilizadores do StackOverflow
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
 */
public class User {
    /** Nome de um utilizador */
    private String name;
    /** Bio de um utilizador */
    private String bio;
    /** ID de um utilizador*/
    private long id;
    /** Reputação de um utilizador */
    private int reputation;
    /** Número de posts feitos por um utilizador */
    private int postcount;
    /** Lista dos posts feitos por um utilizador */
    private List<Post> posts;

    /** Construtor vazio de User
     *
     */
    public User(){
        this.name = "";
        this.bio = "";
        this.id = 0;
        this.reputation = 0;
        this.postcount = 0;
        this.posts = new ArrayList<Post>();
    }

    /** Construtor cópia de User
     *
     * @param u Utilizador a ser copiado
     */
    public User(User u){
        this.name = u.getName();
        this.bio = u.getBio();
        this.id = u.getID();
        this.reputation = u.getRep();
        this.postcount = u.getpCount();
        this.posts = u.getPosts();
    }

    /** Construtor parametrizado de User
     *
     * @param name Nome do utilizador
     * @param bio Bio do utilizador
     * @param id ID do utilizador
     * @param reputation Reputação do utilizador
     * @param postcount Número de posts feitos pelo utilizador
     * @param posts Lista de posts feitos pelo utilizador
     */
    public User(String name, String bio, long id, int reputation, int postcount, ArrayList<Post> posts){
        this.name = name;
        this.bio = bio;
        this.id = id;
        this.reputation = reputation;
        this.postcount = postcount;
        this.posts = new ArrayList<Post>();
        setPosts(posts);
    }
    /** Adiciona um post à lista de posts de um utilizador
     *
     * @param p Post a ser adicionado à lista de posts de um utilizador
     */
    public void addPost(Post p){
        this.posts.add(p);
    }
    /** Incrementa o número de posts feitos por um utilizador
     *
     */
    public void incrementsCount(){
        this.postcount += 1;
    }

    /** Dá o nome de um utilizador
     *
     * @return String: Nome de um utilizador
     */
    public String getName(){
        return this.name;
    }

    /** Dá a bio de um utilizador
     *
     * @return String: Bio de um utilizador
     */
    public String getBio(){
        return this.bio;
    }

    /** Dá o valor do ID de um utilizador
     *
     * @return long: ID de um utilizador
     */
    public long getID(){
        return this.id;
    }

    /** Dá o valor da reputação de um utilizador
     *
     * @return int: Valor da reputação de um utilizador
     */
    public int getRep(){
        return this.reputation;
    }

    /** Dá o valor do número de posts de um utilizador
     *
     * @return int: Valor do número de posts de um utilizador
     */
    public int getpCount(){
        return this.postcount;
    }

    /** Dá a lista de posts feitos por um utilizador
     *
     * @return List<Post>: Lista de posts feitos por um utilizador
     */
    public List<Post> getPosts(){
        List<Post> posts = new ArrayList<>();
        for(Post p : this.posts){
            posts.add(p);
        }
        return posts;
    }

    /** Guarda o nome de um utilizador
     *
     * @param name Nome de um utilizador
     */
    public void setName(String name){
        this.name = name;
    }

    /** Guarda a bio de um utilizador
     *
     * @param bio Bio de um utilizador
     */
    public void setBio(String bio){
        this.bio = bio;
    }

    /** Guarda o valor do ID de um utilizador
     *
     * @param id ID de um utilizador
     */
    public void setID(long id){
        this.id = id;
    }

    /** Guarda o valor da reputação de um utilizador
     *
     * @param reputation Valor da reputação de um utilizador
     */
    public void setRep(int reputation){
        this.reputation = reputation;
    }

    /** Guarda o valor do número de posts de um utilizador
     *
     * @param postcount Número de posts de um utilizador
     */
    public void setPCount(int postcount){
        this.postcount = postcount;
    }

    /** Guarda a lista de posts de um utilizador
     *
     * @param posts Lista de posts de um utilizador
     */
    public void setPosts(ArrayList<Post> posts){
        for(Post post : posts){
            this.posts.add(post);
        }
    }

    /** Implementação do método equals de User
     *
     */
    public boolean equals(Object obj){
		if(obj == this) return true;
		if(obj == null || obj.getClass() != this.getClass()) return false;
		else{
			User u = (User) obj;
			return u.getID() == this.getID();
		}
	}

  /** Implementação do método clone de User
   *
   */
    public User clone(){
		return new User(this);
	}

  /** Implementação do método toString de User
   *
   */
    public String toString(){
	StringBuilder sb = new StringBuilder();
        sb.append("<------- USER ------->\n");
	sb.append("Name: ").append(this.name).append("\n");
	if(this.bio != null) sb.append("Bio: " + this.bio + "\n");
	sb.append("ID: ").append(this.id).append("\n");
	sb.append("Reputation: ").append(this.reputation).append("\n");
	sb.append("Post Count: ").append(this.postcount).append("\n");

        /*
        sb.append("\nPost List:\n");
        for(Post p : this.posts){
            sb.append("\n");
            sb.append(p.toString());
        }
        */
        sb.append("<---------------->\n\n");
	return sb.toString();
    }

}
