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
* Clone this repository to your local system.<br>
```git clone https://github.com/ishandikshit/ticketingsystem.git```
* Navigate to the folder<br>
```cd ticketingsystem```
* Perform Maven clean and install<br>
```mvn clean install```
* Run test cases through Maven<br>
```mvn test```
* Start the application<br>
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

* **Seat Map-Reserved Seats**

![screen shot 2018-05-21 at 2 01 15 am](https://user-images.githubusercontent.com/21368799/40299442-1c0da680-5c9b-11e8-9143-2595f81ad407.png)

* **Hold Timed_Out**: If 20 or more seconds are passed between holding the seats and reserving them.

![screen shot 2018-05-21 at 3 23 02 am](https://user-images.githubusercontent.com/21368799/40303165-902de1b4-5ca6-11e8-9c6c-a1bb1c5654a6.png)

* **Seat Map**: After booking 8, 32, 12, 16 seats successively

![screen shot 2018-05-21 at 2 13 12 am](https://user-images.githubusercontent.com/21368799/40299959-bbe221c6-5c9c-11e8-9801-a9518a17715d.png)

* **Check Reservation:** A user can check reservation by their email id used at time of booking.

![screen shot 2018-05-21 at 2 13 25 am](https://user-images.githubusercontent.com/21368799/40299960-bbfe3712-5c9c-11e8-8132-b1f88d32203a.png)


## Important Algorithms
#### 1. Picking Up Best Seats
* The intuition here is to follow below rules in order of preference:
	1. All the seats should be together.
	2. Seats should be far from the screen.
	3. Seats should be as near to center as possible.
* The algorithm keeps track of seats filled in each row using two pointers (left and right)
* Code Snippet
```java
public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		SeatHold seathold = new SeatHold();
		if (numSeats <= 0 || numSeats > availableSeats) {
			seathold.setValidRequest(false);
			return seathold;
		}

		List<Seat> seats = new ArrayList<Seat>();
		int l = -1, r = -1, mid = (seatMap[0].length) / 2;
		for (int row = 0; row < seatMap.length; row++) {
			if (ticketAllocationState.containsKey(row)) {
				int currLeft = ticketAllocationState.get(row)[0];
				int currRight = ticketAllocationState.get(row)[1];
				if (currLeft >= seatMap[0].length - currRight && currLeft >= numSeats) {
					l = currLeft - numSeats;
					r = currLeft;
					ticketAllocationState.put(row, new int[] { l, currRight });
				} else if (currLeft < seatMap[0].length - currRight && seatMap[0].length - currRight >= numSeats) {
					l = currRight;
					r = currRight + numSeats;
					ticketAllocationState.put(row, new int[] { currLeft, r });
				}

			} else {
				l = mid - numSeats / 2;
				r = mid + numSeats / 2;
				ticketAllocationState.put(row, new int[] { l, r });
			}

			if (l != -1 && r != -1) {
				for (int i = l; i < r; i++) {
					seatMap[row][i] = 1;
					Seat s = new Seat(row, i, false);
					seats.add(s);
				}
				break;
			}
		}
 }
```

#### 2. Holding seats for a particular time interval
* A new thread is created everytime a user tries to book a seat until 20 seconds or a reservation is made.
* Code Snippet
```java
public Thread createHoldThread(final SeatHold hs, final String reserveDecision, final String customerEmail) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("\nTickets Held! This session is valid for 20 seconds");
					Thread.sleep(20000);
					if (reserveDecision.equals("")) {
						releaseSeats(hs, customerEmail);

					}
				} catch (InterruptedException e) {
					// e.printStackTrace();
				} catch (Exception e) {
					// e.printStackTrace();
				}

			}
		});
		return t;
	}
```
