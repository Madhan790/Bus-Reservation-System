package opps;

import java.sql.*;
import java.util.Scanner;



import java.sql.*;
import java.util.Scanner;

public class BusRerservation {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/busreservation";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "madhan790@";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. View Buses");
            System.out.println("2. Make a Reservation");
            System.out.println("3. Register");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewBuses();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 3:
                    registerUser();
                    break;
                case 4:
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewBuses() {
        try (//Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection con= DbConnection.getConnection();
             Statement statement = con.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM bus");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                boolean ac = resultSet.getBoolean("ac");
                int capacity = resultSet.getInt("capacity");
               String driver = resultSet.getString("driver_name");
                
                // Get the number of booked seats for the current bus
                int bookedSeats = getBookedSeatsForBus(id);

                System.out.println("Bus " + id + ": AC - " + ac + ", Capacity - " + capacity + ", Driver name - "+ driver + 
                        ", Booked Seats - " + bookedSeats + ", Available Seats - " + (capacity - bookedSeats));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getBookedSeatsForBus(int busId) {
        try (//Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);\
        Connection con= DbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "SELECT COUNT(*) FROM booking WHERE bus_no = ?")) {
            preparedStatement.setInt(1, busId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int getBusCapacity(int busId) {
        try (//Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection con= DbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "select capacity from bus where id=?;")) {
            preparedStatement.setInt(1, busId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void makeReservation() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.next();

        // Check if the user exists
        int userId = getUserIdByUsername(username);
        if (userId == -1) {
            System.out.println("User not found. Please register first.");
            return;
        }

        // Display available buses
        viewBuses();

        // Ask the user to select a bus
        System.out.print("Enter the bus ID you want to reserve: ");
        int busId = scanner.nextInt();

        // Check if the bus exists
        if (!isBusIdValid(busId)) {
            System.out.println("Invalid bus ID. Please try again.");
            return;
        }
        // Get the number of booked seats for the selected bus
        int bookedSeats = getBookedSeatsForBus(busId);

        // Get the capacity for the selected bus
        int capacity = getBusCapacity(busId);

        // Check if there are available seats
        if (bookedSeats >= capacity) {
            System.out.println("Booking is full. No more seats available.");
            return;
        }
        // Make a reservation
        try (//Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection con= DbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "INSERT INTO booking (passenger_name, bus_no, travel_date) VALUES (?, ?, NOW())")) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, busId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Reservation successful!");
            } else {
                System.out.println("Reservation failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registerUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.next();

        // Check if the username is already taken
        if (getUserIdByUsername(username) != -1) {
            System.out.println("Username is already taken. Please choose another one.");
            return;
        }

        System.out.print("Enter your password: ");
        String password = scanner.next();

        // Register the new user
        try (//Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection con= DbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "INSERT INTO user (username, password) VALUES (?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getUserIdByUsername(String username) {
        try (//Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection con= DbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "SELECT id FROM user WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static boolean isBusIdValid(int busId) {
        try (//Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        		Connection con= DbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     "SELECT id FROM bus WHERE id = ?")) {
            preparedStatement.setInt(1, busId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}



