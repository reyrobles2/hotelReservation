import api.*;
import model.*;
import code.*;

import java.util.*;


public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getSingletonClass();
    private static final HotelResource hotelResource = HotelResource.getSingletonClass();
    private static final ArrayList<String> roomNumberList = new ArrayList<>();

    // Methods
    public AdminMenu() {
        boolean keepRunning = true;
        Scanner scanner = new Scanner(System.in);
        try {
            while (keepRunning) {
                try {
                    displayUserInputMessage();
                    int userSelection = Integer.parseInt(scanner.nextLine());
                    switch (userSelection) {
                        case 1:
                            // See all Customers
                            seeAllCustomers();
                            break;
                        case 2:
                            // See all Rooms
                            seeAllRooms();
                            break;
                        case 3:
                            // See all Reservations
                            seeAllReservations();
                            break;
                        case 4:
                            // Add a Room
                            addRooms();
                            break;
                        case 5:
                            // Add Test Data
                            new AddTestDataMenu();
                            break;
                        case 6:
                            // Back to Main Menu
                            keepRunning = false;
                            break;
                        default:
                            System.out.println("Error - Invalid Input");
                    }
                } catch (Exception ex) {
                    System.out.println("Exception "+ ex.getLocalizedMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void addRooms() {

        List<IRoom> rooms = new LinkedList<IRoom>();
        // Refresh and capture only room numbers to a List while user input
        roomNumberList.clear();
        Scanner scanner = new Scanner(System.in);
        // User Input for adding a Room
        try {
            String userInput = null;
            do {
                // Prepare the IRoom information
                rooms.add(addARoom(scanner));
                userInput = Common.userInputToContinue(scanner,
                        "Would you like to add another room y/n");
            }
            while (userInput.toLowerCase().equals("y"));
            // Add the Rooms IRoom List
            try {
                adminResource.addRoom(rooms);
                System.out.println("Room/s successfully created");
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private Room addARoom(Scanner scanner) {

        // User Inputs
        String roomNumber = userInputRoomNumber(scanner);
        Double price = userInputPrice(scanner);
        RoomType enumeration = userInputRoomType(scanner);
        // Add the room number only to the List
        roomNumberList.add(roomNumber);
        // Create a Room instance through the Resource
        return adminResource.createRoom(roomNumber, price, enumeration);
    }

    private void seeAllReservations() {
        try {
            adminResource.displayAllReservations();
        }
        catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void seeAllRooms() {
        try {
            Collection<IRoom> rooms = adminResource.getAllRooms();
            if (rooms.isEmpty()){
                System.out.println("No rooms found");
            }
            else {
                for (IRoom iRoom : rooms) {
                    System.out.println(iRoom);
                }
            }
            System.out.println();
        }
        catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void seeAllCustomers() {
        try {
            Collection<Customer> customers = adminResource.getAllCustomers();
            if (customers.isEmpty()){
                System.out.println("No Customers found");
            }
            else {
                for (Customer customer : customers) {
                    System.out.println(customer);
                }
            }
            System.out.println();
        }
        catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private RoomType userInputRoomType(Scanner scanner) {
        boolean keepRunning = true;
        int roomType = 0;
        RoomType enumeration = null;
        while (keepRunning) {
            System.out.println("Enter room type: 1 for single bed, 2 for double bed");
            // Validate User Input for room type
            try {
                roomType = Integer.parseInt(scanner.nextLine());
            }
            catch (Exception ex){
                System.out.println("Error - Invalid room type");
                continue;
            }
            // Validate room types with Enum values
            try {
                enumeration = adminResource.getRoomTypeValues(roomType);
                if (enumeration == null){
                    throw new Exception("Error - Invalid room type");
                }
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            keepRunning = false;
        }
        return enumeration;
    }

    private Double userInputPrice(Scanner scanner) {
        boolean keepRunning = true;
        Double price = 0.0;
        while (keepRunning) {
            System.out.println("Enter price per night");
            // Validate room price
            try {
                price = Double.parseDouble(scanner.nextLine());
            } catch (Exception ex) {
                System.out.println("Error - Invalid price" );
                continue;
            }
            try {
                adminResource.validatePrice(price);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            keepRunning = false;
        }
        return price;
    }

    private String userInputRoomNumber(Scanner scanner) {
        boolean keepRunning = true;
        String roomNumber = null;
        while (keepRunning) {
            System.out.println("Enter room number");
            roomNumber = scanner.nextLine();
            // Validate room number not blank
            try {
                adminResource.validateRoomNumber(roomNumber);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            // Check for room number duplicates in the temporary list
            try {
                if (roomNumberList.contains(roomNumber)){
                    throw new Exception("Error - room number already exist");
                }
                else {
                    // And then check the room number in the master list
                    try {
                        hotelResource.iRoom = hotelResource.getRoom(roomNumber);
                        if (hotelResource.iRoom != null) {
                            throw new Exception("Error - room number already exist");

                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getLocalizedMessage());
                        continue;
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            keepRunning = false;
        }
        return roomNumber;
    }

    public static void displayUserInputMessage(){
        System.out.println("\nAdmin Menu");
        System.out.println("____________________________________________________");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Add Test Data");
        System.out.println("6. Back to Main Menu");
        System.out.println("____________________________________________________");
        System.out.println("Please select a number for the menu option");
    }
}
