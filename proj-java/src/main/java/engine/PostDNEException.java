package engine;

/** Classe PostDNEException
 * Implementa uma excepção para quando um dado post não existe
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/
public class PostDNEException extends Exception{
    public PostDNEException() {
        super();
    }

    public PostDNEException(long id) {
        super(Long.toString(id));
    }
}
