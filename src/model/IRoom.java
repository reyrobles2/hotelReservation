package model;

public interface IRoom {

    // Abstract Methods
    public String getRoomNumber();
    public Double getRoomPrice();
    public RoomType getRoomType();
    public boolean isFree();

}
