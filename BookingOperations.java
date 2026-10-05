package hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.Scanner;

public class BookingOperations {

    Scanner sc = new Scanner(System.in);

    // BOOK ROOM

    public void bookRoom() {

        Connection con = null;

        try {

            con = DBConnection.getConnection();

            System.out.print("Enter Room ID: ");
            int roomId = sc.nextInt();
            sc.nextLine();

            // Check room availability

            String checkRoom = "SELECT * FROM rooms WHERE room_id = ? AND status = 'AVAILABLE'";

            PreparedStatement roomPs = con.prepareStatement(checkRoom);

            roomPs.setInt(1, roomId);

            ResultSet roomRs = roomPs.executeQuery();

            if (!roomRs.next()) {

                System.out.println("Room is not available.");

                return;
            }

            double price = roomRs.getDouble("price");

            roomRs.close();
            roomPs.close();

            // Customer details

            System.out.print("Enter Customer Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            // Date details

            System.out.print("Enter Check-In Date (YYYY-MM-DD): ");
            String checkInDate = sc.nextLine();

            System.out.print("Enter Check-Out Date (YYYY-MM-DD): ");
            String checkOutDate = sc.nextLine();

            Date inDate = Date.valueOf(checkInDate);
            Date outDate = Date.valueOf(checkOutDate);

            // Calculate number of days

            long difference = outDate.getTime() - inDate.getTime();

            long days = difference / (1000 * 60 * 60 * 24);

            if (days <= 0) {

                System.out.println("Check-out date must be after check-in date.");

                return;
            }

            double totalAmount = days * price;

            // START TRANSACTION

            con.setAutoCommit(false);

            // Insert customer

            String customerSql =
                    "INSERT INTO customers(name, phone, email) VALUES (?, ?, ?)";

            PreparedStatement customerPs =
                    con.prepareStatement(customerSql,
                            PreparedStatement.RETURN_GENERATED_KEYS);

            customerPs.setString(1, name);
            customerPs.setString(2, phone);
            customerPs.setString(3, email);

            customerPs.executeUpdate();

            ResultSet customerKeys =
                    customerPs.getGeneratedKeys();

            int customerId = 0;

            if (customerKeys.next()) {

                customerId = customerKeys.getInt(1);
            }

            customerKeys.close();
            customerPs.close();

            // Insert booking

            String bookingSql =
                    "INSERT INTO bookings " +
                    "(customer_id, room_id, check_in_date, check_out_date, status, total_amount) " +
                    "VALUES (?, ?, ?, ?, 'BOOKED', ?)";

            PreparedStatement bookingPs =
                    con.prepareStatement(bookingSql,
                            PreparedStatement.RETURN_GENERATED_KEYS);

            bookingPs.setInt(1, customerId);
            bookingPs.setInt(2, roomId);
            bookingPs.setDate(3, inDate);
            bookingPs.setDate(4, outDate);
            bookingPs.setDouble(5, totalAmount);

            bookingPs.executeUpdate();

            ResultSet bookingKeys =
                    bookingPs.getGeneratedKeys();

            int bookingId = 0;

            if (bookingKeys.next()) {

                bookingId = bookingKeys.getInt(1);
            }

            bookingKeys.close();
            bookingPs.close();

            // Update room status

            String updateRoom =
                    "UPDATE rooms SET status = 'BOOKED' WHERE room_id = ?";

            PreparedStatement updatePs =
                    con.prepareStatement(updateRoom);

            updatePs.setInt(1, roomId);

            updatePs.executeUpdate();

            updatePs.close();

            // COMMIT TRANSACTION

            con.commit();

            System.out.println("Room booked successfully!");
            System.out.println("Booking ID: " + bookingId);
            System.out.println("Customer ID: " + customerId);
            System.out.println("Total Days: " + days);
            System.out.println("Total Amount: ₹" + totalAmount);

            con.close();

        } catch (Exception e) {

            try {

                if (con != null) {

                    con.rollback();

                    System.out.println("Transaction rolled back.");
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            e.printStackTrace();
        }
    }


    // CHECK-IN

    public void checkIn() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.print("Enter Booking ID: ");
            int bookingId = sc.nextInt();

            String sql =
                    "UPDATE bookings SET status = 'CHECKED_IN' " +
                    "WHERE booking_id = ? AND status = 'BOOKED'";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, bookingId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Check-In successful!");

            } else {

                System.out.println("Invalid Booking ID or booking already processed.");
            }

            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // CHECK-OUT

    public void checkOut() {

        Connection con = null;

        try {

            con = DBConnection.getConnection();

            System.out.print("Enter Booking ID: ");
            int bookingId = sc.nextInt();

            // Start transaction

            con.setAutoCommit(false);

            // Update booking

            String bookingSql =
                    "UPDATE bookings SET status = 'CHECKED_OUT' " +
                    "WHERE booking_id = ? AND status = 'CHECKED_IN'";

            PreparedStatement bookingPs =
                    con.prepareStatement(bookingSql);

            bookingPs.setInt(1, bookingId);

            int rows = bookingPs.executeUpdate();

            if (rows == 0) {

                System.out.println("Invalid Booking ID or customer is not checked in.");

                con.rollback();

                return;
            }

            // Find room

            String roomSql =
                    "SELECT room_id FROM bookings WHERE booking_id = ?";

            PreparedStatement roomPs =
                    con.prepareStatement(roomSql);

            roomPs.setInt(1, bookingId);

            ResultSet rs = roomPs.executeQuery();

            int roomId = 0;

            if (rs.next()) {

                roomId = rs.getInt("room_id");
            }

            // Make room available

            String updateRoom =
                    "UPDATE rooms SET status = 'AVAILABLE' WHERE room_id = ?";

            PreparedStatement updatePs =
                    con.prepareStatement(updateRoom);

            updatePs.setInt(1, roomId);

            updatePs.executeUpdate();

            // Commit

            con.commit();

            System.out.println("Check-Out successful!");
            System.out.println("Room is now available.");

            rs.close();
            roomPs.close();
            updatePs.close();
            bookingPs.close();

            con.close();

        } catch (Exception e) {

            try {

                if (con != null) {

                    con.rollback();

                    System.out.println("Transaction rolled back.");
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            e.printStackTrace();
        }
    }
}