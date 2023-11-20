// Code written by Sam Clark
// Student ID - w1854525

package OOP_CWK_w1854525;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Consultation class with appropriate constructors and getter/setter methods
public class Consultation {
    protected String consultBookingNum;
    protected double consultCost;
    protected String consultNotes;
    protected String consultImage;
    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;
    protected Doctor doctor;
    protected Patient patient;

    public Consultation() {}

    public Consultation(Doctor doctor, Patient patient, String bookingNum, double cost, String notes, String image,
                        LocalDateTime startDateTime) {
        this.consultBookingNum = bookingNum;
        this.consultCost = cost;
        this.consultNotes = notes;
        this.consultImage = image;
        this.startDateTime = startDateTime;
        this.doctor = doctor;
        this.patient = patient;
    }

    public void setConsultBooking(String booking) {
        this.consultBookingNum = booking;
    }

    public String getConsultBooking() {
        return consultBookingNum;
    }

    public void setConsultCost(int consultCost) {
        this.consultCost = consultCost;
    }

    public double getConsultCost() {
        return consultCost;
    }

    public void setConsultNotes(String consultNotes) {
        this.consultNotes = consultNotes;
    }

    public String getConsultNotes() {
        return consultNotes;
    }

    public void setConsultImage(String consultImage) {
        this.consultImage = consultImage;
    }

    public String getConsultImage() {
        return consultImage;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = LocalDateTime.parse(startDateTime);
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatObject = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH:mm:ss");
        String formattedDate = getStartDateTime().format(formatObject);
        return "Consultation Details:\n-- Doctor: "+doctor.getPersonFirstName()+" "+doctor.getPersonLastName()+
                "\n-- Patient: "+patient.getPersonFirstName()+" "+patient.getPersonLastName()+
                "\n-- Date & Time: "+formattedDate+"\n-- Price: "+getConsultCost()+
                "\n-- Notes: "+getConsultNotes()+"\n-- Image: "+getConsultImage()+
                "\n-- Consultation Booking Number: "+getConsultBooking();
    }
}