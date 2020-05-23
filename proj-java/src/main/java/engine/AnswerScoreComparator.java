package engine;

import java.util.Comparator;

/** Classe AnswerScoreComparator
 * Implementa um comparator da pontuação de duas respostas
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class AnswerScoreComparator implements Comparator<Post> {
    public int compare(Post q1, Post q2) {
        double score1, score2;
        if(q1 instanceof Answer && q2 instanceof Answer){
            score1 = q1.getScore() * 0.45 + q1.getScore() * 0.2 + ((Answer) q1).getComments() * 0.1 + q1.getRep()*0.25;
            score2 = q2.getScore() * 0.45 + q2.getScore() * 0.2 + ((Answer) q2).getComments() * 0.1 + q2.getRep()*0.25;

            if(score1 > score2) return 1;
            if(score1 < score2) return -1;
            else{
                if(q1.getId() > q2.getId()) return 1;
                if(q1.getId() < q2.getId()) return -1;
            }
        }
        return 0;
    }
}
