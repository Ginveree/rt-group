package my.uum;

import java.sql.*;

/**
 * This class is mainly processing on MYSQLite.
 */
public class SQLite {
    /**
     * This method is to connect to MYSQLite database.
     *
     * @return The connection that connected to MYSQLite database.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e + "");
        }
        return conn;
    }

    /**
     * This method is to drop an unwanted table.
     */
    public static void dropTable() {
        String url = "jdbc:sqlite:database.db";
        String sql = "DROP TABLE roomDetails";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is to let staff adds details into roomDetails.
     */
    public static void addExtra() {
        String url = "jdbc:sqlite:database.db";
        String sql = "ALTER TABLE roomDetails ADD booking_Id TEXT FOREIGN KEY(booking_Id) REFERENCES guestDetails(booking_Id))";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is to create a new table in database.
     */
    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:database.db";
        // SQL statement for creating a new table
        String sql = "CREATE TABLE roomDetails (" +
                "room_Id INTEGER PRIMARY KEY NOT NULL , " +
                "staff_Id TEXT NOT NULL, " +
                "room_Description TEXT NOT NULL, " +
                "max_Capacity INTEGER NOT NULL, " +
                "booking_Date TEXT NOT NULL, " +
                "booking_Time TEXT NOT NULL," +
                "status TEXT NOT NULL," +
                "booking_Id TEXT," +
                "FOREIGN KEY(staff_Id) REFERENCES staffDetails(staff_Id) " +
                "FOREIGN KEY(booking_Id) REFERENCES guestDetails(booking_Id));";
//        String sql = "CREATE TABLE staffDetails (" +
//                "staff_Id TEXT NOT NULL , " +
//                "room_Id TEXT NOT NULL, " +
//                "FOREIGN KEY(room_Id) REFERENCES roomDetails(room_Id));";
//        String sql = "CREATE TABLE guestDetails (" +
//                "booking_Id INTEGER PRIMARY KEY NOT NULL , " +
//                "room_Id TEXT NOT NULL, " +
//                "ic_Number TEXT NOT NULL, " +
//                "name INTEGER NOT NULL, " +
//                "mobile_No TEXT NOT NULL, " +
//                "email TEXT NOT NULL," +
//                "purpose TEXT NOT NULL," +
//                "date_Time TEXT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ," +
//                "FOREIGN KEY(room_Id) REFERENCES roomDetails(room_Id));";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is to delete one row of data in staffDetails database based on the staff ID.
     *
     * @param staff_Id The staff ID.
     */
    public static void deleteStaffDetails(String staff_Id) {
        // SQLite connection string
        String url = "jdbc:sqlite:database.db";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM staffDetails WHERE staff_Id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, staff_Id);
            pstmt.executeUpdate();
            System.out.println("Staff's detail has been deleted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method id to delete one row of data in roomDetails database based on the room ID.
     *
     * @param room_Id The room ID.
     */
    public static void deleteRoomDetails(String room_Id) {
        // SQLite connection string
        String url = "jdbc:sqlite:database.db";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM roomDetails WHERE room_Id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, room_Id);
            pstmt.executeUpdate();
            System.out.println("Room's detail has been deleted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is to make the booking_Id column empty after the booking has been cancelled.
     *
     * @param bookingId Booking_Id that wants to be removed.
     */
    public static void deleteBookingIdinRoomDetails(String bookingId) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE roomDetails SET booking_Id = ? WHERE booking_Id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "");
            ps.setString(2, bookingId);
            ps.execute();
            System.out.println("Booking Id has been deleted");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is for admin to insert details in roomDetails database.
     *
     * @param room_Description The description of the room.
     * @param max_Capacity     The maximum capacity or the room.
     * @param booking_Date     The available date of the room.
     * @param booking_Time     The available time of the room.
     */
    public static void adminInsertRoomDetails(String room_Description, int max_Capacity, String booking_Date, String booking_Time) {
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO roomDetails(room_Description,max_Capacity,booking_Date,booking_Time) VALUES (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, room_Description);
            ps.setInt(2, max_Capacity);
            ps.setString(3, booking_Date);
            ps.setString(4, booking_Time);
            ps.execute();
            System.out.println("Data successfully inserted!");

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method will insert staff details into staffDetails database.
     *
     * @param staff_Id The staff ID.
     * @param room_Id  The room ID.
     */
    public static void insertSDetails(String staff_Id, String room_Id) {
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO staffDetails(staff_Id,room_Id) VALUES (?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, staff_Id);
            ps.setString(2, room_Id);
            ps.execute();
            System.out.println("Data successfully inserted!");

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is to insert the room details into roomDetails database manually.
     *
     * @param room_Id          The ID of the room.
     * @param staff_Id         The staff ID that in-charge in this room.
     * @param room_Description The description of the room.
     * @param max_Capacity     The maximum capacity or the room.
     * @param booking_Date     The available date of the room.
     * @param booking_Time     The available time of the room.
     * @param status           The status of the room: Available or Booked.
     * @param booking_Id       The booking ID of the room.
     */
    public static void insertRoomDetails(String room_Id, String staff_Id, String room_Description, int max_Capacity, String booking_Date, String booking_Time, String status, String booking_Id) {
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO roomDetails(room_Id,staff_Id,room_Description,max_Capacity,booking_Date,booking_Time,status,booking_Id) VALUES (?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, room_Id);
            ps.setString(2, staff_Id);
            ps.setString(3, room_Description);
            ps.setInt(4, max_Capacity);
            ps.setString(5, booking_Date);
            ps.setString(6, booking_Time);
            ps.setString(7, status);
            ps.setString(8, booking_Id);
            ps.execute();
            System.out.println("Room details successfully inserted!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is to insert the new generated booking ID into the roomDetails class based on the room ID.
     *
     * @param bookingId The booking ID that wants to be inserted.
     * @param roomId    The selected room ID.
     */
    public static void insertBookingIdIntoRoomDetails(String bookingId, String roomId) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE roomDetails SET booking_Id = ? WHERE room_Id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, bookingId);
            ps.setString(2, roomId);
            ps.execute();
            System.out.println("Booing ID has inserted in roomDetails!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is to insert the user's details into the guestDetails database.
     *
     * @param room_Id   The selected room ID.
     * @param nric      User's NRIC number.
     * @param name      User's name.
     * @param mobile_No User's mobile number.
     * @param email     User's email address.
     * @param purpose   User's purpose on booking the room.
     */
    public static void guestDetails(String room_Id, String nric, String name, String mobile_No, String email, String purpose) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO guestDetails(room_Id,ic_Number,name,mobile_No, email, purpose) VALUES (?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, room_Id);
            ps.setString(2, nric);
            ps.setString(3, name);
            ps.setString(4, mobile_No);
            ps.setString(5, email);
            ps.setString(6, purpose);
            ps.execute();
            System.out.println("Guest's details have successfully inserted!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is to change the status to Booked in roomDetails database based on the room ID.
     *
     * @param roomId The selected room ID.
     */
    public static void roomBooked(String roomId) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE roomDetails SET status = ? WHERE room_Id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Booked");
            ps.setString(2, roomId);
            ps.execute();
            System.out.println("Status from room ID has changed to BOOKED!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is to change the status to Available in roomDetails database based on the room ID.
     *
     * @param roomId The selected room ID.
     */
    public static void roomAvailable(String roomId) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE roomDetails SET status = ? WHERE room_Id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Available");
            ps.setString(2, roomId);
            ps.execute();
            System.out.println("Status from room Id has changed to AVAILABLE!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is to delete one of the details in guestDetails database based on the booking ID.
     *
     * @param bookingId The selected booking ID.
     */
    public static void deleteDetails(String bookingId) {//perfect
        String sql = "DELETE FROM guestDetails WHERE booking_Id = ?";
        try (Connection conn = SQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setString(1, bookingId);
            // execute the delete statement
            pstmt.executeUpdate();
            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is to get the booking ID from the guestDetails database based on the room ID.
     * The purpose is to inform user their booking ID after they have confirmed their booking.
     *
     * @param roomId The selected room ID.
     * @return The booking ID.
     */
    public static String getBookingIdFromGuestDetails(String roomId) {//perfect
        String bookingID = "";
        String sql = "SELECT booking_Id FROM guestDetails WHERE room_Id = ? ";
        try (Connection conn = SQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setString(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                bookingID = rs.getString("booking_Id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookingID;
    }

    /**
     * This method is to get the room ID in roomDetails database based on the booking ID.
     *
     * @param bookingId The selected booking ID.
     * @return The room ID.
     */
    public static String getRoomIdFromBookingId(String bookingId) {
        String roomid = "";
        String sql = "SELECT room_Id FROM roomDetails WHERE booking_Id = ?";
        try (Connection conn = SQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setString(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                roomid = rs.getString("room_Id") + "\n";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return roomid;
    }

    /**
     * This method is to get the booking ID in roomDetails based on the room ID.
     *
     * @param room_Id The selected room ID.
     * @return The room ID.
     */
    public static String getBookingIdFromRoomDetails(String room_Id) {//perfect
        String bookingId = "";
        String sql = "SELECT booking_Id FROM roomDetails WHERE room_Id = ?";
        try (Connection conn = SQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setString(1, room_Id);
            ResultSet rs = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                bookingId = rs.getString("booking_Id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookingId;
    }

    /**
     * This method is to update the nric number into guestDetails database based on the booking ID.
     *
     * @param nric      The new nric number.
     * @param bookingID The booking ID.
     */
    public static void menuUpdateNRIC(String nric, String bookingID) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE guestDetails SET ic_Number = ? WHERE booking_Id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, nric);
            ps.setString(2, bookingID);
            ps.execute();
            System.out.println("NRIC has been updated!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method will update the name into guestDetails database based on the booking ID.
     *
     * @param name      The new name.
     * @param bookingID The booking ID.
     */
    public static void menuUpdateName(String name, String bookingID) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE guestDetails SET name = ? WHERE booking_Id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, bookingID);
            ps.execute();
            System.out.println("Name has been updated!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method will update the mobile into guestDetails database based on the booking ID.
     *
     * @param mobile    The new mobile number.
     * @param bookingID The booking ID.
     */
    public static void menuUpdateMobile(String mobile, String bookingID) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE guestDetails SET mobile_No = ? WHERE booking_Id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, mobile);
            ps.setString(2, bookingID);
            ps.execute();
            System.out.println("Mobile number has been updated!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method will update the email address into guestDetails database based on the booking ID.
     *
     * @param email     The new email address.
     * @param bookingID The booking ID.
     */
    public static void menuUpdateEmail(String email, String bookingID) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE guestDetails SET email = ? WHERE booking_Id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, bookingID);
            ps.execute();
            System.out.println("Email address has been updated!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method will update the email address into guestDetails database based on the booking ID.
     *
     * @param purpose   The new purpose.
     * @param bookingID The booking ID.
     */
    public static void menuUpdatePurpose(String purpose, String bookingID) {//perfect
        Connection conn = SQLite.connect();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE guestDetails SET purpose = ? WHERE booking_Id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, purpose);
            ps.setString(2, bookingID);
            ps.execute();
            System.out.println("Purpose has been updated!");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * This method is to display room ID, maximum capacity, date and time based on the given capacity.
     *
     * @param max_Capacity The capacity.
     * @return room ID, maximum capacity, date and time.
     */
    public static String getCapacity(double max_Capacity) {//perfect
        String respond = "", status = "Available";
        String sql = "SELECT room_Id, max_Capacity, booking_Date, booking_Time "
                + "FROM roomDetails WHERE max_Capacity = ? AND status = ?";
        try (Connection conn = SQLite.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the value
            pstmt.setInt(1, (int) max_Capacity);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                for (int i = 0; i < 1; i++) {
                    respond += rs.getString("room_Id") + "\t\t\t\t\t\t\t\t" +
                            rs.getInt("max_Capacity") + "\t\t\t\t\t\t\t" +
                            rs.getString("booking_Date") + "\t\t   " +
                            rs.getString("booking_Time") + "\n";
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return respond;
    }
}
