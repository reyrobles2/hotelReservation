package service;

import model.*;
import code.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class ReservationService{


    private static final String delimiter = ",";
    private static final String packageNameData  = "data";
    private static final String testDataFileNameCustomer = "Customer.txt";
    private static final String testDataFileNameRoom = "Room.txt";
    private static final String testDataFileNameReservation = "Reservation.txt";
    private static String line = null;
    private static String filePath = null;

    private static final ReservationService singletonClass = new ReservationService();
    private static final CustomerService customerService = CustomerService.getSingletonClass();

    private static final Collection<Reservation> reservations = new ArrayList<Reservation>();
    private static final Collection<IRoom> iRooms = new ArrayList<IRoom>();
    private static final Collection<IRoom> iRoomsFree = new ArrayList<IRoom>();
    private static final Map<String, IRoom> mapOfRooms = new HashMap<String, IRoom>();
    private static final Map<String, FreeRoom> mapOfRoomsFree = new HashMap<String, FreeRoom>();
    private static final Map<String, FreeRoom> mapOfRoomsFreeFuture = new HashMap<String, FreeRoom>();
    private static final Map<ReservationKey, Reservation> mapOfReservations = new HashMap<ReservationKey, Reservation>();

    // Methods
    private ReservationService(){
    }

    public static ReservationService getSingletonClass() {
        return singletonClass;
    }

    public void addRoom(IRoom room){
        // Check for duplicate keys
        IRoom checkIRoom = getARoom(room.getRoomNumber());
        if (checkIRoom == null) {
            IRoom addRoom = new Room(room.getRoomNumber(), room.getRoomPrice(), room.getRoomType());
            // Add the Room
            mapOfRooms.put(addRoom.getRoomNumber(), addRoom);
        }
        else{
            throw new IllegalArgumentException("Error - room number "+
                                                room.getRoomNumber()+ " already exist");
        }
    }

    public IRoom getARoom(String roomId){
        return mapOfRooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        iRooms.clear();
        iRooms.addAll(mapOfRooms.values());
        return iRooms;
    }

    public synchronized Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) throws ParseException, IllegalArgumentException {
        if (customer == null) {
            throw new IllegalArgumentException("Error - Customer %s not found".formatted(customer.getEmail()));
        }
        else{
            IRoom iRoom = mapOfRooms.get(room.getRoomNumber());
            if (iRoom == null){
                throw new IllegalArgumentException("Error - Room "+room.getRoomNumber()+" not found");
            }
            else{
                Reservation reservation = new Reservation(customer,iRoom, checkInDate, checkOutDate);
                ReservationKey reservationKey = new ReservationKey(customer.getEmail(), iRoom.getRoomNumber(),checkInDate,checkOutDate);
                // Check for duplicate keys
                Reservation checkReservation = mapOfReservations.get(reservationKey);
                if (checkReservation == null) {
                    // Add to Reservations
                    mapOfReservations.put(reservationKey, reservation);
                    // Remove Room from Free Rooms
                    mapOfRoomsFree.remove(room.getRoomNumber());
                    return reservation;
                }
                else{
                    throw new IllegalArgumentException("Error - Reservation already exist - " +
                            reservation.getCustomer().getEmail() + " "+ reservation.getRoom().getRoomNumber()+" "+
                            convertDateToStringShort(checkInDate)+" "+convertDateToStringShort(checkOutDate));
                }
            }

        }
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate){
        mapOfRoomsFree.clear();
        mapOfRoomsFreeFuture.clear();  // Easy to debug, split them and merged it later
        // Build Free Rooms
        buildFreeRoomsFuture(checkInDate, checkOutDate);
        mapOfRoomsFreeFuture.putAll(mapOfRoomsFree);
        // Remove Free Rooms
        buildFreeRoomsFutureUnavailable(checkInDate, checkOutDate);
        return getTheListOfFreeRooms();
    }

    public Collection<IRoom> getTheListOfFreeRooms() {
        iRoomsFree.clear();
        iRoomsFree.addAll(mapOfRoomsFreeFuture.values());
        return iRoomsFree;
    }

    private void buildFreeRoomsFutureUnavailable(Date checkInDate, Date checkOutDate) {
        for (IRoom room : mapOfRooms.values()) {
            boolean roomUnavailable = false;
            for (ReservationKey reservationKey : mapOfReservations.keySet()){
                if (room.getRoomNumber().equals(reservationKey.getRoomNumber())) {
                    // Scenario 1:
                    // Checking for a duplicate reservation
                    if ((checkInDate.compareTo(reservationKey.getCheckInDate()) == 0) &&
                            (checkOutDate.compareTo(reservationKey.getCheckOutDate()) == 0)) {
                        // Add room to free room with price 0 (via Free Room Constructor)
                        roomUnavailable = true;
                    }
                    // Scenario 2:
                    // Checking for exact dates on both sides User Input and Reservation Dates
                    if ((checkInDate.compareTo(reservationKey.getCheckOutDate()) == 0) ||
                            (checkOutDate.compareTo(reservationKey.getCheckOutDate()) == 0) ||
                            (checkInDate.compareTo(reservationKey.getCheckInDate()) == 0) ||
                            (checkOutDate.compareTo(reservationKey.getCheckInDate()) == 0)) {
                        // Add room to free room with price 0 (via Free Room Constructor)
                        roomUnavailable = true;
                    }
                    // Scenario 3:
                    // Using ranges
                    // - checking User input checkIn and checkOut dates with the Reservation date range
                    // - checking Reservation checkIn and checkOut dates with the User input date range
                    if (Common.checkIfDateBetweenTwoDates(checkInDate,
                            reservationKey.getCheckInDate(), reservationKey.getCheckOutDate()) ||
                        Common.checkIfDateBetweenTwoDates(checkOutDate,
                            reservationKey.getCheckInDate(), reservationKey.getCheckOutDate())){
                        // Add room to free room with price 0 (via Free Room Constructor)
                        roomUnavailable = true;
                    }
                    if (Common.checkIfDateBetweenTwoDates(reservationKey.getCheckInDate(),
                            checkInDate, checkOutDate) ||
                        Common.checkIfDateBetweenTwoDates(reservationKey.getCheckOutDate(),
                            checkInDate, checkOutDate)){
                        // Add room to free room with price 0 (via Free Room Constructor)
                        roomUnavailable = true;
                    }
                    // Scenario 4:
                    // Similar to Scenario 3, but used and "AND" operator
                    // Although Scenario 3 will be sufficient and this could be redundant
                    // added it just in case
                    if (Common.checkIfDateBetweenTwoDates(checkInDate,
                            reservationKey.getCheckInDate(), reservationKey.getCheckOutDate()) &&
                            Common.checkIfDateBetweenTwoDates(checkOutDate,
                                    reservationKey.getCheckInDate(), reservationKey.getCheckOutDate())){
                        // Add room to free room with price 0 (via Free Room Constructor)
                        roomUnavailable = true;
                    }
                    if (Common.checkIfDateBetweenTwoDates(reservationKey.getCheckInDate(),
                            checkInDate, checkOutDate) &&
                            Common.checkIfDateBetweenTwoDates(reservationKey.getCheckOutDate(),
                                    checkInDate, checkOutDate)){
                        // Add room to free room with price 0 (via Free Room Constructor)
                        roomUnavailable = true;
                    }
                }
            }
            if(roomUnavailable) {
                // remove from free Room
                  mapOfRoomsFreeFuture.remove(room.getRoomNumber());
            }
        }
    }

    private void buildFreeRoomsFuture(Date checkInDate, Date checkOutDate) {
        for (IRoom room : mapOfRooms.values()) {
            boolean roomFound = false;
            boolean roomAvailable = false;
            for (ReservationKey reservationKey : mapOfReservations.keySet()){
                if (room.getRoomNumber().equals(reservationKey.getRoomNumber())) {
                    roomFound = true;
                    if ((checkInDate.compareTo(reservationKey.getCheckInDate()) < 0) &&
                        (checkOutDate.compareTo(reservationKey.getCheckInDate()) < 0)) {
                        // Add room to free room with price 0 (via Free Room Constructor)
                        roomAvailable = true;
                    }
                    if (checkInDate.compareTo(reservationKey.getCheckOutDate()) > 0){
                        // Add room to free room with price 0 (via Free Room Constructor)
                        roomAvailable = true;
                    }
                }
            }
            if (!roomFound) {
                // Room number was not found in Reservation
                // Add room to free room with price 0 (via Free Room Constructor)
                FreeRoom freeRoom = new FreeRoom(room.getRoomNumber(),
                        room.getRoomPrice(),
                        room.getRoomType());
                mapOfRoomsFree.put(room.getRoomNumber(), freeRoom);
            }
            if (roomAvailable) {
                FreeRoom freeRoom = new FreeRoom(room.getRoomNumber(),
                                    room.getRoomPrice(),
                                   room.getRoomType());
                mapOfRoomsFreeFuture.put(room.getRoomNumber(), freeRoom);
            }
        }
    }

    public Date addDaysToDate(Date checkDate, int noOfDaysOut) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkDate);
        calendar.add(Calendar.DAY_OF_MONTH, noOfDaysOut);
        checkDate = calendar.getTime();
        String currentDateStr = new Reservation().convertDateToStringShort(checkDate);
        return new Reservation().convertStringToDate(currentDateStr);
    }

    public Collection<Reservation> getCustomersReservation(Customer customer){
        reservations.clear();
        for (Reservation reservation: mapOfReservations.values()){
            if (reservation.getCustomer().getEmail().equals(customer.getEmail())){
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public void printAllReservation(){
        if (mapOfReservations.isEmpty()){
            System.out.println("No Reservations found");
        }
        else {
            for (Reservation reservation : mapOfReservations.values()) {
                System.out.println(reservation+"\n");
            }
        }
    }

    public IRoom checkIsRoomFree(Collection<IRoom> iRooms, String roomNumber) {
        for(IRoom iRoom: iRooms){
            if (iRoom.getRoomNumber().equals(roomNumber)){
                if (iRoom.isFree()){
                    return iRoom;
                }
            }
        }
        return null;
    }

    public void validateRoomNumber(String roomNumber){
        new Room().validateRoomNumber(roomNumber);
    }
    public RoomType getRoomTypeValues(int roomType){
        return RoomType.getValues(roomType);
    }
    public void validatePrice(Double price) {
        new Room().validatePrice(price);
    }
    public Room createRoom(String roomId, Double price, RoomType enumeration){
        return new Room(roomId, price, enumeration);
    }

    public Date convertStringToDate(String dateString) throws ParseException {
        return new Reservation().convertStringToDate(dateString);
    }

    public String convertDateToStringLong(Date date){
        return new Reservation().convertDateToStringLong(date);
    }
    public String convertDateToStringShort(Date date){
        return new Reservation().convertDateToStringShort(date);
    }

    public void checkDateWithCurrentDate(Date date) throws ParseException {
        new Reservation().checkDateWithCurrentDate(date);
    }

//    public String getFilePath(String fileName) throws IOException {
    String getFilePath(String fileName) throws IOException {

            // Note: this is a common method to be used for Service Layer
        // and did not add it to code package, a package of common methods used for User Interfaces
        ClassLoader classLoader;
        Enumeration<URL> srcList = null;

        switch (fileName){
            case testDataFileNameCustomer:
                Customer customer = new Customer();
                Class<?> classCustomer = customer.getClass();
                classLoader = classCustomer.getClassLoader();
                srcList = classLoader.getResources(packageNameData);
                break;
            case testDataFileNameRoom:
                Room room = new Room();
                Class<?> classRoom = room.getClass();
                classLoader = classRoom.getClassLoader();
                srcList = classLoader.getResources(packageNameData);
                break;
            case testDataFileNameReservation:
                Reservation reservation = new Reservation();
                Class<?> classReservation = reservation.getClass();
                classLoader = classReservation.getClassLoader();
                srcList = classLoader.getResources(packageNameData);
                break;
        }
        File dirFile = new File(srcList.nextElement().getFile());
        File[] files = dirFile.listFiles();
        String canonicalFile = null;
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(fileName)) {
                    // Print the File path of the Test Data file
                    System.out.println("Test Data File: " + file.getCanonicalFile());
                    canonicalFile = file.getCanonicalPath();
                    break;
                }
            }
        }
        return canonicalFile;
    }

    public boolean addTestDataCustomer(int userSelectionTestData, String fileName) throws IOException {
        boolean addTestDataOk = true;
        filePath = getFilePath(fileName);

        if (userSelectionTestData == 1) {
            CustomerService.mapOfCustomers.clear();
        }
        Scanner scanner = new Scanner(new File(filePath));

        while (scanner.hasNext()) {
            try {
                line = scanner.next();
                System.out.println();
                // Print the test data
                System.out.println(line);
                String[] customerLine = line.split(delimiter);
                customerService.addCustomer(customerLine[2], customerLine[0], customerLine[1]);
            }
            catch (Exception ex){
                    addTestDataOk = false;
                    System.out.println(ex.getLocalizedMessage());

                }
            }
        return addTestDataOk;
    }

    public boolean addTestDataRoom(int userSelectionTestData, String fileName) throws IOException {
        boolean addTestDataOk = true;
        filePath = getFilePath(fileName);

        if (userSelectionTestData == 1) {
            mapOfRooms.clear();
        }
        Scanner scanner = new Scanner(new File(filePath));

        while (scanner.hasNext()) {
            try {
                line = scanner.next();
                System.out.println();
                // Print the test data
                System.out.println(line);
                String[] roomLine = line.split(delimiter);
                Room room = new Room(roomLine[0],
                        Double.valueOf(roomLine[1]),
                        getRoomTypeValues(Integer.valueOf(roomLine[2])));
                IRoom iRoom = new Room(room.getRoomNumber(), room.getRoomPrice(), room.getRoomType());
                addRoom(iRoom);
            }
            catch (Exception ex){
                addTestDataOk = false;
                System.out.println(ex.getLocalizedMessage());

            }

        }
        return addTestDataOk;
    }

    public boolean addTestDataReservation(int userSelectionTestData, String fileName)
                                                            throws IOException {
        boolean addTestDataOk = true;
        filePath = getFilePath(fileName);

        try {
            if (userSelectionTestData == 1) {
                mapOfReservations.clear();
            }
            if (CustomerService.mapOfCustomers.isEmpty()){
                throw new IllegalArgumentException("Error - Customer file is empty. Customer is required in Reservations");
            }
        }catch (Exception ex){
            addTestDataOk = false;
            System.out.println(ex.getLocalizedMessage());
        }
        try{
            if (mapOfRooms.isEmpty()){
                throw new IllegalArgumentException("Error - Room file is empty. Rooms are required in Reservations");
            }
        }catch (Exception ex){
            addTestDataOk = false;
            System.out.println(ex.getLocalizedMessage());

        }
        Scanner scanner = new Scanner(new File(filePath));
        if (addTestDataOk) {
            while (scanner.hasNext()) {
                try {
                    line = scanner.next();
                    System.out.println();
                    // Print the test data
                    System.out.println(line);
                    String[] reservationLine = line.split(delimiter);
                    Customer customer = customerService.getCustomer(reservationLine[0]);
                    if (customer == null) {
                        throw new IllegalArgumentException("Error - Customer Email " + reservationLine[0] + " not found");
                    }
                    IRoom iRoom = getARoom(reservationLine[1]);
                    if (iRoom == null) {
                        throw new IllegalArgumentException("Error - room number " + reservationLine[1] + " not found");
                    }
                    Reservation reservation = new Reservation(customer, iRoom,
                            convertStringToDate(reservationLine[2]),
                            convertStringToDate(reservationLine[3]));
                    new ReservationKey(customer.getEmail(),
                            iRoom.getRoomNumber(),
                            reservation.getCheckInDate(),
                            reservation.getCheckOutDate());
                    reserveARoom(customer, iRoom, reservation.getCheckInDate(), reservation.getCheckOutDate());

                } catch (Exception ex) {
                    addTestDataOk = false;
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }
        return addTestDataOk;
    }
}
