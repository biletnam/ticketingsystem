package ticketing;

import java.util.Scanner;


public class App {
	static String reserveDecision = "";
	static int[][] seatMap = new int[10][40];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("---WELCOME TO THE AWESOME MOVIE THEATRE----");
		final TicketServiceImpl ti = new TicketServiceImpl(seatMap);
		while (true) {
			Scanner scanner = new Scanner(System.in);
			Scanner scannerStr = new Scanner(System.in);
			System.out.println("\n\nSelect one option: \n1. View Number of available seats" + "\n2. Book Seats"
					+ "\n3. Confirmed Users" + "\n4. Exit");
			int selection = scanner.nextInt();

			switch (selection) {
			case 1:
				System.out.println(ti.numSeatsAvailable());
				break;
			case 2:
				System.out.println("Seats to book: ");
				int numSeats = scanner.nextInt();

				System.out.println("Your email address please: ");
				String customerEmail = scannerStr.nextLine();

				SeatHold holdedseats = ti.findAndHoldSeats(numSeats, customerEmail);
				ti.printMap();

				Thread hold = ti.createHoldThread(holdedseats, reserveDecision, customerEmail);
				hold.start();

				System.out.println("\nDo you want to reserve these seats? (Y/N)");
				reserveDecision = scannerStr.nextLine();

				if (reserveDecision.equals("Y"))
					ti.reserveSeats(holdedseats.seatHoldId, customerEmail);
				else
					ti.releaseSeats(holdedseats, customerEmail);

				reserveDecision = "";
				hold.interrupt();
				break;
			case 3:
				System.out.println(ti.getBookingRecord().keySet());
				break;
			case 4:
				ti.destroySession();
				break;
			}

			if (selection == 4)
				break;

		}
	}

}