// Code written by Sam Clark
// Student ID - w1854525

package OOP_CWK_w1854525;

import java.time.LocalDate;

// Person class with appropriate constructors and getter/setter methods
public class Person {
    protected String personFirstName;
    protected String personLastName;
    protected LocalDate personDOB;
    protected String personMobileNo;

    public Person() {
    }

    public Person(String firstName, String lastName, LocalDate DOB, String mobileNo) {
        this.personFirstName = firstName;
        this.personLastName = lastName;
        this.personDOB = DOB;
        this.personMobileNo = mobileNo;
    }

    public void setPersonFirstName(String firstName) {
        this.personFirstName = firstName;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonLastName(String lastName) {
        this.personLastName = lastName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonDOB(LocalDate DOB) {
        this.personDOB = DOB;
    }

    public LocalDate getPersonDOB() {
        return personDOB;
    }

    public void setPersonMobileNo(String mobileNo) {
        this.personMobileNo = mobileNo;
    }

    public String getPersonMobileNo() {
        return personMobileNo;
    }
}