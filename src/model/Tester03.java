package model;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static model.RoomType.SINGLE;

public class Tester03 {
    public static void main(String[] args) throws ParseException {

        Map<String, Customer> mapOfCustomers = new HashMap<String, Customer>();

        Map<ReservationKey, Reservation> mapOfReservations = new HashMap<ReservationKey, Reservation>();
        Map<ReservationKey, ReservationKey> mapOfReservationKeys = new HashMap<ReservationKey, ReservationKey>();
//        Map<String, ReservationKey> mapOfReservationKeys = new HashMap<String, ReservationKey>();


        Customer customer = new Customer("Jeff", "Bridges", "jeff@domain.com");
        System.out.println(customer);
        mapOfCustomers.put(customer.getEmail(), customer);


//        Customer customer1 = new Customer("first", "second", "email");
//        Customer customer1 = new Customer("Jeff", "Bridges", "jeff@domain.com");
        Customer customer1 = new Customer("Jeffrey", "Bridges", "jeff@domain.com");
        System.out.println(customer1);
//        if customer.equals(customer1));
        System.out.println(customer.equals(customer1));
        System.out.println("map of customer: "+mapOfCustomers.get(customer1.getEmail()));
//        Customer customerTemp = mapOfCustomers.get(customer1.getEmail());
        Customer customerTemp = mapOfCustomers.get("jeff@domain.com");

        System.out.println(customer1);
        if (customerTemp == null) {
            mapOfCustomers.put(customer1.getEmail(), customer1);
        }
        else{
            System.out.println("Customer already exist");
        }

        System.out.println("Map Testing");


        for (String key: mapOfCustomers.keySet()){
            System.out.println(key);
        }
        for (Customer cust: mapOfCustomers.values()){
            System.out.println(cust);
        }

    }
}