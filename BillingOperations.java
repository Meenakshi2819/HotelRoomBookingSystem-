package hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class BillingOperations {

    Scanner sc = new Scanner(System.in);

    // GENERATE BILL

    public void generateBill() {

        try {

            Connection con = DBConnection.getConnection();

            System.out.print("Enter Booking ID: ");
            int bookingId = sc.nextInt();

            String sql =
                    "SELECT b.booking_id, c.name, c.phone, " +
                    "r.room_number, r.room_type, r.price, " +
                    "b.check_in_date, b.check_out_date, " +
                    "b.status, b.total_amount " +
                    "FROM bookings b " +
                    "JOIN customers c ON b.customer_id = c.customer_id " +
                    "JOIN rooms r ON b.room_id = r.room_id " +
                    "WHERE b.booking_id = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, bookingId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("========== HOTEL BILL ==========");

                System.out.println("Booking ID    : "
                        + rs.getInt("booking_id"));

                System.out.println("Customer Name : "
                        + rs.getString("name"));

                System.out.println("Phone         : "
                        + rs.getString("phone"));

                System.out.println("Room Number   : "
                        + rs.getInt("room_number"));

                System.out.println("Room Type     : "
                        + rs.getString("room_type"));

                System.out.println("Price/Day     : ₹"
                        + rs.getDouble("price"));

                System.out.println("Check-In Date : "
                        + rs.getDate("check_in_date"));

                System.out.println("Check-Out Date: "
                        + rs.getDate("check_out_date"));

                System.out.println("Status        : "
                        + rs.getString("status"));

   

                System.out.println("TOTAL AMOUNT  : ₹"
                        + rs.getDouble("total_amount"));

            } else {

                System.out.println("Booking not found.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}