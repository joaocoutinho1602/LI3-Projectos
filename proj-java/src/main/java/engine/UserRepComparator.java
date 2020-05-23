package engine;

import engine.User;

import java.io.Serializable;
import java.util.Comparator;

/** Classe UserRepComparator
 *  Implementa um comparator de reputação de dois utilizadores
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
 */
public class UserRepComparator implements Comparator<User>, Serializable {
    public int compare(User u1, User u2) {
        if(u1.getRep() > u2.getRep()) return -1;
        else if(u1.getRep() < u2.getRep()) return 1;
        else{
            if(u1.getID() > u2.getID()) return -1;
            else if(u1.getID() < u2.getID()) return 1;
            else return 0;
        }
    }

}
