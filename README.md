<h1 align="center">Simple Project-Bidding Market Place</h1>

## Project scope and implementation details

1. Solution implemented for FixedBid type. it can be enhanced to HourlyBid type. 
2. Buyers(50,000) registered as part of the system start up.
3. Error scenarios handled for below use cases
```	
	a. Project not found
	b. System Available for scheduling
	c. Bids not found
	d. Buyer not found
```
4. Unit test case implemented
5. Following APIs implemented
```
	a. add project - This is used to add project by a Seller	   
	b. add project-bid -- This is used to add bid to a Seller's project. If project reached deadline, this API returns project info  with lowest bid buyer info
	c. get projects - This is used to get the list of projects (last 100 or from beginning to end)
	d. get project-bids -- This is used to get the list of project bids
	e. get buyers -- This is used to get the list of buyers
	f. get lowestbid -- This can be used to get the lowest bid for a given project
```
## Instructions to build, run and test

1. To build the package and run tests
``` mvn package ```
2. To run tests alone
``` mvn test ```
3. To run the app and view swagger-ui as rest client for API testing
``` mvn spring-boot:run ```
http://localhost:8080/swagger-ui.html

## cURL commands to test the APIs

### Add project
```
curl -X POST \
  http://localhost:8080/marketplace/project \
  -H 'content-type: application/json' \
  -d '{
  "deadlineDateTime": "2019-08-07 07:15:00Z",
  "description": "project bidding software1",
  "seller": {
    "fullName": "seller1",
    "id": 1
  }
}'
```
### Add project-bid
```
curl -X POST \
  http://localhost:8080/marketplace/project/bid \
  -H 'content-type: application/json' \
  -d '{
  "buyer": {
    "id": 1
  },
  "fixedPrice": 10,
  "projectId": 1
}'
```
### Get projects
```
curl -X GET \
  http://localhost:8080/marketplace/projects \
  -H 'content-type: application/json'
```
### Get project-bids
```
curl -X GET \
  'http://localhost:8080/marketplace/project/bids?projectId=1' \
  -H 'content-type: application/json'  
```
### Get Buyers
```
curl -X GET \
  http://localhost:8080/marketplace/buyers \
  -H 'content-type: application/json'  
```
### Get lowest bid
```
curl -X GET \
  'http://localhost:8080/marketplace/project/lowestbid?projectId=1' \
  -H 'content-type: application/json'
```
