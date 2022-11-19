import api.AdminResource;
import code.Common;

import java.util.Scanner;

public class AddTestDataMenu {

    private final String testDataFileNameCustomer = "Customer.txt";
    private final String testDataFileNameRoom = "Room.txt";
    private final String testDataFileNameReservation = "Reservation.txt";

    private final AdminResource adminResource = AdminResource.getSingletonClass();

// Methods
    public AddTestDataMenu() {
        boolean keepRunning = true;
        Scanner scanner = new Scanner(System.in);
        try {
            while (keepRunning) {
                try {
                    displayUserInputMessage();
                    int userSelection = Integer.parseInt(scanner.nextLine());
                    switch (userSelection) {
                        case 1:
                            // Add Test Data for Customer
                            testDataCustomer(scanner);
                            break;
                        case 2:
                            // Add Test Data for Room
                            testDataRoom(scanner);
                            break;
                        case 3:
                            // Add Test Data for Reservation
                            testDataReservation(scanner);
                            break;
                        case 4:
                            // Back to Admin Menu
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

    private void testDataReservation(Scanner scanner) {
        boolean addTestDataOk = true;
        try {
            System.out.println("Prepare a text file "+ testDataFileNameReservation+" and store it in src/data folder");
            String userInput = Common.userInputToContinue(scanner,
                    "Do you want to continue? - y/n");
            if (userInput.toLowerCase().equals("y")) {
                int userSelectionTestData = displayUserSelectionMessageTestData(scanner);
                addTestDataOk = adminResource.addTestDataReservation(userSelectionTestData,
                                                                    testDataFileNameReservation);
                if (addTestDataOk){
                    //Display all Reservations
                    System.out.println("\nReservations uploaded successfully");
                }
                else{
                    System.out.println("\nReservations uploaded with errors");

                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private void testDataRoom(Scanner scanner) {
        boolean addTestDataOk = true;
        try {
            System.out.println("Prepare a text file "+ testDataFileNameRoom+" and store it in src/data folder");
            String userInput = Common.userInputToContinue(scanner,
                    "Do you want to continue? - y/n");
            if (userInput.toLowerCase().equals("y")) {
                int userSelectionTestData = displayUserSelectionMessageTestData(scanner);
                addTestDataOk = adminResource.addTestDataRoom(userSelectionTestData, testDataFileNameRoom);
                if (addTestDataOk){
                    // Display all Rooms
                    System.out.println("\nRooms uploaded successfully");
                }
                else{
                    System.out.println("\nRooms uploaded with errors");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private void testDataCustomer(Scanner scanner) {
        boolean addTestDataOk = true;
        try {
            System.out.println("Prepare a text file "+ testDataFileNameCustomer+" and store it in src/data folder");
            String userInput = Common.userInputToContinue(scanner,
                    "Do you want to continue? - y/n");
            if (userInput.toLowerCase().equals("y")) {
                int userSelectionTestData = displayUserSelectionMessageTestData(scanner);
                addTestDataOk = adminResource.addTestDataCustomer(userSelectionTestData,
                                                                        testDataFileNameCustomer);
                if (addTestDataOk) {
                    // Dispay all Customer
                    System.out.println("\nCustomers uploaded successfully");
                }
                else{
                    System.out.println("\nCustomers uploaded with errors");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private int displayUserSelectionMessageTestData(Scanner scanner) {
        boolean keepRunning = true;
        int userSelection = 0;
        try {
            System.out.println("How would you like to upload your test data?\n " +
                                "Select option (1) Overwrite or (2) Add more");
            while (keepRunning) {
                try {
                    userSelection = Integer.parseInt(scanner.nextLine());
                    if ((userSelection == 1) || (userSelection == 2)) {
                        keepRunning = false;
                    } else {
                        System.out.println("Please enter (1) or (2)");
                    }

                } catch (Exception ex) {
                    System.out.println("Exception " + ex.getLocalizedMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return userSelection;
    }

    private void displayUserInputMessage() {
        System.out.println("\nAdd Test Data Menu");
        System.out.println("____________________________________________________");
        System.out.println("1. Customers");
        System.out.println("2. Rooms");
        System.out.println("3. Reservations");
        System.out.println("4. Back to Admin Menu");
        System.out.println("____________________________________________________");
        System.out.println("Please select a number for the menu option");
    }
}
