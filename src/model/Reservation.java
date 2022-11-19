package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class Reservation {

    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    private final SimpleDateFormat sDateFormatShort = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    private final SimpleDateFormat sDateFormatLong = new SimpleDateFormat("E MMM dd yyyy", Locale.ENGLISH);
    private final java.util.Date currentDate = new java.util.Date();

    private final Room rRoom = new Room();

    // Methods
    public Reservation(){
    }

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) throws ParseException {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        customer.validateEmailRegex(customer.getEmail());
        customer.validateFirstName(customer.getFirstName());
        customer.validateLastName(customer.getLastName());
        rRoom.validateRoomNumber(room.getRoomNumber());
        rRoom.validatePrice(room.getRoomPrice());
        checkDateWithCurrentDate(checkInDate);
        checkDateWithCurrentDate(checkOutDate);
        checkCheckOutDateWithCheckInDate(checkOutDate, checkInDate);

    }

    public String convertPriceFormat(Double price){
        return rRoom.priceFormat(price);
    }

    public Date convertStringToDate(String dateString) throws ParseException {
        if (dateString.length() > 10){
            throw new IllegalArgumentException("Error - Invalid Date Format");
        }
        sDateFormatShort.setLenient(false);
        return sDateFormatShort.parse(dateString);
    }


    public String convertDateToStringLong(Date date){
        return sDateFormatLong.format(date);
    }

    public String convertDateToStringShort(Date date){
        return sDateFormatShort.format(date);
    }

    public void checkDateWithCurrentDate(Date date) throws ParseException {
        String currentDateStr = convertDateToStringShort(currentDate);
        Date newCurrentDate = new Date();
        newCurrentDate = convertStringToDate(currentDateStr);
        if (date.compareTo(newCurrentDate) < 0) { // If Date less than Current Date
            throw new IllegalArgumentException("Error - Date entered is in the past");
        }
    }

    public void checkCheckOutDateWithCheckInDate(Date checkOutDate, Date checkInDate) {
        if (checkOutDate.compareTo(checkInDate) < 0) {
            throw new IllegalArgumentException("Error - CheckOut Date is less than CheckIn Date");
        }
    }

// Accessor methods
    public Customer getCustomer(){
        return this.customer;
    }

    public IRoom getRoom(){
        return this.room;
    }

    public String getRoomNumber(){
       return this.room.getRoomNumber();
    }

    public Date getCheckInDate() {
        return this.checkInDate;
    }

    public Date getCheckOutDate() {
        return this.checkOutDate;
    }

// Mutator methods
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }


    @Override
    public int hashCode() {
        return ("Room Number: " + this.room.getRoomNumber()+
                "CheckInDate: "+ checkInDate + "CheckOutDate: "+ checkOutDate).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // if object is not null and is an instance of Customer
        final Reservation reservation = (Reservation)object;
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        return this.room.getRoomNumber().equals(reservation.room.getRoomNumber()) &&
               this.checkInDate.equals(reservation.checkInDate) &&
               this.checkOutDate.equals(reservation.checkOutDate);
    }


    // Override toString() method
    @Override
    public String toString(){

        return this.customer.getFirstName()+" "+this.customer.getLastName()+"\n"+
                "Room: "+this.room.getRoomNumber()+ " - "+this.room.getRoomType()+"\n"+
                "Price: "+ convertPriceFormat(this.room.getRoomPrice())+ " price per night\n"+
                "Checkin Date: "+ convertDateToStringLong(this.checkInDate)+"\n"+
                "Checkout Date: "+ convertDateToStringLong(this.checkOutDate);

    }
}
