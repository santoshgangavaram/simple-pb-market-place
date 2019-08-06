package demo.simplemarketplace.controller;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.simplemarketplace.model.Buyer;
import demo.simplemarketplace.model.Error;
import demo.simplemarketplace.model.FixedBid;
import demo.simplemarketplace.model.Project;
import demo.simplemarketplace.model.Project.State;

@RestController
@RequestMapping(value = "/marketplace")
public class MarketPlaceController {

  List<Project> projects = new ArrayList<>();

  static List<Buyer> buyers = new ArrayList<>();

  List<FixedBid> bids = new ArrayList<>();

  Map<Integer, List<FixedBid>> projectBids = new HashMap<>();

  ScheduledExecutorService executorService = Executors.newScheduledThreadPool(100);

  public static final DateTimeFormatter DATE_TIME_SECONDS;

  static {
    // register 50,000 buyers
    IntStream.range(1, 50001)
        .forEach(
            orderedInt -> {
              Buyer buyer = new Buyer();
              buyer.setId(orderedInt);
              buyer.setFullName("testbuyer" + orderedInt);
              buyers.add(buyer);
            });

    // prepare datetimeformatter to work on yyyy-MM-dd hh:mm:ssZ format
    DateTimeFormatterBuilder builder =
        new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE);
    builder.appendLiteral(' ');
    builder
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2);
    builder.appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).appendOffsetId();
    DATE_TIME_SECONDS = builder.toFormatter();
  }

  @PostMapping(value = "/project", produces = "application/json")
  public ResponseEntity<?> postProject(@RequestBody Project project) {

    long delay =
        Duration.between(
                ZonedDateTime.now(),
                ZonedDateTime.parse(project.getDeadlineDateTime(), DATE_TIME_SECONDS))
            .toMillis();

    // schedule job to run complete the bidding and find the lowest bid
    try {
      executorService.schedule(
          () -> {
            project.setState(State.BIDDING_COMPLETE);
            Collections.sort(projectBids.get(project.getId()));
            project.setFinalBid(projectBids.get(project.getId()).get(0));
          },
          delay,
          TimeUnit.MILLISECONDS);
    } catch (RejectedExecutionException exp) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
          .body(new Error().setMessage("System unavailable now. Please try after sometime"));
    }
    project.setState(State.CREATED);
    project.setId(projects.size());
    projects.add(project);

    return ResponseEntity.ok(projects);
  }

  @PostMapping(value = "/project/bid", produces = "application/json")
  public ResponseEntity<?> bidProject(@RequestBody FixedBid bid) {

    // perform validations
    if (!(bid.getBuyer().getId() >= 1 && bid.getBuyer().getId() <= 50000)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new Error().setMessage("Buyer not found"));
    }
    if (!(bid.getProjectId() >= 1 && bid.getProjectId() <= projects.size())) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new Error().setMessage("Project not found"));
    }

    // check if bidding is already complete and respond the same to the client
    Project project = findProject(bid.getProjectId());
    if (project != null && project.getState().equals(State.BIDDING_COMPLETE)) {
      project
          .getFinalBid()
          .getBuyer()
          .setFullName(findBuyer(project.getFinalBid().getBuyer().getId()).getFullName());
      return ResponseEntity.ok(project);
    }

    // add to projectBids map
    bid.getBuyer().setFullName(findBuyer(bid.getBuyer().getId()).getFullName());
    if (projectBids.containsKey(bid.getProjectId())) {
      bid.setId(projectBids.get(bid.getProjectId()).size());
      projectBids.get(bid.getProjectId()).add(bid);
    } else {
      List<FixedBid> fixedBids = new ArrayList<>();
      bid.setId(1);
      fixedBids.add(bid);
      projectBids.put(bid.getProjectId(), fixedBids);
    }

    return ResponseEntity.ok(projectBids.get(bid.getProjectId()));
  }

  @GetMapping(value = "/project/bids", produces = "application/json")
  public ResponseEntity<?> getBids(@RequestParam int projectId) {

    // perform validations
    if (!(projectId >= 1 && projectId <= projects.size())) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new Error().setMessage("Project not found"));
    }
    if (!projectBids.containsKey(projectId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new Error().setMessage("Bids not found"));
    }

    return ResponseEntity.ok(projectBids.get(projectId));
  }

  @GetMapping(value = "/project/lowestbid", produces = "application/json")
  public ResponseEntity<?> getLowestBid(@RequestParam int projectId) {
    Project project = findProject(projectId);

    // perform validations
    if (project == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new Error().setMessage("Project not found"));
    }
    if (project.getFinalBid() == null) {
      return ResponseEntity.status(HttpStatus.PROCESSING)
          .body(new Error().setMessage("Bidding in progress"));
    }

    return ResponseEntity.ok(project.getFinalBid());
  }

  @GetMapping(value = "/buyers", produces = "application/json")
  public List<Buyer> getBuyers() {
    return buyers;
  }

  @GetMapping("/projects")
  public List<Project> getProjects(
      @RequestParam(required = false) Integer beginning,
      @RequestParam(required = false) Integer end) {

    // sublist of the list if more than 100
    if (!projects.isEmpty() && projects.size() > 100) {
      return projects.subList(beginning, end);
    }

    return projects;
  }

  Project findProject(int projectId) {
    return projects
        .stream()
        .filter(project1 -> project1.getId() == projectId)
        .findFirst()
        .orElse(null);
  }

  Buyer findBuyer(int buyerId) {
    return buyers.stream().filter(buyer -> buyer.getId() == buyerId).findFirst().orElse(null);
  }
}
