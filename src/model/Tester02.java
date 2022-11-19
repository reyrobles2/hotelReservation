package model;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static model.RoomType.SINGLE;

public class Tester02 {
    public static void main(String[] args) throws ParseException {

        Map<ReservationKey, Reservation> mapOfReservations = new HashMap<ReservationKey, Reservation>();
        Map<ReservationKey, ReservationKey> mapOfReservationKeys = new HashMap<ReservationKey, ReservationKey>();
//        Map<String, ReservationKey> mapOfReservationKeys = new HashMap<String, ReservationKey>();




        Customer customer = new Customer("Jeff", "Bridges", "jeff@domain.com");
        System.out.println(customer);

//        Customer customer1 = new Customer("first", "second", "email");

        IRoom room = new Room("100",150.99, SINGLE);
        FreeRoom freeRoom = new FreeRoom("100",150.99,SINGLE);
        System.out.println(room);
        System.out.println(freeRoom);

        IRoom room1 = new Room("101",150.99, SINGLE);
        FreeRoom freeRoom1 = new FreeRoom("101",150.99,SINGLE);
        System.out.println(room1);
        System.out.println(freeRoom1);

        System.out.println("Room number comparison: "+room.getRoomNumber().equals(room1.getRoomNumber()));

        System.out.println();
        System.out.println("Reservation Testing");
        Date date = new Date();
        Reservation reservation = new Reservation(customer, room,date,date);
        ReservationKey reservationKey = new ReservationKey(customer.getEmail(), room.getRoomNumber(), date, date);
        System.out.println(room.getRoomNumber());
        System.out.println(reservationKey);
        mapOfReservations.put(reservationKey,reservation);
        String roomStr = room.getRoomNumber();
//        mapOfReservationKeys.put(roomStr, reservationKey);
        mapOfReservationKeys.put(reservationKey,reservationKey);

        Reservation reservation1 = new Reservation(customer, room1,date,date);
        ReservationKey reservationKey1 = new ReservationKey(customer.getEmail(), room1.getRoomNumber(), date, date);
        System.out.println(room1.getRoomNumber());
        System.out.println(reservationKey1);
        mapOfReservations.put(reservationKey1,reservation1);
        String roomStr1 = room1.getRoomNumber();
//        mapOfReservationKeys.put(roomStr1, reservationKey1);
        mapOfReservationKeys.put(reservationKey1, reservationKey1);


        System.out.println();
        System.out.println("Reservation Keys");
        for (ReservationKey rKey: mapOfReservations.keySet()){
            System.out.println(rKey);
//            System.out.println(rKey.getRoom().getRoomNumber());
        }

        System.out.println("Reservation Values");
        for (Reservation rValue : mapOfReservations.values()){
            System.out.println(rValue);
            System.out.println(rValue.getRoomNumber());
        }


//        if (mapOfReservationKeys.containsKey("100")){
////        if (mapOfReservationKeys.containsKey((){
//
//        System.out.println(true);
//        }
//        else{
//            System.out.println(false);
//        }
//
//        if (mapOfReservationKeys.containsValue("100")){
//            System.out.println(true);
//        }
//        else{
//            System.out.println(false);
//        }
//
//        System.out.println(mapOfReservationKeys.get("100"));
    }
}
