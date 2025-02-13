## Environment
- Java version: 17
- Maven version: 3.*
- Spring Boot version: 3.2.3

## Data
Example of a Event data JSON object:
```json
{
    "id":1,      
    "name": "Soft Summit",
    "location": "Xton City",
    "duration": 8,
    "cost": 30.0
}
```

## Requirements
Here you are provided a events database where you have to implement `/event` REST endpoint for following 3 operations.

`GET` request to `/event/byId/{id}`:
* return the event entry with given id and status code 200
* if the requested event entry doesn't exist, then status code 404 should be returned

`GET` request to `/event/top3?by={by}`:
* return the top 3 event entries sorted by given field and status code 200.
* for example: `/event/top3?by=cost` gives top 3 entries by cost
* if give `by` is invalid attribute(other than cost & duration), return status code 400

`GET` request to `/event/total?by={by}`:
* return the total value summed by given field and status code 200
* for example: `/event/total?by=duration` gives total duration of all events
* if give `by` is invalid attribute(other than cost & duration), return status code 400
 
`GET` request to `/scan/report/eventDashboard`:
* it needs to generate report like below JSON where it needs to group by `location` and find the total events per location and cost duration ratio per location.
* status code should be 200 and precision needs to be till 3 decimal places.

 ```json
[
{
    "location":"antarctica",
    "totalEvents": 3,
    "costDurationRatio": 20.987
},
{
    "location":"antarctica",
    "totalEvents": 6,
    "costDurationRatio": 50.7
}
]
```

* exclude the entries from calculation which have `location` or `event name` null or empty.
* impactFactor formula is:
```
costDurationRatio(location) = sum(cost) of a location/sum(duration) of a location
```

## Commands
- run: 
```bash
mvn clean spring-boot:run
```
- install: 
```bash
mvn clean install
```
- test: 
```bash
mvn clean test
```
