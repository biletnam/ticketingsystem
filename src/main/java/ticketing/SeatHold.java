package ticketing;

import java.util.List;

/**
 * @author ishandikshit 
 * Contains all the information related to a HELD seat.
 */
public class SeatHold {
	int seatHoldId;
	List<Seat> seats;
	boolean isValidRequest;

	public boolean isValidRequest() {
		return isValidRequest;
	}

	public void setValidRequest(boolean isValidRequest) {
		this.isValidRequest = isValidRequest;
	}

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

}
