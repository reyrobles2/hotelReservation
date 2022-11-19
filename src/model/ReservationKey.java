package model;

import java.text.ParseException;
import java.util.Date;

public class ReservationKey extends Reservation{

    private final String email;
    private final String roomNumber;
    private final Date checkInDate;
    private final Date checkOutDate;

    // Methods
    public ReservationKey(String email, String roomNumber, Date checkInDate, Date checkOutDate) throws ParseException {
        super();
        this.email = email;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        Customer customer = new Customer();
        customer.validateEmailRegex(email);
        Room room = new Room();
        room.validateRoomNumber(roomNumber);
        checkDateWithCurrentDate(checkInDate);
        checkDateWithCurrentDate(checkOutDate);
        checkCheckOutDateWithCheckInDate(checkOutDate, checkInDate);
    }

    // Accessor Methods
    public String getEmail(){
        return this.email;
    }

    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }
    public Date getCheckInDate() {
        return this.checkInDate;
    }
    public Date getCheckOutDate() {
        return this.checkOutDate;
    }


    // methods
    @Override
    public int hashCode() {
        return ("Room Number: " + roomNumber+
                "CheckInDate: "+ checkInDate + "CheckOutDate: "+ checkOutDate).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // if object is not null and is an instance of Customer
        final ReservationKey reservationKey = (ReservationKey)object;
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        return this.roomNumber.equals(reservationKey.roomNumber) &&
                this.checkInDate.equals(reservationKey.checkInDate) &&
                this.checkOutDate.equals(reservationKey.checkOutDate);
    }

    @Override
    public String toString(){
        return "Email: "+this.email+ " Room No: "+this.roomNumber+
                " CheckInDate: "+ this.checkInDate+ " CheckOutDate: "+ this.checkOutDate;
    }
}
