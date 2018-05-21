package ticketing;

import static org.junit.Assert.*;

import org.junit.Test;

public class TicketServiceImplTest {
	static int[][] seatMap = new int[10][40];
	int seatHoldId=0;
	TicketServiceImpl ti = new TicketServiceImpl(seatMap);

	@Test
	public void testNumSeatsAvailable() {
		assertTrue(ti.numSeatsAvailable()==400);
	}
		
	@Test
	public void testGetBookingRecord() {
		String email = "ishan.dikshit@asu.edu";
		SeatHold seatHold = ti.findAndHoldSeats(10, email);
		assertTrue(ti.getBookingRecord().get(email).equals(seatHold));
	}
	
	@Test
	public void testGetBookingRecord_neg() {
		String email = "ishan.dikshit@asu.edu";
		SeatHold seatHold = ti.findAndHoldSeats(10, email);
		assertTrue(!seatHold.equals(ti.getBookingRecord().get("ishan.juit@gmail.com")));
	}
	

	@Test
	public void testSetAvailableSeats() {
		ti.setAvailableSeats(300);
		assertTrue(ti.numSeatsAvailable()==300);
	}

	@Test
	public void testSetAvailableSeats_neg() {
		ti.setAvailableSeats(300);
		assertTrue(!(ti.numSeatsAvailable()!=300));
	}
	
	@Test
	public void testFindAndHoldSeats() {
		String email = "ishan.dikshit@asu.edu";
		int availableSeats = ti.numSeatsAvailable();
		seatHoldId = ti.findAndHoldSeats(30, email).getSeatHoldId();
		assertTrue(ti.numSeatsAvailable()==availableSeats-30);		
	}
	
	@Test
	public void testFindAndHoldSeats_negSeats() {
		String email = "ishan.dikshit@asu.edu";
		SeatHold seatHold = ti.findAndHoldSeats(-2, email);
		assertTrue(seatHold.isValidRequest()==false);		
	}
	
	@Test
	public void testFindAndHoldSeats_exceedAvailableSeats() {
		String email = "ishan.dikshit@asu.edu";
		SeatHold seatHold = ti.findAndHoldSeats(100000, email);
		assertTrue(seatHold.isValidRequest()==false);		
	}

	@Test
	public void testReserveSeats() {
		String email = "ishan.dikshit@asu.edu";
		seatHoldId = ti.findAndHoldSeats(30, email).getSeatHoldId();
		String response = ti.reserveSeats(seatHoldId, email);
		assertTrue(response!="HOLD_INVALID");
	}

	@Test
	public void testReserveSeats_InvalidHoldId() {
		String email = "ishan.dikshit@asu.edu";
		seatHoldId = ti.findAndHoldSeats(30, email).getSeatHoldId();
		String response = ti.reserveSeats(987654321, email);
		assertTrue(response.equals("HOLD_INVALID"));
	}
	
	@Test
	public void testReleaseSeats() throws Exception {
		String email = "ishan.dikshit@asu.edu";
		int availableSeats = ti.numSeatsAvailable();
		SeatHold seatHold = ti.findAndHoldSeats(30, email);
		assertTrue(ti.numSeatsAvailable()==availableSeats-30);
		ti.releaseSeats(seatHold, email);
		assertTrue(ti.numSeatsAvailable()==availableSeats);
	}
	
	@Test
	public void testReleaseSeats_invalidHold() {
		String email = "ishan.dikshit@asu.edu";
		ti.findAndHoldSeats(30, email);
		SeatHold invalidSeatHold = new SeatHold();
		invalidSeatHold.setSeatHoldId(987654321);
		String exception = "";
		try {
			ti.releaseSeats(invalidSeatHold, email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			exception=e.getMessage();
		}
		assertTrue(exception.equals("Invalid Seat Hold"));
	}
	
	@Test
	public void testDestroySession_seatMap() {
		TicketServiceImpl newInstance = new TicketServiceImpl(new int[20][30]);
		newInstance.destroySession();
		assertTrue(newInstance.seatMap==null);
	}
	@Test
	public void testDestroySession_bookingRecord() {
		TicketServiceImpl newInstance = new TicketServiceImpl(new int[20][30]);
		newInstance.destroySession();
		assertTrue(newInstance.bookingRecord==null);
	}

}
