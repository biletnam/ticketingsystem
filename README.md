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
* The Home Menu
