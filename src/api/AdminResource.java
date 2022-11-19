package api;

import model.*;
import service.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

public class AdminResource {

    private static final AdminResource singletonClass = new AdminResource();

    private final CustomerService customerService = CustomerService.getSingletonClass();

    private final ReservationService reservationService = ReservationService.getSingletonClass();
    
    // Methods
    private AdminResource(){
    }

    public static AdminResource getSingletonClass() {
        return singletonClass;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms){
        for (IRoom room: rooms){
            reservationService.addRoom(room);
        }
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations(){
        reservationService.printAllReservation();
    }

    public void validateEmailRegex(String email){
        customerService.validateEmailRegex(email);
    }

    public void validateLastName(String lastName){ customerService.validateLastName(lastName);}

    public void validateFirstName(String firstName){ customerService.validateFirstName(firstName);}

    public void validateRoomNumber(String roomNumber){ reservationService.validateRoomNumber(roomNumber);}
    public RoomType getRoomTypeValues(int roomType){
        return reservationService.getRoomTypeValues(roomType);
    }

    public void validatePrice(Double price) {
        reservationService.validatePrice(price);
    }
    public Room createRoom(String roomNumber, Double price, RoomType enumeration){
        return reservationService.createRoom(roomNumber, price, enumeration);
    }

    public boolean addTestDataCustomer(int userSelectionTestData, String fileName) throws IOException {
        return reservationService.addTestDataCustomer(userSelectionTestData, fileName);
    }
    public boolean addTestDataRoom(int userSelectionTestData, String fileName) throws IOException {
        return reservationService.addTestDataRoom(userSelectionTestData, fileName);
    }
    public boolean addTestDataReservation(int userSelectionTestData, String fileName)
                                                                throws IOException, ParseException {
        return reservationService.addTestDataReservation(userSelectionTestData, fileName);
    }

    public IRoom checkIsRoomFree(Collection<IRoom> iRooms, String roomNumber) {
        return reservationService.checkIsRoomFree(iRooms, roomNumber);
    }


}
