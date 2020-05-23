package li3;

import java.time.LocalDate;
import java.util.List;
import common.Pair;
import engine.NotQuestionException;
import engine.PostDNEException;
import engine.UserDNEException;

/** Interface TADCommunity
 *  É únicamente responsável por gerar outputs, que têm como destino o Utilizador.
 *  Contém a representação visual do menu das queries.
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/

public interface TADCommunity {

    public void init();

    public void load(String dumpPath);

    // Query 1
    public Pair<String,String> infoFromPost(long id) throws PostDNEException;

    // Query 2
    public List<Long> topMostActive(int N);

    // Query 3
    public Pair<Long,Long> totalPosts(LocalDate begin, LocalDate end);

    // Query 4
    public List<Long> questionsWithTag(String tag, LocalDate begin, LocalDate end);

    // Query 5
    public Pair<String, List<Long>> getUserInfo(long id) throws UserDNEException;

    // Query 6
    public List<Long> mostVotedAnswers(int N, LocalDate begin, LocalDate end);

    // Query 7
    public List<Long> mostAnsweredQuestions(int N, LocalDate begin, LocalDate end);

    // Query 8
    public List<Long> containsWord(int N, String word);

    // Query 9
    public List<Long> bothParticipated(int N, long id1, long id2);

    // Query 10
    public long betterAnswer(long id) throws PostDNEException, NotQuestionException;

    // Query 11
    public List<Long> mostUsedBestRep(int N, LocalDate begin, LocalDate end);

    public void clear();
}
