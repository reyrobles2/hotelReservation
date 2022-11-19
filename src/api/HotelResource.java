package api;

import model.*;
import service.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;


public class HotelResource {

    public Customer customer;
    public IRoom iRoom;

    private static final HotelResource singletonClass = new HotelResource();

    private final CustomerService customerService = CustomerService.getSingletonClass();

    private final ReservationService reservationService = ReservationService.getSingletonClass();

    // Methods
    private HotelResource(){
    }

    public static HotelResource getSingletonClass() {
        return singletonClass;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName){
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) throws ParseException {
        return reservationService.reserveARoom(customerService.getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        return reservationService.getCustomersReservation(customerService.getCustomer(customerEmail));
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut){
        return reservationService.findRooms(checkIn, checkOut);
    }

    public Date convertStringToDate(String dateString) throws ParseException {
        return reservationService.convertStringToDate(dateString);
    }

    public String convertDateToStringLong(Date date){
        return reservationService.convertDateToStringLong(date);
    }

    public String convertDateToStringShort(Date date){
        return reservationService.convertDateToStringShort(date);
    }

    public void checkDateWithCurrentDate(Date date) throws ParseException {
        reservationService.checkDateWithCurrentDate(date);
    }

    public Date addDaysToDate(Date checkDate, int noOfDaysOut) throws ParseException {
        return reservationService.addDaysToDate(checkDate, noOfDaysOut);
    }
}