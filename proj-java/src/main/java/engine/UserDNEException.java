package engine;

public class UserDNEException extends Exception {
    public UserDNEException() {
        super();
    }

    public UserDNEException(long id) {
        super(Long.toString(id));
    }
}
