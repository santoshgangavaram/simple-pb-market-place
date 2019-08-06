package demo.simplemarketplace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import demo.simplemarketplace.controller.MarketPlaceController;
import demo.simplemarketplace.model.Buyer;
import demo.simplemarketplace.model.FixedBid;
import demo.simplemarketplace.model.Project;
import demo.simplemarketplace.model.Seller;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SimplemarketplaceApplicationTests {

  @Autowired private TestRestTemplate restTemplate;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  public void successfulBidding() throws InterruptedException {

    Project project = new Project();
    project.setDescription("Project Bidding software");

    ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);

    project.setDeadlineDateTime(
        MarketPlaceController.DATE_TIME_SECONDS.format(now.plusSeconds(20)));
    Seller seller = new Seller();
    seller.setFullName("Seller1");
    seller.setId(1);
    project.setSeller(seller);

    List<Buyer> buyers = restTemplate.getForObject("/marketplace/buyers", List.class);

    assertEquals(50000, buyers.size());

    // when
    List<HashMap> projects =
        restTemplate.postForObject("/marketplace/project", project, List.class);

    assertEquals(1, projects.size());

    project.setId((int) projects.get(0).get("id"));

    Random ran = new Random();

    List<Integer> list = new ArrayList<Integer>();

    IntStream.range(1, 51)
        .forEach(
            i -> {
              int rand = ran.nextInt(200);
              list.add(rand);
              restTemplate.postForObject(
                  "/marketplace/project/bid", build(rand, project.getId(), i), List.class);
            });

    List bids =
        restTemplate.getForObject(
            "/marketplace/project/bids?projectId=" + project.getId(), List.class);

    assertEquals(bids.toString(), 50, bids.size());

    TimeUnit.SECONDS.sleep(30);

    FixedBid projectBid =
        restTemplate.getForObject(
            "/marketplace/project/lowestbid?projectId=" + project.getId(), FixedBid.class);

    assertNotNull(projectBid);

    Collections.sort(list);

    assertEquals(list.get(0).intValue(), projectBid.getFixedPrice());
  }

  private FixedBid build(int price, int projectId, int buyerId) {
    FixedBid fixedBid = new FixedBid();
    fixedBid.setFixedPrice(price);
    fixedBid.setProjectId(projectId);
    Buyer buyer = new Buyer();
    buyer.setId(buyerId);
    fixedBid.setBuyer(buyer);
    return fixedBid;
  }
}
