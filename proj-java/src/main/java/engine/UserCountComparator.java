package engine;

import engine.User;

import java.io.Serializable;
import java.util.Comparator;

/** Classe UserCountComparator
 * Implementa um comparator do número de posts feitos por dois utilizadores
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class UserCountComparator implements Comparator<User>, Serializable {
    public int compare(User u1, User u2) {
        if(u1.getpCount() > u2.getpCount()) return -1;
        else if(u1.getpCount() < u2.getpCount()) return 1;
        else{
            if(u1.getID() > u2.getID()) return -1;
            else if(u1.getID() < u2.getID()) return 1;
            else return 0;
        }
    }

}
