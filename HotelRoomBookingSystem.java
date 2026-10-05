package hotel;

import java.util.Scanner;

public class HotelRoomBookingSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        DBConnection.showConnectionMessage();

        RoomOperations room = new RoomOperations();

        BookingOperations booking = new BookingOperations();

        BillingOperations billing = new BillingOperations();

        while (true) {

            System.out.println("-----------------------------------");
            System.out.println("   HOTEL ROOM BOOKING SYSTEM");
            System.out.println("-------------------------------------");

            System.out.println("1. Check Room Availability");
            System.out.println("2. Book Room");
            System.out.println("3. Check-In");
            System.out.println("4. Check-Out");
            System.out.println("5. Billing & Exit");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:

                    room.checkRoomAvailability();

                    break;

                case 2:

                    booking.bookRoom();

                    break;

                case 3:

                    booking.checkIn();

                    break;

                case 4:

                    booking.checkOut();

                    break;

                case 5:

                    billing.generateBill();

                    System.out.println("Thank you for using Hotel Room Booking System.");

                    sc.close();

                    System.exit(0);

                    break;

                default:

                    System.out.println("Invalid choice");
            }
        }
    }
}
