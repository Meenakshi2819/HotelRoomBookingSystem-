package hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RoomOperations {

    // CHECK ROOM AVAILABILITY //

    public void checkRoomAvailability() {

        String sql = "SELECT * FROM rooms WHERE status = 'AVAILABLE'";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("----- AVAILABLE ROOMS -----");

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                        "Room ID: " + rs.getInt("room_id")
                        + " | Room Number: " + rs.getInt("room_number")
                        + " | Type: " + rs.getString("room_type")
                        + " | Price: " + rs.getDouble("price")
                );
            }

            if (!found) {

                System.out.println("No rooms available.");

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}