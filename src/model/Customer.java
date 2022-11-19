package model;

import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;

    private final String emailRegex = "^(.+)@(.+).com$";
    private final Pattern pattern = Pattern.compile(emailRegex);

    // Methods
    public Customer(){
    }

    public Customer(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        validateEmailRegex(email);
        validateFirstName(firstName);
        validateLastName(lastName);
    }
    // Validation Methods
    public void validateEmailRegex(String email) {
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Error - Invalid email - "+ email);
        }
    }
    public void validateLastName(String lastName){
        if (lastName.length() == 0){
            throw new IllegalArgumentException("Error - Last Name is blank");
        }
    }

    public void validateFirstName(String firstName){
        if (firstName.length() == 0){
            throw new IllegalArgumentException("Error - First Name is blank");
        }
    }


    // Accessor Methods
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }


// Mutator Methods
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        return ("Email: " + email).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // if object is not null and is an instance of Customer
        final Customer customer = (Customer)object;
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        return this.email.equals(customer.email);
    }

    // Override toString() method
    @Override
    public String toString(){
        return  "Email: "+ this.email + " Name: "+ this.firstName+" "+this.lastName;
    }

}
