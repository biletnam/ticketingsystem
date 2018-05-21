package ticketing;

import java.util.Scanner;

/**
 * @author ishandikshit 
 * Description: This is a driver class written to call the functions
 *         from the TicketServiceImpl class.
 */
public class App {
	static String reserveDecision = ""; // decision to be stored weather
										// reserving HELD tickets or not
	static int[][] seatMap = new int[10][40];

	/**
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		System.out.println("\n-----------------------------\n" + "\nWAL-TICKET SYSTEM");
		final TicketServiceImpl ti = new TicketServiceImpl(seatMap);
		SeatHold heldseats = null;
		String customerEmail = null;
		Thread hold = null;

		while (true) {
			Scanner scannerInt = new Scanner(System.in);
			Scanner scannerStr = new Scanner(System.in);
			String reserveOpt = "";
			if (heldseats != null)
				reserveOpt = "\n4. ***RESERVE SELECTED SEATS***";
			
			/* MAIN MENU */
			System.out.println("\n" + "\n-----------------------------\n"
					+ "\nSelect one option: \n1. VIEW AVAILABLE SEATS" + "\n2. FIND AND HOLD SEATS"
					+ "\n3. CHECK YOUR RESERVATION" + reserveOpt + "\n0. EXIT" + "\n-----------------------------\n");
			int selection = scannerInt.nextInt();
			switch (selection) {

			case 1: // VIEW AVAILABLE SEATS
				System.out.println("Seats Available: " + ti.numSeatsAvailable());
				ti.printMap();
				continue;

			case 2: // FIND AND HOLD SEATS
				System.out.println("Number of seats to book: ");
				int numSeats = scannerInt.nextInt();
				System.out.println("Your email address please: ");
				customerEmail = scannerStr.nextLine();
				heldseats = ti.findAndHoldSeats(numSeats, customerEmail);
				hold = ti.createHoldThread(heldseats, reserveDecision, customerEmail); // Create a new thread to start HELD seat timer
				hold.start();
				continue;
			
			case 3: // CHECK YOUR RESERVATION
				System.out.println("Your email address please: ");
				customerEmail = scannerStr.nextLine();
				if (ti.getBookingRecord().containsKey(customerEmail)) {
					for (Seat seat : ti.getBookingRecord().get(customerEmail).seats)
						System.out.println("Row: " + seat.row + ", Seat Number: " + seat.column);
				}
				continue;

			case 4:	//***RESERVE SELECTED SEATS*** (Only visible if one or more seats are in HELD state)
				System.out.println("\nAre you sure you want to reserve these seats? (Y/N)");
				reserveDecision = scannerStr.nextLine();
				if (reserveDecision.equals("Y"))
					ti.reserveSeats(heldseats.seatHoldId, customerEmail);
				else
					ti.releaseSeats(heldseats, customerEmail);
				reserveDecision = "";
				heldseats = null;
				continue;

			case 0: //EXIT
				ti.destroySession();
				break;

			default:
				System.out.println("C'mon! You can do better! Another try? ");
				break;
			}

			if (hold != null)
				hold.interrupt(); //Interrupt the thread
			if (selection == 0)
				break;
		}
	}

}