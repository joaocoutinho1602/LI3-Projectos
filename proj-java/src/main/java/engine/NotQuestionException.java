package engine;

/** Classe NotQuestionException
 * Implementa uma excepção para o id não é de uma Question
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
 */
public class NotQuestionException extends Exception {
    public NotQuestionException() {
        super();
    }

    public NotQuestionException(long id) {
        super(Long.toString(id));
    }
}
