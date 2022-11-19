package model;

import java.text.DecimalFormat;

public class Room implements IRoom{
    protected String roomNumber;
    protected Double price;
    protected RoomType enumeration;


    protected final DecimalFormat decimalFormatPrice = new DecimalFormat("$###.##");
    protected final String RoomNumberRegex = "^[0-9]*$";


    // Methods
    public Room(){

    }
    public Room(String roomNumber, Double price, RoomType enumeration){
        super();
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
        validateRoomNumber(roomNumber);
        validatePrice(price);

    }

    public String priceFormat(double price){
        return decimalFormatPrice.format(price);
    }

    public void validatePrice(double price){
        if (price <= 0){
            throw new IllegalArgumentException("Error - room price should be > 0");
        }
    }

    // Validation methods
    public void validateRoomNumber(String roomNumber){
        if (roomNumber.length() == 0) {
            throw new IllegalArgumentException("Error - room number is blank");
        }
        else{
            if (!roomNumber.matches(RoomNumberRegex)){
                throw new IllegalArgumentException("Error - Invalid room number");
            }
            else{
                if (roomNumber.length() != 3){
                    throw new IllegalArgumentException("Error - room number format should be 999");
                }
            }

        }
    }
// Implement accessor methods from IRoom Interface
    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return this.price;
    }

    @Override
    public RoomType getRoomType() {
        return this.enumeration;
    }

    @Override
    public boolean isFree() {
        return this.price != null && this.price.equals(0.0);
    }

// Mutator methods
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setEnumeration (RoomType enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public int hashCode() {
        return ("Room Number: " + roomNumber).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // if object is not null and is an instance of Customer
        final Room room = (Room)object;
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        return this.roomNumber.equals(room.roomNumber);
    }

    // Override toString() method
    @Override
    public String toString() {
        return "Room Number: " + this.roomNumber +
                " " + this.enumeration +
                " Room Price: " + priceFormat(this.price);

    }
}
