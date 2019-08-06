package demo.simplemarketplace.model;

import java.util.Objects;

public class Buyer {

  private int id;

  private String fullName;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName, id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Buyer other = (Buyer) obj;
    return Objects.equals(fullName, other.fullName) && id == other.id;
  }

  @Override
  public String toString() {
    return "Buyer [id=" + id + ", fullName=" + fullName + "]";
  }
}
