import api.AdminResource;
import api.HotelResource;
import code.Common;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;


public class MainMenu {

    private static final HotelResource hotelResource = HotelResource.getSingletonClass();
    private static final AdminResource adminResource = AdminResource.getSingletonClass();

    // Methods
    public MainMenu(){

        boolean keepRunning = true;
        Scanner scanner = new Scanner(System.in);

        try {
            while (keepRunning) {
                try {
                    displayUserInputMessage();
                    int userSelection = Integer.parseInt(scanner.nextLine());
                    switch (userSelection){
                        case 1:
                            // Find and reserve a room
                            findReserveARoom();
                            break;
                        case 2:
                            // See my reservations
                            seeMyReservations();
                            break;
                        case 3:
                            // Create an Account
                            createAccount();
                            break;
                        case 4:
                            // Admin
                            new AdminMenu();
                            break;
                        case 5:
                            // Exit
                            System.exit(0);
                        default:
                            System.out.println("Error - Invalid Input");
                    }
                }
                catch (Exception ex){
                    System.out.println("Exception "+ ex.getLocalizedMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        } finally {
            scanner.close();
        }
   }

    private void findReserveARoom() {
        Collection<IRoom> iRooms;
        Scanner scanner = new Scanner(System.in);
        boolean sysDefault = false;
        try {
            // User Input CheckIn and CheckOut Dates
            Date checkInDate = userInputCheckInDate(scanner);
            Date checkOutDate = userInputCheckOutDate(checkInDate, scanner);

            // Store the original CheckIn and CheckOut Dates in case we need it for no. of days out
            Date checkInDateBase = checkInDate;
            Date checkOutDateBase = checkOutDate;
            // Find a Room based on User CheckIn and CheckOut Date
            iRooms = hotelResource.findARoom(checkInDate, checkOutDate);
            String checkInDateStr = hotelResource.convertDateToStringShort(checkInDate);
            String checkOutDateStr = hotelResource.convertDateToStringShort(checkOutDate);

            if (iRooms.isEmpty()) {
                // If it is empty, Add CheckIn and Checkout Dates Plus 7
                // and ran find a room again
                try {
                    checkInDate = hotelResource.addDaysToDate(checkInDateBase, 7);
                } catch (ParseException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
                try {
                    checkOutDate = hotelResource.addDaysToDate(checkOutDateBase, 7);

                } catch (ParseException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }

                String userInput = null;
                sysDefault = true;
                do {
                    // Find a Room
                    iRooms = hotelResource.findARoom(checkInDate, checkOutDate);
                    checkInDateStr = hotelResource.convertDateToStringShort(checkInDate);
                    checkOutDateStr = hotelResource.convertDateToStringShort(checkOutDate);

                    if (iRooms.isEmpty()) {
                        // If no rooms available, this time ask the user to enter no of days out
                        if (sysDefault){
                            sysDefault = false;
                            System.out.println("No Rooms Available\n");
                        }
                        else{
                            System.out.println("No Rooms Available from "+checkInDateStr+" to "
                                                                        +checkOutDateStr+"\n");
                        }
                        //*****
                        // If no rooms available, ask the user if they still wish to continue
                        // and keep prompting them to enter the no of days out
                        // Otherwise, exit
                        userInput = Common.userInputToContinue(scanner,
                                "Would you like to continue by entering the Number of Days Out? - y/n");
                        if (userInput.toLowerCase().equals("y")) {
                            // User input prompting the user to enter no of days out
                            int noOfDaysOut = userInputNoOfDaysOut(scanner);
                            // Calculate the new Check Dates based from User input
                            try {
                                checkInDate = hotelResource.addDaysToDate(checkInDateBase, noOfDaysOut);
                            } catch (ParseException ex) {
                                System.out.println(ex.getLocalizedMessage());
                            }
                            try {
                                checkOutDate = hotelResource.addDaysToDate(checkOutDateBase, noOfDaysOut);
                            } catch (ParseException ex) {
                                System.out.println(ex.getLocalizedMessage());
                            }
                        } else {
                            // User does not want to continue entering the no of days out
                            break;
                        }
                    } else {
                        // Display the IRooms Free
                        seeAllRoomsFree(iRooms, checkInDateStr, checkOutDateStr, sysDefault);
                        if (userInput == null) {
                            // This happens if system recommendation (plus 7) has free rooms
                            break;
                        }
                        else {
                            //*****
                            // Even if there free rooms available
                            // Give the user the option to play around with the check dates for flexibility
                            // and ask the user if they still wish to continue
                            // and keep prompting them to enter the no of days out
                            // Otherwise, exit
                            userInput = Common.userInputToContinue(scanner,
                                    "Would you like to continue by entering the Number of Days Out? - y/n");
                            if (userInput.toLowerCase().equals("y")) {
                                // User input prompting the user to enter no of days out
                                int noOfDaysOut = userInputNoOfDaysOut(scanner);
                                // Calculate the new Check Dates based from User input
                                try {
                                    checkInDate = hotelResource.addDaysToDate(checkInDateBase, noOfDaysOut);
                                } catch (ParseException ex) {
                                    System.out.println(ex.getLocalizedMessage());
                                }
                                try {
                                    checkOutDate = hotelResource.addDaysToDate(checkOutDateBase, noOfDaysOut);
                                } catch (ParseException ex) {
                                    System.out.println(ex.getLocalizedMessage());
                                }
                            } else {
                                // User does not want to continue entering the no of days out
                                break;
                            }
                        }
                    }
                }
                while (userInput.toLowerCase().equals("y"));

            } else {
                // Display the IRooms free
                seeAllRoomsFree(iRooms,checkInDateStr, checkOutDateStr, sysDefault);
            }
            //
            //********************** Booking Logic
            if (!iRooms.isEmpty()) {
                // Book the Room
                booking(iRooms, checkInDate, checkOutDate, scanner);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());

        }
    }

    private void booking(Collection<IRoom> iRooms, Date checkInDate,
                         Date checkOutDate, Scanner scanner) throws Exception {

       String userInputBook = Common.userInputToContinue(scanner,
                "Would you like to book a room? - y/n");
        if (userInputBook.toLowerCase().equals("y")) {

            String userInputAccount = Common.userInputToContinue(scanner,
                    "Do you have an account with us? - y/n");
            if (userInputAccount.toLowerCase().equals("y")) {
                // Enter an existing customer email
                String email = userInputEmailBook(scanner);
                // Check if customer email not null, if null customer has exited booking
                if (email != null){
                    // Enter an existing Room Number
                     IRoom iRoom = userInputRoomNumberBook(iRooms, scanner);
                    // After Free Rooms were validated from user input,
                    // perform the room booking and display the reservation
                        System.out.println("Reservation: ");
                        System.out.println(hotelResource.bookARoom(email,
                                            iRoom, checkInDate, checkOutDate)+"\n");
                }
            }
        }
    }

    private void createAccount() {
        boolean keepRunning = true;
        Scanner scanner = new Scanner(System.in);
        try {
            while (keepRunning) {
                // User Inputs
                String email = userInputEmail(scanner);
                String firstName = userInputFirstName(scanner);
                String lastName = userInputLastName(scanner);
                // Create the Account
                try {
                    hotelResource.createACustomer(email, firstName, lastName);
                    System.out.println("Account successfully created.");
                }
                catch (IllegalArgumentException ex) {
                    System.out.println(ex.getLocalizedMessage());
                    System.out.println("Account was not created. Please retry again.");
                    continue;
                }
                keepRunning = false;
            }
        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private void seeMyReservations() {
        Scanner scanner = new Scanner(System.in);
        String email = userInputEmailBook(scanner);
        if (email != null){
            try {Collection<Reservation> myReservations   = hotelResource.getCustomersReservations(email);
                if (myReservations.isEmpty()){
                    System.out.println("No Reservations found for "+ email+"\n");
                }
                else {
                    for (Reservation myReservation : myReservations) {
                        System.out.println(myReservation+"\n");
                    }
                }
            }
            catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());

            }
        }
    }

    private void seeAllRoomsFree(Collection<IRoom> iRooms, String checkInDateStr,
                                                            String checkOutDateStr, boolean sysDefault) {
        if (sysDefault){
            System.out.println("Free Rooms from " + checkInDateStr + " to " + checkOutDateStr +
                                                    " (System Recommended)");
        }
        else {
            System.out.println("Free Rooms from " + checkInDateStr + " to " + checkOutDateStr);
        }
        for (IRoom iRoom: iRooms){
            System.out.println(iRoom);
        }
        System.out.println();
    }

    private IRoom userInputRoomNumberBook(Collection<IRoom> iRooms, Scanner scanner) {
        boolean keepRunning = true;
        IRoom iRoom = null;
        while (keepRunning) {
            System.out.println("What room number would you like to reserve");
            String roomNumber = scanner.nextLine();
            // Validate room number not blank
            try {
                adminResource.validateRoomNumber(roomNumber);
                // Check if the room entered is from the Room Available List
                 iRoom = adminResource.checkIsRoomFree(iRooms, roomNumber);
                if (iRoom == null){
                    throw new IllegalArgumentException("Room Number: "+roomNumber+" is not available");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            keepRunning = false;
        }
        return iRoom;
    }

    private String userInputEmailBook(Scanner scanner) {
        boolean keepRunning = true;
        String email = null;
        while (keepRunning) {
            System.out.println("Enter Email format: name@domain.com");
            email = scanner.nextLine();
            // Check for a valid email format
            try {
                adminResource.validateEmailRegex(email);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            // A valid and existing Email is required when Booking
            try{
                hotelResource.customer = hotelResource.getCustomer(email);
                if (hotelResource.customer == null){
                    throw new Exception("Error - Email not found. Create an Account or check with Admin");
                }
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            if (hotelResource.customer == null) {
                try {
                    String userInputAccount = Common.userInputToContinue(scanner,
                            "Do you still want to continue? - y/n");
                    if (userInputAccount.toLowerCase().equals("y")) {
                        continue;
                    }
                    else{
                        email = null;
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
            keepRunning = false;
        }
        return email;
    }

    private int userInputNoOfDaysOut(Scanner scanner) {
        boolean keepRunning = true;
        int noOfDaysOut = 0;
        while (keepRunning) {
            System.out.println("Enter the Number of Days Out");
            // Validate no of days out
            try {
                noOfDaysOut = Integer.parseInt(scanner.nextLine());
            } catch (Exception ex) {
                System.out.println("Error - Invalid Number of Days Out" );
                continue;
            }
            keepRunning = false;
        }
        return noOfDaysOut;
    }

    private Date userInputCheckOutDate(Date checkInDate, Scanner scanner) {
        boolean keepRunning = true;
        Date checkOutDate = null;
        while (keepRunning) {
            System.out.println("Enter CheckOut Date month/day/year example 2/21/2020");
            // Validate CheckOut Date
            try {
                checkOutDate = hotelResource.convertStringToDate(scanner.nextLine());
            } catch (ParseException ex) {
                System.out.println("Error - Invalid Date Format" );
                continue;
            }
            catch (IllegalArgumentException ex){
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            try{
                hotelResource.checkDateWithCurrentDate(checkOutDate);
            } catch (ParseException ex){
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            catch (IllegalArgumentException ex){
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            if (checkOutDate.compareTo(checkInDate)< 0){
                System.out.println("Error - CheckOut Date is less than CheckIn Date" );
                continue;
            }
            keepRunning = false;
        }
        return checkOutDate;
    }

    private Date userInputCheckInDate(Scanner scanner) {
        boolean keepRunning = true;
        Date checkInDate = null;
        while (keepRunning) {
            System.out.println("Enter CheckIn Date mm/dd/yyyy example 02/01/2020");
            // Validate CheckIn Date
            try {
                checkInDate = hotelResource.convertStringToDate(scanner.nextLine());
            } catch (ParseException ex) {
                System.out.println("Error - Invalid Date Format" );
                continue;
            }
            catch (IllegalArgumentException ex){
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            try{
                hotelResource.checkDateWithCurrentDate(checkInDate);
            } catch (ParseException ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            catch (IllegalArgumentException ex){
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            keepRunning = false;
        }
        return checkInDate;
    }

    private String userInputLastName(Scanner scanner) {
        boolean keepRunning = true;
        String lastName = null;
        while (keepRunning) {
            System.out.println("Last Name");
            lastName = scanner.nextLine();
            // Validate Last Name
            try {
                adminResource.validateLastName(lastName);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            keepRunning = false;
        }
        return lastName;
    }

    private String userInputFirstName(Scanner scanner) {
        boolean keepRunning = true;
        String firstName = null;
        while (keepRunning) {
            System.out.println("First Name");
            firstName = scanner.nextLine();
            // Validate First Name
            try {
                adminResource.validateFirstName(firstName);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            keepRunning = false;
        }
        return firstName;
    }

    private String userInputEmail(Scanner scanner) {
        boolean keepRunning = true;
        String email = null;
        while (keepRunning) {
            System.out.println("Enter Email format: name@domain.com");
            email = scanner.nextLine();
            // Check for a valid email format
            try {
                adminResource.validateEmailRegex(email);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            // Check if email already exist
            try{
                hotelResource.customer = hotelResource.getCustomer(email);
                if (hotelResource.customer != null){
                    throw new Exception("Error - Email already exist");
                }
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            keepRunning = false;
        }
        return email;
    }

    public static void displayUserInputMessage(){
        System.out.println("Welcome to the Hotel Reservation Application");
        System.out.println("____________________________________________________");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an Account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("____________________________________________________");
        System.out.println("Please select a number for the menu option");
    }
}
