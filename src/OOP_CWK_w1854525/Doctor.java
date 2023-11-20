// Code written by Sam Clark
// Student ID - w1854525

package OOP_CWK_w1854525;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// Doctor class with appropriate constructors and getter/setter methods
public class Doctor extends Person {
    protected String medLicenseNo;
    protected String specialisation;
    protected Consultation consultation;
    protected ArrayList<Consultation> consultList = new ArrayList<>();

    public Doctor() {}

    public Doctor(String medNum) {
        this.medLicenseNo = medNum;
    }

    public Doctor(String firstName, String lastName, LocalDate DOB, String mobileNo, String medNum, String specialise) {
        super(firstName, lastName, DOB, mobileNo);
        this.medLicenseNo = medNum;
        this.specialisation = specialise;
    }

    public void setMedLicenseNo(String medNum) {
        this.medLicenseNo = medNum;
    }

    public String getMedLicenseNo() {
        return medLicenseNo;
    }

    public void setSpecialisation(String specialise) {
        this.specialisation = specialise;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void initialiseConsultation(Consultation consultation) {
        consultation.setConsultNotes("Debug");
        consultation.setConsultBooking("Debug");
        consultation.setConsultCost(0);
        consultation.setStartDateTime("2000-01-01 00:00");

        consultList.add(consultation);
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
        consultList.add(consultation);
    }

    public Consultation getConsultation(String bookingNum) {
        if (consultList != null) {
            for (Consultation i : consultList) {
                if (i.getConsultBooking().equals(bookingNum)) {
                    return i;
                }
            }
        }
           return consultation;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatObject = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDOB = getPersonDOB().format(formatObject);

        return "Name - "+getPersonFirstName()+" "+getPersonLastName()+"\nDate of Birth - "
                +formattedDOB +"\nMobile Number - "+getPersonMobileNo()+"\nMedical License No - "+getMedLicenseNo()
                +"\nSpecialisation - "+getSpecialisation();
    }
}