// Code written by Sam Clark
// Student ID - w1854525

package OOP_CWK_w1854525;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Patient class with appropriate constructors and getter/setter methods
public class Patient extends Person {
    protected String patientID;
    protected int NumOfConsults;

    public Patient() {}

    public Patient(String firstName, String lastName, LocalDate DOB, String mobileNo, String patID, int NumOfConsults) {
        super(firstName, lastName, DOB, mobileNo);
        this.patientID = patID;
        this.NumOfConsults = 0;
    }

    public void setPatientID(String patID) {
        patientID = patID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void incrementConsults() {
        this.NumOfConsults = NumOfConsults + 1;
    }

    public int getNumOfConsults() {
        return NumOfConsults;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatObject = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDOB = getPersonDOB().format(formatObject);
        return "Patient Details -- Name: "+getPersonFirstName()+" "+getPersonLastName()+", Date of Birth: "+formattedDOB
                +", Mobile Number: "+getPersonMobileNo()+", Patient ID: "+getPatientID();
    }
}