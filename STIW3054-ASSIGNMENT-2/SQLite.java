package my.uum;

import java.sql.*;

/**
 * This class is for connecting to the SQLite database to store, delete or display the input and information by the users which
 * are including user's information, booking meeting room information and room information.
 *
 * @author Wong Seow Ting
 */
public class SQLite {

    /**
     * This method is for connecting the program to the SQLite database through the driver manager.
     *
     * @return Connection to the SQLite database.
     */
    public static Connection connect() {
        String jdbc = "jdbc:sqlite:C:/Users/seowting2/IdeaProjects/assignment-2-seowting/src/main/java/SQLite/stiw3054assignment2.db";
        Connection con = null;

        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection(jdbc);
            System.out.println("Successfully connected to SQLite.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    /**
     * This method is for storing and inserting the input user information by the users into the SQLite database which are user_table.
     *
     * @param ICNO         The IC number of the users.
     * @param staff_id     The staff ID of the users.
     * @param name         The name of the users.
     * @param Mobile_TelNo The mobile telephone number of the users.
     * @param email        The email of the users.
     */
    public static void insertUser(String ICNO, String staff_id, String name, String Mobile_TelNo, String email) {
        String sql = "INSERT INTO user_table (ICNO, staff_id, name, Mobile_TelNo, email)" +
                "VALUES (?,?,?,?,?);";
        try {
            Connection con = SQLite.connect();
            PreparedStatement state;

            state = con.prepareStatement(sql);
            state.setString(1, ICNO);
            state.setInt(2, Integer.parseInt(staff_id));
            state.setString(3, name);
            state.setString(4, Mobile_TelNo);
            state.setString(5, email);
            state.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is for storing and inserting the input booking information by the users into the SQLite database which are booking_table.
     *
     * @param staff_id        The staff ID of the users.
     * @param purpose_booking The purpose of booking meeting room of the users.
     * @param Booking_Date    The booking meeting room date of the users.
     * @param Booking_Time    The booking meeting room time of the users.
     * @param Room_Id         The meeting room ID of the users would like to book.
     */
    public static void insertBooking(String staff_id, String purpose_booking, String Booking_Date, String Booking_Time, String Room_Id) {
        String sql = "INSERT INTO booking_table (staff_id, purpose_booking, Booking_Date, Booking_Time, Room_Id)" +
                "VALUES (?,?,?,?,?);";
        try {
            Connection con = SQLite.connect();
            PreparedStatement state;

            state = con.prepareStatement(sql);
            state.setInt(1, Integer.parseInt(staff_id));
            state.setString(2, purpose_booking);
            state.setDate(3, Date.valueOf(Booking_Date));
            state.setTime(4, Time.valueOf(Booking_Time));
            state.setInt(5, Integer.parseInt(Room_Id));
            state.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is for displaying all the user,booking and room information from the SQLite database which are from user_table, booking_table and room_table.
     *
     * @param staff_id The staff ID of the users.
     * @return User, booking and room information.
     */
    public static String displayUserBookingRoom(String staff_id) {
        String sql = "SELECT u.staff_id, ICNO, name, Mobile_TelNo, email, b.purpose_booking, Booking_Date, Booking_Time, r.Room_Id, " +
                "Room_Description, Maximum_Capacity FROM user_table u INNER JOIN booking_table b ON (u.staff_id = b.staff_id) " +
                "INNER JOIN room_table r ON (b.Room_Id = r.Room_Id) WHERE u.staff_id =" + staff_id;
        String userBookingRoom = "";
        try {
            Connection con = SQLite.connect();
            Statement state;

            state = con.createStatement();
            ResultSet res = state.executeQuery(sql);

            while (res.next()) {
                userBookingRoom = "\n\n***Personal Information***" +
                        "\nIC/NO: " + res.getString("ICNO") +
                        "\nStaff ID: " + res.getInt("staff_id") +
                        "\nName: " + res.getString("name") +
                        "\nPhone No: " + res.getString("Mobile_TelNo") +
                        "\nEmail Address: " + res.getString("email") +
                        "\n\n***Booking Details***" +
                        "\nBooking Purpose: " + res.getString("purpose_booking") +
                        "\nBooking Date: " + res.getDate("Booking_Date") +
                        "\nBooking Time: " + res.getTime("Booking_Time") +
                        "\n\n***Meeting Room Details***" +
                        "\nRoom ID: " + res.getInt("Room_Id") +
                        "\nRoom Description: " + res.getString("Room_Description") +
                        "\nRoom Capacity (Maximum): " + res.getInt("Maximum_Capacity");

                StringBuffer buffer = new StringBuffer(userBookingRoom);
                buffer.append(userBookingRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userBookingRoom;
    }

    /**
     * This method is for displaying some the user,booking and room information from the SQLite database which are from user_table, booking_table and room_table.
     *
     * @return Some the user,booking and room information.
     */
    public static String displayUserList() {
        String sql = "SELECT u.staff_id, b.Booking_Date, Booking_Time, r.Room_Id, Room_Description, Maximum_Capacity FROM user_table u INNER JOIN booking_table b ON (u.staff_id = b.staff_id) INNER JOIN room_table r ON (b.Room_Id = r.Room_Id)";
        String userList = "";
        try {
            Connection con = SQLite.connect();
            Statement state;

            state = con.createStatement();
            ResultSet res = state.executeQuery(sql);

            while (res.next()) {
                String userList2 = "\n\n***Personal Information***" +
                        "\nStaff ID: " + res.getInt("staff_id") +
                        "\n\n***Booking Details***" +
                        "\nRoom ID: " + res.getInt("Room_Id") +
                        "\nRoom Description: " + res.getString("Room_Description") +
                        "\nRoom Capacity (Maximum): " + res.getInt("Maximum_Capacity") +
                        "\nBooking Date: " + res.getDate("Booking_Date") +
                        "\nBooking Time: " + res.getTime("Booking_Time");

                StringBuffer buffer = new StringBuffer(userList2);
                buffer.append(userList2);
                userList = userList + userList2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * This method is for deleting the booking information from the SQLite database which are from booking_table according to the staff ID.
     *
     * @param staff_id The staff ID of the users.
     */
    public static void deleteBooking(String staff_id) {
        String sql = "DELETE FROM booking_table WHERE staff_id =" + staff_id;
        try {
            Connection con = SQLite.connect();
            PreparedStatement state;

            state = con.prepareStatement(sql);
            state.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is for deleting the user information from the SQLite database which are from user_table according to the staff ID.
     *
     * @param staff_id The staff ID of the users.
     */
    public static void deleteUser(String staff_id) {
        String sql = "DELETE FROM user_table WHERE staff_id =" + staff_id;
        try {
            Connection con = SQLite.connect();
            PreparedStatement state;

            state = con.prepareStatement(sql);
            state.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
