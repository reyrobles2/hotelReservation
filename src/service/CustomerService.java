package service;

import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class CustomerService {

    private static final CustomerService singletonClass = new CustomerService();
    private static final Customer customer = new Customer();
    private static final Collection<Customer> customers = new ArrayList<Customer>();
    public static final Map<String, Customer> mapOfCustomers = new HashMap<String, Customer>();

    // Methods
    private CustomerService(){
    }

    public static CustomerService getSingletonClass() {
        return singletonClass;
    }

    public void addCustomer(String email, String firstName, String lastName){
        // Check for duplicate keys
        Customer checkCustomer = getCustomer(email);
        if (checkCustomer == null) {
            Customer addCustomer = new Customer(firstName, lastName, email);
            // Add the Customer
            mapOfCustomers.put(addCustomer.getEmail(), addCustomer);
        }
        else{
            throw new IllegalArgumentException("Error - Customer Email "+email+" already exist");
        }
    }

    public Customer getCustomer(String customerEmail){
        return mapOfCustomers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers(){
        customers.clear();
        customers.addAll(mapOfCustomers.values());
        return customers;
    }

    public void validateEmailRegex(String email){
        customer.validateEmailRegex(email);
    }
    public void validateLastName(String lastName){ customer.validateLastName(lastName);}
    public void validateFirstName(String firstName){ customer.validateFirstName(firstName);}

}
