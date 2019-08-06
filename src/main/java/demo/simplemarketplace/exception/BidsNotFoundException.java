package demo.simplemarketplace.exception;

public class BidsNotFoundException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 1L;

  public BidsNotFoundException(String message) {
    super(message);
  }
}
