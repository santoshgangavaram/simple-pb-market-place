package demo.simplemarketplace.model;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@Component
public class Project {

  private int id;

  private String description;

  private String deadlineDateTime;

  private Seller seller;

  private State state;

  private FixedBid finalBid;

  @JsonIgnore
  public FixedBid getFinalBid() {
    return finalBid;
  }

  public void setFinalBid(FixedBid finalBid) {
    this.finalBid = finalBid;
  }

  public synchronized State getState() {
    return state;
  }

  @JsonIgnore
  public synchronized void setState(State state) {
    this.state = state;
  }

  public enum State {
    CREATED,
    BIDDING_COMPLETE
  }

  public Seller getSeller() {
    return seller;
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDeadlineDateTime() {
    return deadlineDateTime;
  }

  public void setDeadlineDateTime(String deadlineDateTime) {
    this.deadlineDateTime = deadlineDateTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(deadlineDateTime, description, finalBid, id, seller);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Project other = (Project) obj;
    return Objects.equals(deadlineDateTime, other.deadlineDateTime)
        && Objects.equals(description, other.description)
        && Objects.equals(finalBid, other.finalBid)
        && id == other.id
        && Objects.equals(seller, other.seller);
  }

  @Override
  public String toString() {
    return "Project [id="
        + id
        + ", description="
        + description
        + ", deadlineDateTime="
        + deadlineDateTime
        + ", seller="
        + seller
        + ", state="
        + state
        + ", finalBid="
        + finalBid
        + "]";
  }
}
