// Code written by Sam Clark
// Student ID - w1854525

package OOP_CWK_w1854525;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

public class WestminsterSkinConsultationManager implements SkinConsultationManager {
    static Scanner scan = new Scanner(System.in);

    /*Checks if inputted medical ID already exists for another doctor,
    if so prevents it from being added, asks user to enter a different ID*/
    public String ifUniqueId(HashSet<String> uniqueNum, String uniqueId) {
        while (uniqueNum.contains(uniqueId)) {
            System.out.println("This ID number has already been assigned, please try again.");
            uniqueId = scan.nextLine();
        }
        uniqueNum.add(uniqueId);
        return uniqueId;
    }

    // Reformats user input of DD-MM-YYYY to YYYY-MM-DD for LocalDate to parse it
    public static String reformatDate(String tempDOB) {
        String[] splitDOB = tempDOB.split("-");
        String temp = splitDOB[0];
        splitDOB[0] = splitDOB[2];
        splitDOB[2] = temp;

        tempDOB = String.join("-", splitDOB);
        return tempDOB;
    }
    
    // Removes any white space present on a specific string
    public static String removeWhiteSpace(String data) {
        if (data.contains(" ")) {
            data = data.replace(" ", "");
        }
        return data;
    }
    
    // First character of string to uppercase, anything after to lowercase 
    public static String capitalise(String data) {
        data = data.toLowerCase();
        data = data.substring(0, 1).toUpperCase() + data.substring(1);
        return data;
    }

    public void addDoctor(ArrayList<Doctor> doctorList, HashSet<String> uniqueNum) {
        try {
            if (doctorList.size() < 10) {
                System.out.println("Please enter the following details: ");
                
                System.out.println("First Name: ");
                String docFirstName = scan.next(); //Scans only next word and stores it, anything after space is discarded
                while (!docFirstName.matches("[a-zA-Z]+")) {
                    System.out.println("First Name cannot contain numbers or special characters");
                    System.out.println("Please try again");
                    docFirstName = scan.next();
                }
                
                docFirstName = capitalise(docFirstName); //uppercase on first character of string, rest lowercase
                scan.nextLine();

                System.out.println("Last Name: ");
                String docSurname = scan.next();
                while (!docSurname.matches("[a-zA-Z]+")) {
                    System.out.println("Surname cannot contain numbers or special characters");
                    System.out.println("Please try again");
                    docSurname = scan.next();
                }
                docSurname = capitalise(docSurname);
                scan.nextLine();

                System.out.println("Date of Birth (DD-MM-YYYY): ");
                String unformattedDOB = scan.nextLine();

                unformattedDOB = reformatDate(unformattedDOB); //Calls function to change date from DD-MM-YYYY to YYYY-MM-DD
                LocalDate docDOB = LocalDate.parse(unformattedDOB); //Takes string and parses it into Java localDate class

                System.out.println("Mobile Number: ");
                String docMobileNum = scan.nextLine();

                docMobileNum = removeWhiteSpace(docMobileNum);

                while (docMobileNum.length() < 11 | docMobileNum.length() > 12 | !docMobileNum.matches("[0-9]+")) { 
                //Ensures mobile number input is not too short ot long, and only contains numbers
                    System.out.println("Mobile number must be 11 or 12 digits long and can only contain numbers");
                    System.out.println("Please try again: ");
                    docMobileNum = scan.nextLine();
                }
                
                System.out.println("Medical ID Number: ");
                String docMedNum = scan.nextLine();
                docMedNum = removeWhiteSpace(docMedNum);
                docMedNum = ifUniqueId(uniqueNum, docMedNum);
                
                while (docMedNum.length() != 4 | !docMedNum.matches("[0-9]+")) {
                // Ensures correct length of medical number and that it only contains numbers
                    System.out.println("Medical License number must be 4 digits long and can only contain numbers");
                    System.out.println("Please try again: ");
                    docMedNum = scan.nextLine();
                }

                System.out.println("Specialised Role: ");
                String docSpec = scan.nextLine();
                while (!docSpec.matches("[a-zA-Z ]+")) {
                    System.out.println("Specialisation cannot contain numbers or special characters");
                    System.out.println("Please try again");
                    docSpec = scan.nextLine();
                }
                
                if (docSpec.contains(" ")) {
                    docSpec = docSpec.replace(" ", "#");
                } // Places # symbol in whitespace for data storage, ensures saving/loading works correctly with chosen implementation
                
                docSpec = capitalise(docSpec); //Ensures that first word is capitalised, any other words are not capitalised

                Doctor doctor = new Doctor(docFirstName, docSurname, docDOB, docMobileNum, docMedNum, docSpec);
                doctorList.add(doctor); //Adds new doctor onto arraylist for storing
                System.out.println(docFirstName + " " + docSurname + " has been successfully added to system. \n");
            }
            else {
                System.out.println("List of Doctors is currently full. \n");
            }
        }
        catch (DateTimeParseException | ArrayIndexOutOfBoundsException exc) {
            System.out.println("Date must be in the format: 'DD-MM-YYYY' and must be a valid calendar date\n");
            System.out.println(); //Catches error if user input after formatting does not comply with ISO-8601 standards
        }
        returnToMenu();
    }

    //Checks if a match is found between a user inputted id number and the medical numbers stored for doctors on the system.
    public static boolean containsIDNum(final ArrayList<Doctor> doctorList, final String idNum) {
        return doctorList.stream().anyMatch(doctor -> idNum.equals(doctor.getMedLicenseNo()));
    }

    public void deleteDoctor(ArrayList<Doctor> doctorList, HashSet<String> uniqueNum) {
        if (doctorList.isEmpty()) {
            System.out.println("There are currently no doctors on the system. \n");
        }

        else {
            System.out.println("Enter the medical number of the Doctor you wish to delete from the system.");
            String inputMedNo = scan.nextLine();
            // Uses medical number as a basis for deletion, since it is a unique identifier with no duplicates
            if (containsIDNum(doctorList, inputMedNo)) {
                for (Doctor i : doctorList) {
                    if (i.getMedLicenseNo().equals(inputMedNo)) {
                        String tempName = "\nDoctor by the name of " + i.getPersonFirstName() + " " + i.getPersonLastName()
                                + " has been removed from the system.\n";
                        System.out.println(tempName);
                    }
                }
                doctorList.removeIf(doctor -> (inputMedNo.equals(doctor.getMedLicenseNo())));
                uniqueNum.remove(inputMedNo);
                // Removes the doctor and also removes that doctor's medical id number from HashSet of existing med license nums
                System.out.println("There are now " + doctorList.size() + " Doctors stored. \n");
            }
            else {
                System.out.println("No doctor found by that ID number. \n");
            }
        }
        returnToMenu();
    }

    public void printDoctors(ArrayList<Doctor> doctorList) {
        // Temporary arraylist to hold the doctor list so that it doesn't get permanently sorted
        ArrayList<Doctor> tempList = new ArrayList<>(doctorList);
        if (doctorList.isEmpty()) {
            System.out.println("There are currently no doctors on the system. \n");
        }
        else {
            System.out.println("List of Doctors on system: \n");

            // Sorts the data based on the doctor surnames
            tempList.sort((Comparator.comparing(Person::getPersonLastName)));

            for (Doctor doctor : tempList) {
                String formattedList = doctor.toString();
                System.out.println(formattedList.replace("[", "").replace("]", "")
                                                        .replace("#", " "));
                System.out.println(); 
                // Replaces brackets and # with appropriate spacing, and prints an extra line to separate doctors
            }
        }
        returnToMenu();
    }

    public void saveDoctors(ArrayList<Doctor> doctorList) {
        if (doctorList.isEmpty()) {
            System.out.println("There are no doctors stored to be saved. \n");
        }

        else {
            try {
                FileWriter saveData = new FileWriter("doctorList.txt");
                Writer out = new BufferedWriter(saveData);

                for (int i = 0; i < doctorList.size(); i++) {
                    if (doctorList.get(i).getSpecialisation().contains(" ")) {
                        String docSpec = doctorList.get(i).getSpecialisation().replace(" ", "#");
                        doctorList.get(i).setSpecialisation(docSpec);
                        // Ensures that specialisation has # in white space when saving to prevent any errors whilst loading
                    }
                    out.write(doctorList.get(i).toString() + "\n");
                }
                out.close();

                System.out.println("\nData saved to file.\n");
            }
            catch (IOException exc) {
                exc.printStackTrace();
            }
            returnToMenu();
        }
    }

    public void loadDoctors(ArrayList<Doctor> doctorList) {
        String userInput = "Y";
        if (doctorList.size() > 0) {
            // Informs user that any entered data in this session prior to loading will be replaced if load operation is performed
            System.out.println("There is currently data stored on the system, would you like to replace this with loaded data?" +
                    "\nType 'Y' to Load or 'N' to cancel Loading.");
            userInput = scan.nextLine();

            while (!Objects.equals(userInput, "Y")) {

                if (Objects.equals(userInput, "N")) {
                    System.out.println("Cancelling loading..");
                    break;
                }
                else {
                    System.out.println("Input must be 'Y' to load the file or 'N' to abort loading.");
                    userInput = scan.nextLine();
                }
            }
        }
        if (userInput.equals("Y")) {
            doctorList.clear();
            int count = 0;
            try {
                Scanner readFile = new Scanner(new FileReader("doctorList.txt"));
                List<String> fileContents = new ArrayList<>();
                List<String> doctorInfo = new ArrayList<>();

                while (readFile.hasNextLine()) {
                    String line = readFile.nextLine();

                    if (!line.equals("")) {
                        // Splits each line to only retrieve the actual data, discarding the data identifier (e.g. "Name - ")
                        String[] tempArray = line.split("- ");
                        String discarded = tempArray[0];
                        String data = tempArray[1];

                        if (data.contains(" ")) {
                            // Splits name back into first name and surname
                            String[] dataSplitter = data.split(" ");
                            fileContents.add(dataSplitter[0]);
                            fileContents.add(dataSplitter[1]);
                        }
                        else if (data.contains("#")) {
                            // Removes the # from Specialisation for printing or GUI options, when saving this # will be placed again
                            data = data.replace("#", " ");
                            fileContents.add(data);
                        }
                        else {
                            fileContents.add(data);
                        }
                    }
                }
                for (String fileContent : fileContents) {
                    if (count < 5) {
                        doctorInfo.add(fileContent);
                        count++;
                    }
                    else if (count == 5) {
                        doctorInfo.add(fileContent);
                        String date = doctorInfo.get(2);
                        String formattedDate = reformatDate(date); // Sets date to appropriate layout for LocalDate parsing
                        LocalDate loadDate = LocalDate.parse(formattedDate);
                        Doctor doctor = new Doctor(doctorInfo.get(0), doctorInfo.get(1), loadDate,
                                                    doctorInfo.get(3), doctorInfo.get(4), doctorInfo.get(5));
                        doctorList.add(doctor);
                        count = 0;

                        for (int i = 0; i < doctorInfo.size(); i+=0) {
                            doctorInfo.remove(i);
                        }
                    }
                }
                System.out.println("Information successfully loaded.");
            }
            catch (FileNotFoundException exc) {
                System.out.println("No file found.");
            }
        }
        returnToMenu();
    }

    public void returnToMenu() {
        System.out.println("Press 'Enter' to return to main menu.");
        scan.nextLine();
    }

    public String closeProgram() {
        System.out.println("Are you sure you want to exit? 'Y' to Exit or 'N' to return to menu.");
        String menuLoop = scan.nextLine();
        menuLoop = menuLoop.toUpperCase();

        while (!Objects.equals(menuLoop, "N")) {

            if (Objects.equals(menuLoop, "Y")) {
                System.out.println("Closing Program...");
                break;
            }
            else {
                System.out.println("Input must be 'Y' or 'N'");
                menuLoop = scan.nextLine();
            }
        }
        return menuLoop;
    }

    public static void main(String[] args) {
        // Creates all the necessary arraylists & hashsets to store required information
        ArrayList<Doctor> doctorList = new ArrayList<>();
        ArrayList<Patient> patientList = new ArrayList<>();
        ArrayList<Consultation> consultList = new ArrayList<>();
        HashSet<String> uniqueMedNum = new HashSet<>();
        HashSet<String> uniquePatientId = new HashSet<>();
        HashSet<String> uniqueBookingNum = new HashSet<>();

        // Initialises menu
        String menuLoop = "N";
        WestminsterSkinConsultationManager menu = new WestminsterSkinConsultationManager();

        while (menuLoop.equals("N")) {

            System.out.println("Please select an option from the menu:");
            System.out.println("A: Add a Doctor");
            System.out.println("D: Delete a Doctor");      
            System.out.println("P: Display a list of Doctors");      
            System.out.println("S: Save data to file");       
            System.out.println("L: Load data from file");       
            System.out.println("M: Open GUI");     
            System.out.println("C: Close the program");       

            // Reads menu input and converts it to uppercase to 
            // prevent lowercase inputs of correct letters not being recognised
            String menuOption = scan.nextLine();
            menuOption = menuOption.toUpperCase();

            switch (menuOption) {
                case "A": 
                    menu.addDoctor(doctorList, uniqueMedNum);
                    break;
                case "D":
                    menu.deleteDoctor(doctorList, uniqueMedNum);
                    break;
                case "P": 
                    menu.printDoctors(doctorList);
                    break;
                case "S": 
                    menu.saveDoctors(doctorList);
                    break;
                case "L": 
                    menu.loadDoctors(doctorList);
                    break;
                case "M":
                    if (doctorList.size() < 1) {
                        System.out.println("There must be doctors stored on the system to be able to open the GUI");
                        menu.returnToMenu();
                        break;
                    }
                    else {
                        new GUI(doctorList, patientList, consultList, uniquePatientId, uniqueBookingNum);
                        menu.returnToMenu();
                        break;
                    }
                case "C": 
                    menuLoop = menu.closeProgram();
                    break;
                }
            }
        }
    }