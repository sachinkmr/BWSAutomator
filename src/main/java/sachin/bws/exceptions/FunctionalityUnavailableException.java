package sachin.bws.exceptions;

public class FunctionalityUnavailableException extends BWSException{
	private static final long serialVersionUID = 1L;

	public FunctionalityUnavailableException() {
    }

    public FunctionalityUnavailableException(String message) {
        super(message);
    }
}
