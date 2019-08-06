package demo.simplemarketplace.model;

public class Error {

  private String message;

  public String getMessage() {
    return message;
  }

  public Error setMessage(String message) {
    this.message = message;
    return this;
  }

  @Override
  public String toString() {
    return "Error [message=" + message + "]";
  }
}
