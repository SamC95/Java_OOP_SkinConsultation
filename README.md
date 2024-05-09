# Skin Consultation Centre - Java Application
## Object Oriented Programming

By [Sam Clark](https://github.com/SamC95)

This project was coursework for my second year module: Object Oriented Programming -- at the University of Westminster.

## Contents
* [Project Brief](https://github.com/SamC95/Java_OOP_SkinConsultation/tree/master#project-brief)
* [Approach](https://github.com/SamC95/Java_OOP_SkinConsultation/tree/master#approach)
* [Technologies](https://github.com/SamC95/Java_OOP_SkinConsultation/tree/master#technologies)
* [Project Diagrams](https://github.com/SamC95/Java_OOP_SkinConsultation/tree/master#project-diagrams)
* [Implementation](https://github.com/SamC95/Java_OOP_SkinConsultation/tree/master#implementation)
* [Key Learnings](https://github.com/SamC95/Java_OOP_SkinConsultation/tree/master#key-learnings)
* [Achievements](https://github.com/SamC95/Java_OOP_SkinConsultation/tree/master#achievements)
* [Challenges](https://github.com/SamC95/Java_OOP_SkinConsultation/tree/master#challenges)
* [Conclusions](https://github.com/SamC95/Java_OOP_SkinConsultation/tree/master#conclusions)

## Project Brief

### Core Aims
* You are required to develop a program that implements a system to manage a Skin Consultation Centre.
* You should implement a console system from where the manager can add new doctors, delete if needed, print
and save them as described in detailed below.
* You should implement a Graphical User Interface (GUI) from where we can see the list of doctors, book or edit
consultations for patients, etc.
* For the user interface you are not allowed to use drag and drop tools (such as the Designer in NetBeans), and
you cannot use Java FX, but you can use some external API if you want to add graphs or some more
professional components.
* You are required to design your program using UML diagrams. In particular you have to draw:
A UML use case diagram for the system and a UML class diagram.
* According to the Inheritance principle you have to design and implement a super class Person and the subclasses
Doctor and Patient.

### Expected Class & Interface Implementation
* The Doctor subclass should hold specific information and methods. You should add the medical
licence number and the specialisation (e.g. cosmetic dermatology, medical dermatology, paediatric
dermatology, etc.) as instance variables and the relative get/set methods.
* The Patient subclass should hold specific information and methods. You should add a patient unique
id as instance variables (attribute) and the relative get/set methods.
* Design and implement a class called WestminsterSkinConsultationManager, which implements the
interface SkinConsultationManager. WestminsterSkinConsultationManager maintains the
list of the doctors and provides all the methods for the system manager.

### Console Menu Implementation
* The class WestminsterSkinConsultationManager should display in the console a menu, containing the
following management actions from which the user can select one.
* Add a new doctor in the system. It should be possible to add a new doctor, with all the relevant
information. You should consider that the centre can allocate a maximum of 10 doctors.
* Delete a doctor from the system, selecting the medical licence number. Display a message with the
information of the doctor that has been deleted and the total number of doctors in the centre.
* Print the list of the doctors in the consultation centre. For each doctor, print on the screen all the
stored information. The list should be ordered alphabetically according to the doctor surname.
* Save in a file all the information entered by the user so far. The next time the application starts it
should be able to read back all the information saved in the file and continue to use the system.

### Graphical User Interface (GUI) Implementation
* Open a Graphical User Interface (GUI) from the menu console.
* The user can visualise the list of doctors with relative information. The user should be able to
sort the list alphabetically. You are suggested to use a table to display this information on the
GUI but you can choose any other solution.

* The user can select a doctor and add a consultation with that specific doctor. When
implementing these functionalities, you need to comply with the following requirements:
* The user can check the availability of the doctor in specific date/time and can book
a consultation for a patient if the doctor is available. If the doctor is not available
automatically another doctor will be allocated, who is available in that specific
date/time. The choice of the doctor has to be done randomly among all available
doctors.

For each consultation the user has to:
* Add patient information (add all the attributes defined above - name, surname, date
of birth, mobile number, id).
* Display and save the cost for the consultation. Consider that each consultation is
£25 per hour and the first consultation in the center is £15 per hour.
* Add some notes (this could be textual information or the user could upload some
images of the skin). This information should be encrypted in order to preserver data
privacy. You can use available APIs for the encryption of data.
* Once the consultation has been saved in the system, the user can select it and
visualise all the stored information.

## Approach

The approach I took with the project was to initially set up all the expected classes (Person, Patient, Doctor, etc.) and the WestminsterSkinConsultationManager class and associated SkinConsultationManager Interface.

I opted to first focus on implementing the console menu portion of the requirements and getting this working as required. This included the functionality to add and delete doctors. As well as show an alphabetical list of currently stored doctors and the ability to save and load a list of doctors from a text file.

Afterwards. I implemented the GUI section of the project with all the expected functionality using Java Swing. Which includes the ability to book consultation appointments as a patient with the specified requirements from the project brief.

## Technologies

<p>
  <img src="https://github.com/SamC95/Java_OOP_SkinConsultation/assets/132593571/797d87c0-92c2-4f2d-b8bc-953f67b8eff8">
  <img src="https://github.com/SamC95/Java_OOP_SkinConsultation/assets/132593571/84bcad9c-1006-457d-a4e0-86e38013c164">
</p>

* Java
* Java Swing
* IntelliJ

## Project Diagrams

The project included the creation of UML diagrams to define the expected use cases and class interactions. These are as shown below:

### UML Use Case Diagram

<img src="https://github.com/SamC95/Java_OOP_SkinConsultation/assets/132593571/dc5b58b7-a1e0-47fe-a66f-c588e85f696c" width="750" />

### UML Class Diagram

<img src="https://github.com/SamC95/Java_OOP_SkinConsultation/assets/132593571/e45af424-d529-478f-87d2-e467f5ba0e1e" width="750" />

## Implementation

This project taught me several new concepts that I had not had experience with before, likely the largest of these other than general OOP principles would be that it was the first time I utilised a Graphical User Interface in a program.

Below is an example of the code for showing an initial GUI frame showing a list of doctors that are stored on the system that the user can select from.

```java
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
            } 
        };
        
        for (Doctor doctor : doctorList) {
            String name = doctor.getPersonFirstName() + " " + doctor.getPersonLastName();
            LocalDate DOB = doctor.getPersonDOB();
            String mobileNum = doctor.getPersonMobileNo();
            String medNum = doctor.getMedLicenseNo();
            String role = doctor.getSpecialisation();

            if (role.contains("#")) {
                role = role.replace("#", " ");
            }
            Object[] tableData = {name, DOB, mobileNum, medNum, role};
            tableModel.addRow(tableData);
        }

        JLabel label = new JLabel("Select a doctor to book a consultation.");
        doctorFrame.add(label, BorderLayout.SOUTH);

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
```

## Key Learnings
* Learnt to utilise Object-Oriented Programming principles using Objects, Classes and Interfaces.
* Implemented a Graphical User Interface for the first time.
* First experience with Java Swing.
* Used UML Diagrams for the first time.
* Gained more experience with writing and reading from text files.
* This was one of the larger scale projects I had worked on at the time, as such it allowed me to improve my general knowledge of Java.
* Gained some experience with utilising test cases.

## Achievements
* Implemented all the expected features and functionality defined in the project brief.
* Was able to effectively implement OOP techniques, which was the core goal of the module project.

## Challenges
* My UML Diagrams had some errors, could have been better defined and a little bit better organised.
* I had some challenges related to the implementation of the GUI, I feel that I could definitely have handled it more efficiently.

## Conclusions
This project taught me to utilise several critically important aspects of software development; such as OOP principles, basic project planning and testing, graphical user interfaces and saving to and loading from text files.

Since this was one of the larger projects I had worked on at the time, it was also one of the first that I utilised multiple separate classes and had communication/interaction between them which presented a unique challenge at the time. I feel looking back on the project now after having worked on several projects afterwards, that I definitely could refine aspects of the project to be more efficiently designed but overall the project met the defined goals as per the project brief without any significant bugs or other issues.
