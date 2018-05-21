package ticketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author ishandikshit Implements TicketService interface
 * 
 */
public class TicketServiceImpl implements TicketService {

	int[][] seatMap;	//will contain current seatmap status
	int availableSeats;		//will contain current number of available seats
	HashMap<Integer, int[]> ticketAllocationState = new HashMap<Integer, int[]>();		//left and right pointers for filled seats in each row.
	HashMap<String, SeatHold> bookingRecord = new HashMap<String, SeatHold>();		//holds bookings done for each email 
	HashMap<Integer, SeatHold> holdMapper = new HashMap<Integer, SeatHold>();		//holds seats held done against seatholdID

	/**
	 * @param seatMap
	 * Constructor to initialize seatMap
	 */
	public TicketServiceImpl(int[][] seatMap) {
		this.seatMap = seatMap;
		availableSeats = seatMap.length * seatMap[0].length;
	}

	public HashMap<String, SeatHold> getBookingRecord() {
		return bookingRecord;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	/* (non-Javadoc)
	 * @see ticketing.TicketService#numSeatsAvailable()
	 * Implemented from TicketService interface
	 */
	public int numSeatsAvailable() {
		return availableSeats;
	}

	
	/* (non-Javadoc)
	 * @see ticketing.TicketService#findAndHoldSeats(int, java.lang.String)
	 * Description:
	 * The intuition here is to follow below rules in order of preference:
	 * 	1. All the seats should be together.
	 * 	2. Seats should be far from the screen.
	 * 	3. Seats should be as near to center as possible.
	 * The algorithm keeps track of seats filled in each row using two pointers (left and right)
	 * Returns a SeatHold object with all the information.
	 */
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

		Random rand = new Random();	//random number to generate a seaHoldId
		int seatHoldId = rand.nextInt(90000) + 10000;	//making it 5 digit
		seathold.setSeatHoldId(seatHoldId);
		seathold.setSeats(seats);
		seathold.setValidRequest(true);
		availableSeats -= numSeats;
		holdMapper.put(seatHoldId, seathold);
		bookingRecord.put(customerEmail, seathold);
		return seathold;
	}

	/* (non-Javadoc)
	 * @see ticketing.TicketService#reserveSeats(int, java.lang.String)
	 * Description:
	 * On calling this function it checks if the seats are in "HELD" state, which is 1
	 * If yes, seats are moved to RESERVED state, which is 9.
	 */
	public String reserveSeats(int seatHoldId, String customerEmail) {
		if (!holdMapper.containsKey(seatHoldId)) {
			return "HOLD_INVALID";
		}
		SeatHold seatHold = holdMapper.get(seatHoldId);
		for (Seat hold : seatHold.getSeats()) {
			if (seatMap[hold.row][hold.column] == 1) {	//HELD state identifier = 1
				seatMap[hold.row][hold.column] = 9;	////RESERVED state identifier = 9
			} else {
				System.out.println("The session was no longer valid on the selected seats. Please start again!");
				return "HOLD_INVALID";
			}
		}
		bookingRecord.put(customerEmail, seatHold);
		String confirmationCode = seatHoldId + "AXZPD021";
		System.out.println(seatHold.getSeats().size() + " seats reserved with confirmation code: " + confirmationCode);
		return confirmationCode;
	}

	public void releaseSeats(SeatHold seatHold, String customerEmail) throws Exception {
		if (null == seatHold.seats) {
			throw new Exception("Invalid Seat Hold");
		}
		for (Seat s : seatHold.seats) {
			if (seatMap[s.row][s.column] == 1) {
				bookingRecord.remove(customerEmail);
				seatMap[s.row][s.column] = 0;
				availableSeats++;
			}

		}
	}

	/**
	 * @param hs
	 * @param reserveDecision
	 * @param customerEmail
	 * Description:
	 * Holding seats for a particular time interval.
	 * A new thread is created every time a user tries to book a seat until 20 seconds or a reservation is made.
	 * @return
	 */
	public Thread createHoldThread(final SeatHold hs, final String reserveDecision, final String customerEmail) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("\nTickets Held! This session is valid for 20 seconds");
					Thread.sleep(20000);
					if (reserveDecision.equals("")) {	//Either no decision is made on reservation or seats are already reserved
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

	/**
	 * Description:
	 * Prints the current Seat Map
	 */
	public void printMap() {
		for (int row = 0; row < seatMap.length; row++) {
			for (int col = 0; col < seatMap[0].length; col++) {
				if (seatMap[row][col] == 9)
					System.out.print("R" + " ");
				else if (seatMap[row][col] == 1)
					System.out.print("H" + " ");
				else if (seatMap[row][col] == 0)
					System.out.print("." + " ");
			}
			System.out.println();
		}
		System.out.println("\n> > - - - - - - - - - - - - - - SCREEN THIS WAY - - - - - - - - - - - - - - < <");
	}

	/**
	 * Description:
	 * Destroys all the large objects to make sure they are not occupying memory anymore.
	 */
	public void destroySession() {
		seatMap = null;
		bookingRecord = null;
		ticketAllocationState=null;
		bookingRecord=null;
		holdMapper = null;
	}

}
