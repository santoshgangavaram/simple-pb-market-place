package demo.simplemarketplace.exception;

public class BuyerNotFoundException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 1L;

  public BuyerNotFoundException(String message) {
    super(message);
  }
}
