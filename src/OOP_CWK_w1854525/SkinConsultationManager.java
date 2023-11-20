// Code written by Sam Clark
// Student ID - w1854525

package OOP_CWK_w1854525;

import java.util.ArrayList;
import java.util.HashSet;

public interface SkinConsultationManager {
public abstract void addDoctor(ArrayList<Doctor> doctorList, HashSet<String> uniqueNum);

public abstract void deleteDoctor(ArrayList<Doctor> doctorList, HashSet<String> uniqueNum);

public abstract void printDoctors(ArrayList<Doctor> doctorList);

public abstract void saveDoctors(ArrayList<Doctor> doctorList);

public abstract void loadDoctors(ArrayList<Doctor> doctorList);

public abstract void returnToMenu();
}
