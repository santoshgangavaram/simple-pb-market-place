<h1 align="center">Simple Project-Bidding Market Place</h1>

## Assumptions

1. Solution implemented for FixedBid type. it can be enhanced to HourlyBid type. 
2. Following APIs implemented
```
	a. add project - This is used to add project by a Seller	   
	b. add project-bid -- This is used to add bid to a Seller's project. If project reached deadline, this API returns project info  with lowest bid buyer info
	c. get projects - This is used to get the list of projects (last 100 or from beginning to end)
	d. get project-bids -- This is used to get the list of project bids
	e. get buyers -- This is used to get the list of buyers
```
## Instructions to build and run the tests

1. To build the package and run tests
``` mvn package ```
2. To run tests alone
``` mvn test ```
3. To run the app and view swagger-ui as rest client for API testing
``` mvn spring-boot:run ``
http://localhost:8080/swagger-ui.html
