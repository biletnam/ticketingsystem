# Ticketing System

Problem Statement:
Design a ticketing system that mainly helps a user perform these functions:
1. Find the number of seats available within the venue 
  Note: available seats are seats that are neither held nor reserved. 
2. Find and hold the best available seats on behalf of a customer 
  Note: each ticket hold should expire within a set number of seconds. 
3. Reserve and commit a specific group of held seats for a customer

## Pre-requisites
* Java 7+
* Maven 3.3+

## Installation Steps:
* Clone this repository to your local system.
```git clone ```
* Navigate to the folder
```cd ticketingsystem```
* Perform Maven clean and install
```mvn clean install```
* Run test cases through Maven
```mvn test```
* Start the application
```mvn exec:java -Dexec.mainClass="ticketing.App"```

## Application Walkthrough
* **The Home Menu:** This menu will be available after each operation.

![screen shot 2018-05-21 at 2 00 19 am](https://user-images.githubusercontent.com/21368799/40299425-0fe7b60c-5c9b-11e8-805d-d39d47386b3c.png)

* **SeatMap:** You can see the seat map by selecting option one everytime.

SeatMap legend = ['.' = Available, 'H' = Held, 'R' = Reserved]

![screen shot 2018-05-21 at 2 00 29 am](https://user-images.githubusercontent.com/21368799/40299430-14fdcc94-5c9b-11e8-98b0-343dc1b1a1fc.png)

* **Find and Hold Seats:** Enter number of seats required and email address.
Assuming it to be a movie theatre, the algorithm will smartly find the farthest and most centered seat possible.

![screen shot 2018-05-21 at 2 00 41 am](https://user-images.githubusercontent.com/21368799/40299438-1bb30e28-5c9b-11e8-8232-162c81447e6a.png)

* **SeatMap-Held Seats:** As stated earlier you can see the current seat map at any point.

![screen shot 2018-05-21 at 2 00 54 am](https://user-images.githubusercontent.com/21368799/40299440-1bcf430e-5c9b-11e8-8f43-b02dcaa64ba3.png)

* **Reserve Seats:** This option is available only when system identifies that some seats are held.

![screen shot 2018-05-21 at 2 01 04 am](https://user-images.githubusercontent.com/21368799/40299441-1bf0d672-5c9b-11e8-8e0c-6c5699075631.png)

* Seat Map-Reserved Seats

![screen shot 2018-05-21 at 2 01 15 am](https://user-images.githubusercontent.com/21368799/40299442-1c0da680-5c9b-11e8-9143-2595f81ad407.png)

* **Seat Map**: After booking 8, 32, 12, 16 seats successively

![screen shot 2018-05-21 at 2 13 12 am](https://user-images.githubusercontent.com/21368799/40299959-bbe221c6-5c9c-11e8-9801-a9518a17715d.png)

* **Check Reservation:** A user can check reservation by their email id used at time of booking.

![screen shot 2018-05-21 at 2 13 25 am](https://user-images.githubusercontent.com/21368799/40299960-bbfe3712-5c9c-11e8-8132-b1f88d32203a.png)

