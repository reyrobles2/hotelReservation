package code;

import java.util.Date;
import java.util.Scanner;

public class Common {

    public static String userInputToContinue(Scanner scanner, String requestInfo) {
        boolean keepRunning = true;
        String userInput = null;
        System.out.println(requestInfo);
        while (keepRunning) {
            // Validate User Input to be y or n
            userInput = scanner.nextLine();
            try {
                if (userInput.length() == 0) {
                    throw new Exception("Please enter Y (Yes) or N (No)");
                }
                else if (!userInput.toLowerCase().equals("y") &&
                        !userInput.toLowerCase().equals("n")) {
                    throw new Exception("Please enter Y (Yes) or N (No)");
                }
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
                continue;
            }
            keepRunning = false;
        }
        return userInput;
    }

    public static boolean checkIfDateBetweenTwoDates(Date dateToCheck, Date startDate, Date endDate) {
        if (dateToCheck != null && startDate != null && endDate != null) {
            return dateToCheck.after(startDate) && dateToCheck.before(endDate);
        }
        throw new IllegalArgumentException("Error - Date is null");
    }

}
