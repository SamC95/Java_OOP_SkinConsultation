// Code written by Sam Clark
// Student ID - w1854525

package OOP_CWK_w1854525;

import javax.crypto.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class GUI extends JFrame {

    protected JFrame doctorFrame;
    protected JFrame patientFrame;
    protected JFrame existingPatientFrame;
    protected JFrame consultFrame;
    protected JFrame consultInfoFrame;
    protected JFrame consultNumFrame;

    private static JTextField textFirstName;
    private static JTextField textLastName;
    private static JTextField textMobileNum;
    private static JTextField textPatientId;
    private static JTextField existingPatientId;
    private static JTextField consultFinder;
    private static JTextField textBookingNum;
    private static JTextArea textNotes;

    // Options that will appear in JComboBox drop-down lists, "--" represents default that must be changed
    private final String[] DAYS = {"--", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                                    "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    private final String[] MONTHS = {"--", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    private final String[] HOURS = {"--", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};

    private final String[] MINUTES = {"--", "00"};

    public GUI(ArrayList<Doctor> doctorList, ArrayList<Patient> patientList,
               ArrayList<Consultation> consultList, HashSet<String> uniquePatientId, HashSet<String> uniqueBookingNum) {
        
        ArrayList<Integer> intYears = new ArrayList<>();
        ArrayList<String> stringYears = new ArrayList<>();
        ArrayList<Integer> intConsultYears = new ArrayList<>();
        ArrayList<String> stringConsultYears = new ArrayList<>();

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = currentYear; i > currentYear - 101; i--) {
            intYears.add(i);    // Creates list of each year from the current year back to 100 years prior.
        }
        for (Integer i : intYears) {
            stringYears.add(String.valueOf(i));
        }
        stringYears.add(0, "----"); //Default value for Years JComboBox, ensures user must select a value in each box to continue
        String[] Years = stringYears.toArray(new String[0]);

        for (int i = currentYear; i < currentYear + 2; i++) {
            intConsultYears.add(i);
        }
        for (Integer i : intConsultYears) {
            stringConsultYears.add(String.valueOf(i));
        }
        stringConsultYears.add(0, "----");
        String[] consultYears = stringConsultYears.toArray(new String[0]);

        // Clears these arraylists since they won't be used for anything else
        intYears.clear();
        intConsultYears.clear();

        // Opens table of doctors
        doctorFrame(doctorList, patientList, consultList, uniquePatientId, uniqueBookingNum, Years, consultYears);
    }
    
    public void doctorFrame(ArrayList<Doctor> doctorList, ArrayList<Patient> patientList,
                            ArrayList<Consultation> consultList, HashSet<String> uniquePatientId, HashSet<String> uniqueBookingNum,
                            String[] Years, String[] consultYears) {
        doctorFrame = new JFrame();
        doctorFrame.setTitle("Table of Doctors");

        String[] namesOfColumns = {"Name", "Date of Birth", "Mobile No", "Medical No", "Specialisation"};
        DefaultTableModel tableModel = new DefaultTableModel(namesOfColumns, 0);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } // Prevents user from being able to edit the text within each field, ensures double-click works for selection.
        };
        

        for (Doctor doctor : doctorList) {
            String name = doctor.getPersonFirstName() + " " + doctor.getPersonLastName();
            LocalDate DOB = doctor.getPersonDOB();
            String mobileNum = doctor.getPersonMobileNo();
            String medNum = doctor.getMedLicenseNo();
            String role = doctor.getSpecialisation();

            if (role.contains("#")) {
                role = role.replace("#", " "); // Removes # when displaying specialisation on table
            }
            
            Object[] tableData = {name, DOB, mobileNum, medNum, role};
            tableModel.addRow(tableData);
        }

        JLabel label = new JLabel("Select a doctor to book a consultation.");
        doctorFrame.add(label, BorderLayout.SOUTH); //Guides user to select a doctor, always placed on the south-most of the frame.

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    JTable target = (JTable) event.getSource(); // Detects where the mouseClick occurred
                    int row = target.getSelectedRow();
                    String doctorId = (String) target.getValueAt(row, 3); //Stores the selected doctor's medical license number for later use

                    String[] options = {"New Patient", "Existing Patient", "Existing Consultation"};

                    int selection = JOptionPane.showOptionDialog(patientFrame,
                            "Book as new patient, existing patient or search for an existing consultation?", "Patient Option",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                    if (selection == JOptionPane.YES_OPTION) {
                        patientFrame(doctorList, patientList, consultList, uniquePatientId, uniqueBookingNum, doctorId, Years, consultYears);
                    }

                    else if (selection == JOptionPane.NO_OPTION) {
                        existingPatient(doctorList, patientList, consultList, uniquePatientId, uniqueBookingNum, doctorId, consultYears);
                    }

                    else if (selection == 2) {
                        checkConsultation(uniqueBookingNum, consultList);
                    }
                }
            }
        });

        table.setBounds(30, 40, 300, 400);
        JScrollPane scrollPane = new JScrollPane(table);
        doctorFrame.add(scrollPane);

        doctorFrame.setSize(700, 300);
        table.setRowSorter(new TableRowSorter<>(tableModel));
        table.setDefaultRenderer(Object.class, centerRenderer);
        doctorFrame.setLocationRelativeTo(null);
        doctorFrame.setAlwaysOnTop(true); // Ensures doctor table always appears on top when first opening
        doctorFrame.setVisible(true);
        doctorFrame.setAlwaysOnTop(false); // Removes always on top requirement after the table has been opened for the first time
        doctorFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // Window for finding an existing consultation by inputting the booking number
    public void checkConsultation(HashSet<String> uniqueBookingNum, ArrayList<Consultation> consultList) {
        doctorFrame.setVisible(false);
        consultNumFrame = new JFrame();
        JPanel panel = new JPanel();
        consultNumFrame.setTitle("Find a Consultation");
        consultNumFrame.add(panel);

        panel.setLayout(null);

        JLabel bookingNum = new JLabel("Enter Booking Number: ");
        bookingNum.setBounds(80, 20, 230, 25);
        consultFinder = new JTextField(10);
        consultFinder.setBounds(240,20,165,25);

        JButton submit = new JButton("Submit");
        submit.setBounds(190, 70, 100, 25);

        panel.add(bookingNum);
        panel.add(consultFinder);
        panel.add(submit);

        consultNumFrame.setSize(500, 150);
        consultNumFrame.setResizable(false);
        consultNumFrame.setLocationRelativeTo(null);
        consultNumFrame.setVisible(true);
        consultNumFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // If user presses cross to close the program
        // Discards the current window and reopens the list of doctors
        consultNumFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                consultNumFrame.dispose();
                doctorFrame.setVisible(true);
            }
        });

        // If user presses submit, runs this block of code
        submit.addActionListener(event -> {
            String consultNumber = consultFinder.getText();
            consultNumber = WestminsterSkinConsultationManager.removeWhiteSpace(consultNumber);

            if (consultNumber.isEmpty()) {
                JOptionPane.showMessageDialog(consultNumFrame, "Please enter a consultation booking number");
            }
            else if (consultNumber.equals("Debug")) { 
            // Prevents user from accessing the initializer consultation used in consultFrame to prevent NullPointerException
                JOptionPane.showMessageDialog(consultNumFrame, "This input is not allowed, please enter a different value");
            }
            else if (!uniqueBookingNum.contains(consultNumber)) {
                JOptionPane.showMessageDialog(consultNumFrame, "No consultation found with this booking number");
            }
            else {
                consultNumFrame.dispose();
                consultInfo(consultList, consultNumber);
            }
        });
    }

    // Window for finding an existing patient by entering their patient ID number
    // asks if the user wants to add a consultation for the found patient
    public void existingPatient(ArrayList<Doctor> doctorList, ArrayList<Patient> patientList,
                                ArrayList<Consultation> consultList, HashSet<String> uniquePatientId,
                                HashSet<String> uniqueBookingNum, String doctorId, String[] consultYears) {
        doctorFrame.setVisible(false);
        existingPatientFrame = new JFrame();
        JPanel panel = new JPanel();
        existingPatientFrame.setTitle("Find Patient");
        existingPatientFrame.add(panel);

        panel.setLayout(null);

        JLabel patientId = new JLabel("Enter Patient ID: ");
        patientId.setBounds(110, 20, 230, 25);
        existingPatientId = new JTextField(10);
        existingPatientId.setBounds(210,20,165,25);

        JButton submit = new JButton("Submit");
        submit.setBounds(190, 70, 100, 25);

        panel.add(patientId);
        panel.add(existingPatientId);
        panel.add(submit);

        existingPatientFrame.setSize(500, 150);
        existingPatientFrame.setResizable(false);
        existingPatientFrame.setLocationRelativeTo(null);
        existingPatientFrame.setVisible(true);
        existingPatientFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        existingPatientFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                existingPatientFrame.dispose();
                doctorFrame.setVisible(true);
            }
        });

        submit.addActionListener(event -> {
            String existingId = existingPatientId.getText();
            existingId = WestminsterSkinConsultationManager.removeWhiteSpace(existingId);

            if (existingId.isEmpty()) {
                JOptionPane.showMessageDialog(existingPatientFrame, "Please enter a patient ID number");
            }
            else if (existingId.equals("Empty")) { // Prevents user from accessing patient used to initialise the arraylist later
                JOptionPane.showMessageDialog(existingPatientFrame, "This patient ID is not allowed, please use a different one");
            }
            else if (!uniquePatientId.contains(existingId)) {
                JOptionPane.showMessageDialog(existingPatientFrame, "No Patient found with this ID number");
            }
            else {
                existingPatientFrame.dispose();
                consultFrame(uniqueBookingNum, doctorList, patientList, consultList, doctorId, existingId, consultYears);
            }
        });
    }

    // Window for user to enter patient details and then continue to booking a consultation or return to the doctor table
    public void patientFrame(ArrayList<Doctor> doctorList, ArrayList<Patient> patientList,
                             ArrayList<Consultation> consultList, HashSet<String> uniquePatientId,
                             HashSet<String> uniqueBookingNum, String doctorId, String[] Years, String[] consultYears) {
        doctorFrame.setVisible(false);
        patientFrame = new JFrame();
        JPanel panel = new JPanel();
        patientFrame.add(panel);
        patientFrame.setTitle("Enter Patient Details");

        panel.setLayout(null);
        
        // Creates all the labels and text-fields with appropriate layout and then adds them to the JPanel
        JLabel labelFirstName = new JLabel("Enter First Name: ");
        labelFirstName.setBounds(10, 20, 200, 25);

        textFirstName = new JTextField(10);
        textFirstName.setBounds(250, 20, 165, 25);

        JLabel labelLastName = new JLabel("Enter Surname: ");
        labelLastName.setBounds(10, 60, 200, 25);

        textLastName = new JTextField(10);
        textLastName.setBounds(250, 60, 165, 25);

        JLabel labelDateOfBirth = new JLabel("Enter Date of Birth (DD-MM-YYYY): ");
        labelDateOfBirth.setBounds(10, 100, 200, 25);

        JComboBox<String> day = new JComboBox<>(DAYS);
        day.setSize(50, 20);
        day.setBounds(250, 100, 40, 25);

        JComboBox<String> month = new JComboBox<>(MONTHS);
        month.setSize(50, 20);
        month.setBounds(300, 100, 40, 25);

        JComboBox<String> year = new JComboBox<>(Years);
        year.setSize(50, 20);
        year.setBounds(350, 100, 70, 25);

        JLabel labelMobileNum = new JLabel("Enter Mobile Number: ");
        labelMobileNum.setBounds(10, 140, 200, 25);

        textMobileNum = new JTextField(10);
        textMobileNum.setBounds(250, 140, 165, 25);

        JLabel labelPatientId = new JLabel("Enter Patient ID Number: ");
        labelPatientId.setBounds(10, 180, 200, 25);

        textPatientId = new JTextField(10);
        textPatientId.setBounds(250, 180, 165, 25);

        JButton submit = new JButton("Submit");
        submit.setBounds(165, 250, 100, 25);

        panel.add(labelFirstName);
        panel.add(textFirstName);

        panel.add(labelLastName);
        panel.add(textLastName);

        panel.add(labelDateOfBirth);
        panel.add(day);
        panel.add(month);
        panel.add(year);

        panel.add(labelMobileNum);
        panel.add(textMobileNum);

        panel.add(labelPatientId);
        panel.add(textPatientId);

        panel.add(submit);

        patientFrame.setSize(450, 350);
        patientFrame.setResizable(false);
        patientFrame.setLocationRelativeTo(null);
        patientFrame.setVisible(true);
        patientFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        patientFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                patientFrame.dispose();
                doctorFrame.setVisible(true);
            }
        });

        submit.addActionListener(event -> {
            String firstName = textFirstName.getText();
            firstName = WestminsterSkinConsultationManager.removeWhiteSpace(firstName);
            if (firstName.length() > 0) {
                firstName = WestminsterSkinConsultationManager.capitalise(firstName);
                firstName = WestminsterSkinConsultationManager.removeWhiteSpace(firstName);
            }
            // Corrects any mistakes in input from the user (not capitalised or space in middle of name)

            String lastName = textLastName.getText();
            if (lastName.length() > 0) {
                lastName = WestminsterSkinConsultationManager.capitalise(lastName);
                lastName = WestminsterSkinConsultationManager.removeWhiteSpace(lastName);
            }

            String tempDOB = day.getSelectedItem() + "-" + month.getSelectedItem()
                             + "-" + year.getSelectedItem();
            // Combines the values selected in the JComboBox's for DD, MM & YYYY into a string 
            // that can be parsed by LocalDateTime

            String mobileNum = textMobileNum.getText();
            mobileNum = WestminsterSkinConsultationManager.removeWhiteSpace(mobileNum);

            String patientId = textPatientId.getText();
            patientId = WestminsterSkinConsultationManager.removeWhiteSpace(patientId);

            try {
                String formattedDOB = WestminsterSkinConsultationManager.reformatDate(tempDOB);
                LocalDate dateOfBirth = LocalDate.parse(formattedDOB);
                
                // If/else statements to ensure user has input in each field and that the input meets requirements
                if (firstName.isEmpty()) {
                    JOptionPane.showMessageDialog(patientFrame, "Please enter a first name");
                }
                else if (lastName.isEmpty()) {
                    JOptionPane.showMessageDialog(patientFrame, "Please enter a surname");
                }
                else if (mobileNum.isEmpty()) {
                    JOptionPane.showMessageDialog(patientFrame, "Please enter a mobile number");
                }
                else if (mobileNum.length() < 11 | mobileNum.length() > 12 | !mobileNum.matches("[0-9]+")) {
                    JOptionPane.showMessageDialog(patientFrame, "Mobile number must be either 11 or 12 digits long"
                            + " and can only contain numbers");
                }
                else if (patientId.isEmpty()) {
                    JOptionPane.showMessageDialog(patientFrame, "Please enter a patient ID number");
                }
                else if (patientId.length() != 6 | !patientId.matches("[0-9]+")) {
                    JOptionPane.showMessageDialog(patientFrame, "Patient ID must be 6 digits long and can only contain numbers");
                }
                else if (uniquePatientId.contains(patientId)) {
                    JOptionPane.showMessageDialog(patientFrame, "This patient ID already exists\nPlease enter a different patient ID");
                }
                else {
                    Patient patient = new Patient(firstName, lastName, dateOfBirth, mobileNum, patientId, 0);
                    patientList.add(patient);
                    uniquePatientId.add(patientId);

                    String[] buttons = {"Yes", "No"};

                    int selection = JOptionPane.showOptionDialog(patientFrame,
                            "Patient added successfully, would you like to enter consultation details?", "Alert",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);

                    if (selection == JOptionPane.YES_OPTION) {
                        patientFrame.dispose();
                        consultFrame(uniqueBookingNum, doctorList, patientList, consultList, doctorId, patientId, consultYears);
                    }
                    else {
                        patientFrame.dispose();
                        doctorFrame.setVisible(true);
                    }
                }
            } 
            catch (DateTimeParseException | ArrayIndexOutOfBoundsException exc) {
                JOptionPane.showMessageDialog(patientFrame, "Please ensure date of birth fields are filled");
            }
        });
    }

    // Window for allowing user to enter all the necessary consultation details
    public void consultFrame(HashSet<String> uniqueBookingNum, ArrayList<Doctor> doctorList, ArrayList<Patient> patientList,
                                ArrayList<Consultation> consultList, String doctorId, String patientId, String[] consultYears) {
        consultFrame = new JFrame();
        JPanel panel = new JPanel();
        consultFrame.setTitle("Enter Consultation details");
        consultFrame.add(panel);
        panel.setLayout(null);

        JLabel enterDate = new JLabel("Enter consultation date (DD-MM-YYYY): ");
        enterDate.setBounds(10, 20, 230, 25);

        JComboBox<String> day = new JComboBox<>(DAYS);
        day.setBounds(250,20,50,25);

        JComboBox<String> month = new JComboBox<>(MONTHS);
        month.setBounds(310, 20, 50, 25);

        JComboBox<String> year = new JComboBox<>(consultYears);
        year.setBounds(370, 20, 90, 25);

        JLabel enterTime = new JLabel("Enter consultation time (HH-MM): ");
        enterTime.setBounds(10, 60, 230, 25);

        JComboBox<String> hour = new JComboBox<>(HOURS);
        hour.setBounds(250, 60, 50, 25);

        JComboBox<String> minute = new JComboBox<>(MINUTES);
        minute.setBounds(310, 60, 50, 25);

        JLabel enterBookingNum = new JLabel("Enter booking number: ");
        enterBookingNum.setBounds(10, 100, 230, 25);

        textBookingNum = new JTextField(10);
        textBookingNum.setBounds(250, 100, 165, 25);

        JLabel enterNotes = new JLabel("Enter text notes: ");
        enterNotes.setBounds(10, 140, 230, 25);

        textNotes = new JTextArea(10, 10);
        textNotes.setBounds(250, 140, 165, 160);
        textNotes.setLineWrap(true);
        textNotes.setWrapStyleWord(true);
        Border border = BorderFactory.createLineBorder(Color.GRAY);
            textNotes.setBorder(BorderFactory.createCompoundBorder(border,
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel enterImageNotes = new JLabel("Select an Image to Upload: ");
        enterImageNotes.setBounds(10, 330, 165, 25);

        JButton imageSelect = new JButton("Add Image");
        imageSelect.setBounds(250, 330, 100, 25);

        JLabel imageSelected = new JLabel("Image Uploaded: None");
        imageSelected.setBounds(10, 370, 300, 25);

        JButton submit = new JButton("Submit");
        submit.setBounds(190, 400, 100, 25);

        JLabel priceInfo = new JLabel("Special offer price of £15 for first visit, £25 afterwards");
        consultFrame.add(priceInfo, BorderLayout.SOUTH);

        panel.add(enterDate);
        panel.add(day);
        panel.add(month);
        panel.add(year);

        panel.add(enterTime);
        panel.add(hour);
        panel.add(minute);

        panel.add(enterBookingNum);
        panel.add(textBookingNum);
        panel.add(enterNotes);
        panel.add(textNotes);
        panel.add(enterImageNotes);
        panel.add(imageSelect);
        panel.add(imageSelected);

        panel.add(submit);

        consultFrame.setSize(500, 500);
        consultFrame.setResizable(false);
        consultFrame.setLocationRelativeTo(null);
        consultFrame.setVisible(true);
        consultFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        imageSelect.addActionListener(event -> {
            JFileChooser file = new JFileChooser();
            file.addChoosableFileFilter(new ImageFilter());
            file.setAcceptAllFileFilterUsed(false);

            int option = file.showOpenDialog(consultFrame);
            if (option == JFileChooser.APPROVE_OPTION) {
                File chosenFile = file.getSelectedFile();
                imageSelected.setText("Image Uploaded: " + chosenFile.getName());
            }
        });

        submit.addActionListener(event -> {
            boolean check = true;
            String backupDoctorId = "";
            double consultCost = 0;
            String consultBookingNum = textBookingNum.getText();
                if (consultBookingNum.isEmpty()) {
                    JOptionPane.showMessageDialog(consultFrame, "Please enter a booking number.");
                    return;
                }
                else if (consultBookingNum.length() != 6 | !consultBookingNum.matches("[0-9]+")) {
                    JOptionPane.showMessageDialog(consultFrame, "Booking number must be 6 digits long and contain only numbers");
                    return; // Checks that booking number is correct length and has no letters or special characters
                }
                else if (uniqueBookingNum.contains(consultBookingNum)) {
                    JOptionPane.showMessageDialog(consultFrame, "This booking number already exists, please try a different one");
                    return; // Checks the unique booking numbers hashset to see if there is any match with the entered booking number
                }
            String consultNotes = textNotes.getText();
                if (consultNotes.isEmpty()) {
                    consultNotes = "No Notes Added";
            }
            String[] tempImage = imageSelected.getText().split(": ");
            String consultImage = tempImage[1];
                if (!consultImage.equals("None")) {
                    try { // If an image has been selected, encrypts the string of the file path
                        consultImage = encryption(consultImage);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (!consultNotes.equals("No Notes Added")) {
                    try { // If user added notes in text area, encrypts the inputted data
                        consultNotes = encryption(consultNotes);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            
                LocalDateTime consultStart;
                String tempDate = day.getSelectedItem() + "-" + month.getSelectedItem()
                                  + "-" + year.getSelectedItem();
                String tempTime = hour.getSelectedItem() + ":" + minute.getSelectedItem();

                    try {
                        String formattedDate = WestminsterSkinConsultationManager.reformatDate(tempDate);

                        tempTime = tempTime.replace("-", ":");

                        consultStart = LocalDateTime.parse(formattedDate + "T" + tempTime);

                        Doctor initialiseDoc = new Doctor("Empty", "Empty", LocalDate.parse("2000-01-01"),
                                                            "Empty", "Empty", "Empty");
                        Patient initialisePat = new Patient("Empty", "Empty", LocalDate.parse("2000-01-01"),
                                                            "Empty", "Empty", 0);
                        Consultation initialiseConsult = new Consultation(initialiseDoc, initialisePat, "Debug",
                                        0, "Debug", "Debug", LocalDateTime.parse("2000-01-01" + "T" + "00:00"));
                        // Creates a "default" in arraylists to initialise it so that it can be checked in next section without throwing NullPointerException
                        Random rnd = new Random();
                        for (Doctor i : doctorList) {
                            if (i.getConsultation(consultBookingNum) == null) {
                                i.setConsultation(initialiseConsult);
                            }

                            if (i.getMedLicenseNo().equals(doctorId)) {
                                if (i.getConsultation(consultBookingNum).getStartDateTime().isEqual(consultStart)) {
                                    JOptionPane.showMessageDialog(consultFrame, "The selected doctor already has " +
                                            "a consultation booked for this time, another doctor will be selected for you");
                                    // If doctor is already booked for selected date/time, finds a random doctor as a replacement
                                    // Please note: I have some problems that occur from this that I was unable to figure out how to resolve
                                    // However, it does work most of the time from my testing
                                    while (check) {
                                        Doctor randomDoctor = doctorList.get(rnd.nextInt(doctorList.size()));

                                        if (randomDoctor.getMedLicenseNo().equals(doctorId)) {
                                            return;
                                        }
                                        if (randomDoctor.getConsultation(consultBookingNum) == null) {
                                            randomDoctor.setConsultation(initialiseConsult);
                                        }
                                        if (!randomDoctor.getConsultation(consultBookingNum).getStartDateTime().isEqual(consultStart)) {
                                            backupDoctorId = randomDoctor.getMedLicenseNo();
                                            JOptionPane.showMessageDialog(consultFrame, "Doctor " +
                                                    randomDoctor.getPersonFirstName() + " " + randomDoctor.getPersonLastName() +
                                                    " is available and has been assigned.");
                                            
                                            consultFrame.dispose();
                                            uniqueBookingNum.add(consultBookingNum);
                                            addConsult(doctorList, patientList, consultList, doctorId, backupDoctorId, patientId, consultBookingNum,
                                                    consultCost, consultNotes, consultImage, consultStart);
                                        }
                                        else {
                                            JOptionPane.showMessageDialog(consultFrame, "There are no doctors " +
                                                    "available at the desired date and time");
                                            consultFrame.dispose();
                                            consultFrame(uniqueBookingNum, doctorList, patientList, consultList, doctorId, patientId,
                                                    consultYears);
                                        }
                                        check = false;
                                    }
                                }
                                else {
                                    consultFrame.dispose();
                                    uniqueBookingNum.add(consultBookingNum);
                                    addConsult(doctorList, patientList, consultList, doctorId, backupDoctorId, patientId, consultBookingNum,
                                            consultCost, consultNotes, consultImage, consultStart);
                                }
                            }
                        }
                    }
                    catch (DateTimeParseException | ArrayIndexOutOfBoundsException exc) {
                        JOptionPane.showMessageDialog(consultFrame, "Please ensure all date & time fields have been filled");
                    }
        });
        consultFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                consultFrame.dispose();
                doctorFrame.setVisible(true);
            }
        });
    }

    // Gets all the info about the Doctor & Patient based on the stored medical license number and patient id
    public void addConsult(ArrayList<Doctor> doctorList, ArrayList<Patient> patientList, ArrayList<Consultation> consultList,
                           String doctorId, String backupDoctorId, String patientId, String consultBookingNum, double consultCost,
                           String consultNotes, String consultImage, LocalDateTime consultStart) {
        Doctor consultDoctor = new Doctor();
        Patient consultPatient = new Patient();

        // If loop finds a match on either the doctorId or backupDoctorId, adds matching doctor to consultation
        for (Doctor i : doctorList) { 
            if (i.getMedLicenseNo().equals(doctorId) || i.getMedLicenseNo().equals(backupDoctorId)) {
                consultDoctor = new Doctor(i.getPersonFirstName(), i.getPersonLastName(), i.getPersonDOB(),
                        i.getPersonMobileNo(), i.getMedLicenseNo(), i.getSpecialisation());
            }
        }

        // If loop finds match on patientId, adds matching patient to consultation
        for (Patient i : patientList) {
            if (i.getPatientID().equals(patientId)) {
                consultPatient = new Patient(i.getPersonFirstName(), i.getPersonLastName(), i.getPersonDOB(),
                        i.getPersonMobileNo(), i.getPatientID(), i.getNumOfConsults());
                // If first consultation booked by patient, price is £15.00
                if (i.getNumOfConsults() == 0) {
                    consultCost = 15;
                }
                // Else if patient has booked at least one consultation before, price is £25.00
                else if (i.getNumOfConsults() >= 1) {
                    consultCost = 25;
                }
                i.incrementConsults(); // Increments the numOfConsults on the specific Patient by 1
            }
        }
        // Adds the appropriate doctor, patient and consultation info to a consultation object 
        // and stores it into a list of consultations
        Consultation consultation = new Consultation(consultDoctor, consultPatient, consultBookingNum,
                consultCost, consultNotes, consultImage, consultStart);
        consultList.add(consultation);
        // Checks that the Doctor medical license number matches either the doctorId or backupDoctorId variable
        for (Doctor i : doctorList) {
            if (i.getMedLicenseNo().equals(doctorId) || i.getMedLicenseNo().equals(backupDoctorId)) {
                i.setConsultation(consultation);
            }
        }
        consultInfo(consultList, consultBookingNum);
    }

    // Window for displaying all the information on the Doctor, Patient and Consultation (Notes are encrypted on this window IF user added them)
    public void consultInfo(ArrayList<Consultation> consultList, String bookingNum) {
        consultInfoFrame = new JFrame();
        JPanel panel = new JPanel();
        consultInfoFrame.add(panel);
        consultInfoFrame.setTitle("Consultation Details");

        panel.setLayout(null);

        JLabel info = new JLabel("Consultation Information ");
        info.setFont(new Font("Helvetica", Font.BOLD, 14));
        info.setBounds(50, 10, 350, 25);

        for (Consultation i : consultList) {
            if (i.getConsultBooking().equals(bookingNum)) {
                JLabel docDetails = new JLabel("Doctor Details: ");
                docDetails.setBounds(50, 30, 100, 25);
                panel.add(docDetails);

                JLabel docName = new JLabel("Name: " + i.getDoctor().getPersonFirstName() + " " + i.getDoctor().getPersonLastName());
                docName.setBounds(50, 50, 200, 25);
                panel.add(docName);

                DateTimeFormatter formatObject = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDocDOB = i.getDoctor().getPersonDOB().format(formatObject);
                String formattedPatDOB = i.getPatient().getPersonDOB().format(formatObject);

                DateTimeFormatter formatConsult = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH:mm:ss");
                String formattedConsultTimeDate = i.getStartDateTime().format(formatConsult);

                JLabel docDOB = new JLabel("Date of Birth: " + formattedDocDOB);
                docDOB.setBounds(50, 65, 200, 25);
                panel.add(docDOB);

                JLabel docMobileNo = new JLabel("Mobile: " + i.getDoctor().getPersonMobileNo());
                docMobileNo.setBounds(50, 80, 200, 25);
                panel.add(docMobileNo);

                JLabel docMedNum = new JLabel("Medical No: " + i.getDoctor().getMedLicenseNo());
                docMedNum.setBounds(50, 95, 200, 25);
                panel.add(docMedNum);

                JLabel docSpec = new JLabel("Specialisation: " + i.getDoctor().getSpecialisation());
                docSpec.setBounds(50, 110, 200, 25);
                panel.add(docSpec);

                JLabel patDetails = new JLabel("Patient Details: ");
                patDetails.setBounds(250, 30, 100, 25);
                panel.add(patDetails);

                JLabel patName = new JLabel("Name: " + i.getPatient().getPersonFirstName() + " " + i.getPatient().getPersonLastName());
                patName.setBounds(250, 50, 200, 25);
                panel.add(patName);

                JLabel patDOB = new JLabel("Date of Birth: "+formattedPatDOB);
                patDOB.setBounds(250, 65, 200, 25);
                panel.add(patDOB);

                JLabel patMobileNo = new JLabel("Mobile: "+i.getPatient().getPersonMobileNo());
                patMobileNo.setBounds(250, 80, 200, 25);
                panel.add(patMobileNo);

                JLabel patId = new JLabel("Patient ID: "+i.getPatient().getPatientID());
                patId.setBounds(250, 95, 200, 25);
                panel.add(patId);

                JLabel consultDetails = new JLabel("Consultation Details: ");
                consultDetails.setBounds(50, 150, 300, 25);
                panel.add(consultDetails);

                JLabel consultDateTime = new JLabel("Consultation Time: "+formattedConsultTimeDate);
                consultDateTime.setBounds(50, 165, 300, 25);
                panel.add(consultDateTime);

                JLabel consultPrice = new JLabel("Price: £" + i.getConsultCost() + "0");
                consultPrice.setBounds(50, 180, 300, 25);
                panel.add(consultPrice);

                JLabel consultNoteText = new JLabel("Notes: " + i.getConsultNotes());
                consultNoteText.setBounds(50, 195, 600, 25);
                panel.add(consultNoteText);

                JLabel consultImagePath = new JLabel("Image File: " + i.getConsultImage());
                consultImagePath.setBounds(50, 210, 600, 25);
                panel.add(consultImagePath);

                JLabel consultBookingNum = new JLabel("Booking Number: " + i.getConsultBooking());
                consultBookingNum.setBounds(50, 225, 300, 25);
                panel.add(consultBookingNum);
            }
        }
        consultInfoFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                consultInfoFrame.dispose();
                doctorFrame.setVisible(true);
            }
        });

        panel.add(info);

        consultInfoFrame.setSize(500, 300);
        consultInfoFrame.setLocationRelativeTo(null);
        consultInfoFrame.setVisible(true);
        consultInfoFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }


     static class ImageFilter extends FileFilter {
        //Code referenced from https://www.tutorialspoint.com/swingexamples/show_file_chooser_images_only.htm
        public final static String JPEG = "jpeg";
        public final static String JPG = "jpg";
        public final static String PNG = "png";

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            // Only allows files of JPEG, JPG or PNG to be seen in file finder window
            String extension = getExtension(file);
            if (extension != null) {
                return extension.equals(JPEG) ||
                        extension.equals(JPG) ||
                        extension.equals(PNG);
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "Image Only";
        }

        String getExtension(File file) {
            String extensionName = null;
            String fileName = file.getName();
            int i = fileName.lastIndexOf('.');

            if (i > 0 && i < fileName.length() - 1) {
                extensionName = fileName.substring(i+1).toLowerCase();
            }
            return extensionName;
        }
    }

     // Code referenced and adapted from https://www.tutorialspoint.com/java_cryptography/java_cryptography_encrypting_data.htm
    private String encryption(String notes) {
        try {
            Signature encryptSignature = Signature.getInstance("SHA256withRSA");
            // Creates a KeyPairGenerator object that generates keys
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Initializes the KeyPairGenerator
            KeyPair pair = keyPairGenerator.generateKeyPair(); // Generates a pair of keys

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic()); 
            // Creates cipher object and then initialises it

            byte[] input = notes.getBytes(); //Adds the variable to the Cipher so that it can be encrypted.
            cipher.update(input);
            byte[] cipherText = cipher.doFinal(); // Encrypts the data and then returns it in encrypted form.
            return new String(cipherText, StandardCharsets.UTF_8);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
               BadPaddingException e) {
            System.out.println("Failed to Encrypt data");
        }
        return notes; // If try block fails, catches error and returns the notes string as it was passed to the method initially.
    }
}