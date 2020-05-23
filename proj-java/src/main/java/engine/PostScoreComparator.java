package engine;

import engine.Post;

import java.io.Serializable;
import java.util.Comparator;

/** Classe PostScoreComparator
 * Implementa um comparator da pontuação de dois posts
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class PostScoreComparator implements Comparator<Post>, Serializable {
    public int compare(Post p1, Post p2) {
        if(p1.getScore() > p2.getScore()) return -1;
        else if(p1.getScore() < p2.getScore()) return 1;
        else{
            if(p1.getId() > p2.getId()) return -1;
            else if(p1.getId() < p2.getId()) return 1;
            else return 0;
        }
    }

}
