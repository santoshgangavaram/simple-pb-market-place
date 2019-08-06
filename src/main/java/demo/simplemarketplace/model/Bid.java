package demo.simplemarketplace.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Bid implements Comparable<Bid> {

  private int id;

  private int projectId;

  private Type type;

  private Buyer buyer;

  public Buyer getBuyer() {
    return buyer;
  }

  public void setBuyer(Buyer buyer) {
    this.buyer = buyer;
  }

  public int getId() {
    return id;
  }

  @JsonIgnore
  public void setId(int id) {
    this.id = id;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  public Type getType() {
    return type;
  }

  @JsonIgnore
  public void setType(Type type) {
    this.type = type;
  }

  public enum Type {
    FIXED,
    HOURLY;
  }

  @Override
  public int hashCode() {
    return Objects.hash(buyer, id, projectId, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Bid other = (Bid) obj;
    return Objects.equals(buyer, other.buyer)
        && id == other.id
        && projectId == other.projectId
        && type == other.type;
  }

  @Override
  public String toString() {
    return "Bid [id="
        + id
        + ", projectId="
        + projectId
        + ", type="
        + type
        + ", buyer="
        + buyer
        + "]";
  }
}
