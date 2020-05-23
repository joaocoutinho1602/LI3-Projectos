package li3;

import common.MyLog;
import engine.Community;
import engine.NotQuestionException;
import engine.PostDNEException;
import engine.UserDNEException;

/** Classe Main
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/

public class Main {


    public static void main(String[] args) throws PostDNEException, UserDNEException, NotQuestionException {


        TADCommunity com = new Community();
        View v = new View();
        Controller c = new Controller();

        c.setModel(com);
        c.setView(v);
        c.init(args[0]);

    }

}
