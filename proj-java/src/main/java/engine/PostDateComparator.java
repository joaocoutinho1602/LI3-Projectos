package engine;

import engine.Post;

import java.io.Serializable;
import java.util.Comparator;

/** Classe PostDateComparator
 * Implementa um comparator da data de dois posts
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class PostDateComparator implements Comparator<Post>, Serializable {
    public int compare(Post p1, Post p2) {
        if(p1.getDate().isAfter(p2.getDate())) return -1;
        else if(p1.getDate().isBefore(p2.getDate())) return 1;
        else{
            if(p1.getId() > p2.getId()) return -1;
            else if(p1.getId() < p2.getId()) return 1;
            else return 0;
        }
    }
}
