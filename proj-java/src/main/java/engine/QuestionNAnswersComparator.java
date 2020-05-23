package engine;

import engine.Post;
import engine.Question;

import java.io.Serializable;
import java.util.Comparator;

/** Classe QuestionNAnswersComparator
 * Implementa um comparator do número de respostas a duas questões
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class QuestionNAnswersComparator implements Comparator<Post>, Serializable {
    public int compare(Post q1, Post q2) {
        if(q1 instanceof Question && q2 instanceof Question){
            if(((Question) q1).getAnswers() > ((Question) q2).getAnswers()) return -1;
            else if(((Question) q1).getAnswers() < ((Question)q2).getAnswers()) return 1;
            else{
                if(q1.getId() > q2.getId()) return -1;
                else if(q1.getId() < q2.getId()) return 1;
                else return 0;
            }
        }
        else return 0;
    }
}
