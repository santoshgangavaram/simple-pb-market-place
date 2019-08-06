package demo.simplemarketplace.model;

public class HourlyBid extends Bid {

  private int hours;

  private int hourlyPrice;

  public int getHours() {
    return hours;
  }

  public void setHours(int hours) {
    this.hours = hours;
  }

  public int getHourlyPrice() {
    return hourlyPrice;
  }

  public void setHourlyPrice(int hourlyPrice) {
    this.hourlyPrice = hourlyPrice;
  }

  @Override
  public int compareTo(Bid o) {
    if (o instanceof HourlyBid) {
      return (this.hourlyPrice * hours) - ((HourlyBid) o).hourlyPrice * ((HourlyBid) o).hours;
    }
    return 0;
  }
}
