package demo.simplemarketplace.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class FixedBid extends Bid {

  private int fixedPrice;

  @Override
  public Type getType() {
    return Type.FIXED;
  }

  public int getFixedPrice() {
    return fixedPrice;
  }

  public void setFixedPrice(int fixedPrice) {
    this.fixedPrice = fixedPrice;
  }

  @Override
  public int compareTo(Bid o) {
    if (o instanceof FixedBid) {
      return this.fixedPrice - ((FixedBid) o).getFixedPrice();
    }
    return 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(fixedPrice);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    FixedBid other = (FixedBid) obj;
    return fixedPrice == other.fixedPrice;
  }

  @Override
  public String toString() {
    return "FixedBid [fixedPrice="
        + fixedPrice
        + ", getBuyer()="
        + getBuyer()
        + ", getId()="
        + getId()
        + ", getProjectId()="
        + getProjectId()
        + ", toString()="
        + super.toString()
        + ", getClass()="
        + getClass()
        + ", hashCode()="
        + hashCode()
        + "]";
  }
}
