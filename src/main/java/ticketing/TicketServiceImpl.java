package ticketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TicketServiceImpl implements TicketService {

	int[][] seatMap;
	int availableSeats;
	HashMap<String, SeatHold> bookingRecord = new HashMap<String, SeatHold>();
	HashMap<Integer, SeatHold> holdMapper = new HashMap<Integer, SeatHold>();

	public TicketServiceImpl(int[][] seatMap) {
		this.seatMap = seatMap;
		availableSeats = seatMap.length * seatMap[0].length;
	}

	public HashMap<String, SeatHold> getBookingRecord() {
		return bookingRecord;
	}

	public int numSeatsAvailable() {
		// TODO Auto-generated method stub
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		// TODO Auto-generated method stub
		SeatHold seathold = new SeatHold();
		List<Seat> seats = new ArrayList<Seat>();
		int remainingSeats = numSeats;
		for (int row = 0; row < seatMap.length; row++) {
			for (int col = 0; col < seatMap[0].length; col++) {
				if (seatMap[row][col] == 0) {
					Seat s = new Seat(row, col, false);
					seats.add(s);
					seatMap[row][col] = 1;
					remainingSeats--;
				}
				if (remainingSeats <= 0) {
					break;
				}
			}
			if (remainingSeats <= 0) {
				break;
			}
		}
		Random rand = new Random();
		int seatHoldId = rand.nextInt(90000) + 10000;
		seathold.setSeatHoldId(seatHoldId);
		seathold.setSeats(seats);
		availableSeats -= numSeats;
		holdMapper.put(seatHoldId, seathold);
		bookingRecord.put(customerEmail, seathold);
		return seathold;
	}

	public String reserveSeats(int seatHoldId, String customerEmail) {
		// TODO Auto-generated method stub
		SeatHold seatHold = holdMapper.get(seatHoldId);
		for (Seat hold : seatHold.getSeats()) {
			if (seatMap[hold.row][hold.column] == 1) {
				seatMap[hold.row][hold.column] = 9;
			} else {
				System.out.println(
						"The session was no longer valid on the selected seats. Please select the seats again");
				return "HOLD_INVALID";
			}
		}
		bookingRecord.put(customerEmail, seatHold);
		String confirmationCode = seatHoldId + "AXZPD021";
		return confirmationCode;
	}

	public void printMap() {
		for (int row = 0; row < seatMap.length; row++) {
			for (int col = 0; col < seatMap[0].length; col++) {
				System.out.print(seatMap[row][col] + " ");
			}
			System.out.println();
		}
	}

	public Thread createHoldThread(final SeatHold hs, final String reserveDecision, final String customerEmail) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("This session is valid for 10 seconds");
					Thread.sleep(10000);
					if (reserveDecision.equals("")) {
						System.out.println("Session expired");
						releaseSeats(hs, customerEmail);

					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}

			}
		});
		return t;
	}

	public void releaseSeats(SeatHold seatHold, String customerEmail) {
		for (Seat s : seatHold.seats) {
			if (seatMap[s.row][s.column] == 1) {
				seatMap[s.row][s.column] = 0;
				availableSeats++;
			}
			bookingRecord.remove(customerEmail);
		}
	}

	public void destroySession() {
		seatMap = null;
		holdMapper = null;
		bookingRecord=null;
	}

}
