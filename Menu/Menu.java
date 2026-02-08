package Menu;

import Assignments.AssignmentMenuManager;
import Attendance.AttendanceManager;
import Course.CourseMenuManager;
import DataManager.DataManager;
import Main.Entry;
import Role.RoleUtil;
import Student.StudentMenuManager;
import Teacher.TeacherMenuManager;
import User.User;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    private final Entry entryApp;
    private User currentUser;
    private final StudentMenuManager studentManager;
    private final CourseMenuManager courseManager;
    private final AssignmentMenuManager assignmentManager;
    private final TeacherMenuManager teacherManager;
    private final AttendanceManager attendanceManager;  

    // Constructor
    public Menu(Entry entryApp) {
        this.scanner = new Scanner(System.in);
        this.entryApp = entryApp;
        this.studentManager = new StudentMenuManager(scanner, entryApp);
        this.courseManager = new CourseMenuManager(scanner, entryApp);
        this.assignmentManager = new AssignmentMenuManager(scanner, entryApp);
        this.teacherManager = new TeacherMenuManager(scanner, entryApp);
        this.attendanceManager = new AttendanceManager(scanner, entryApp);
    }
    // Start the menu loop
    public void start() throws Exception {
        // Load existing data
        try (scanner) {
            // Load existing data
            DataManager.loadAllData();
            // Initialize data if none exists
            if (Entry.courseMap.isEmpty() && Entry.studentMap.isEmpty() && Entry.assignmentMap.isEmpty()) {
                Entry.initAllData();
            }

            if (!login()) {
                    System.out.println("Login cancelled. Exiting...");
                    scanner.close();
                    return;
            }
            
            boolean isMenuRunning = true;
            // Main menu loop
            while (isMenuRunning) {
                // display menu
                displayMainMenu();
                // get user choice
                System.out.println("Enter your choice: ");
                String choice = scanner.nextLine().trim();
                String mappedChoice = mapInputToAction(choice, currentUser.getRole());
                // Handle menu choices
                try {
                switch (mappedChoice) {
                    case "View Student Info", "1" -> studentManager.viewStudentInfo(currentUser);
                    case "View Course Info", "2" -> courseManager.viewCourseInfo(currentUser);
                    case "View Assignment Info", "3" -> assignmentManager.viewAssignmentInfo(currentUser);
                    case "Add New Student", "4" -> studentManager.addNewStudent(currentUser);
                    case "Add New Course", "5" -> courseManager.addNewCourse(currentUser);
                    case "Add New Assignment", "6" -> assignmentManager.addNewAssignment(currentUser);
                    case "Edit Assignment Score", "7" -> assignmentManager.editAssignmentScore(currentUser);
                    case "View Class Roster", "8" -> courseManager.viewClassRoster(currentUser);
                    case "View Assignment By Due Date", "9" -> assignmentManager.viewAssignmentsByDueDate();
                    case "Delete Student", "10" -> studentManager.deleteStudent(currentUser);
                    case "Delete Course", "11" -> courseManager.deleteCourse(currentUser);
                    case "Delete Assignment", "12" -> assignmentManager.deleteAssignment(currentUser);
                    case "Enroll Student in Course", "13" -> studentManager.enrollStudentInCourse(currentUser);
                    case "Generate Student Report", "14" -> studentManager.generateStudentReport(currentUser);
                    case "Generate Course Report", "15" -> courseManager.generateCourseReport(currentUser);
                    case "Drop Student from Course", "16" -> studentManager.dropStudentFromCourse(currentUser);
                    case "Assign Teacher to Course", "17" -> teacherManager.assignTeacherToCourse(currentUser);
                    case "Remove Teacher from Course", "18" -> teacherManager.removeTeacherFromCourse(currentUser);
                    case "View Teacher's Courses", "19" -> teacherManager.viewTeacherCourses(currentUser);
                    case "List All Teachers", "20" -> teacherManager.listAllTeachers(currentUser);
                    case "Edit Student Details", "21" -> studentManager.editStudentDetails(currentUser);
                    case "View Detailed Student Info", "22" -> studentManager.viewDetailedStudentInfo(currentUser);
                    case "View Submission Status", "23" -> studentManager.viewSubmissionStatus(currentUser);
                    case "Mark Assignment Missing", "24" -> studentManager.markAssignmentMissing(currentUser);
                    case "View Missing Assignments", "25" -> assignmentManager.viewMissingAssignments(currentUser);
                    case "Submit Assignment", "26" -> studentManager.submitAssignment(currentUser);
                    case "Take Attendance", "27" -> attendanceManager.takeAttendance(currentUser);
                    case "View Attendance", "28" -> attendanceManager.viewAttendance(currentUser);
                    case "Generate Attendance Report", "29" -> attendanceManager.generateAttendanceReport(currentUser);
                    case "Save and Exit", "30" -> {
                        DataManager.saveAllData(Entry.courseMap, Entry.studentMap, Entry.assignmentMap, Entry.attendanceMap);
                        isMenuRunning = false;
                        System.out.println("Data saved. Exiting application.");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } 
            catch (IllegalArgumentException e) {
                System.out.println("Invalid argument");
            }
                // Pause before showing menu again
                if (isMenuRunning) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            }
        }
    }
    // Display the main menu options based on permission level
    public void displayMainMenu() {
        switch (currentUser.getRole()) {
            case ADMIN -> displayAdminMenu();
            case TEACHER -> displayTeacherMenu();
            case STUDENT -> displayStudentMenu();
            case PARENT -> displayParentMenu();
        }
    }

    private boolean login() throws Exception {
        while (true) { 
            System.out.println("\n=== Welcome to the Student Management Portal ===");
            System.out.println("---------------------------------------------------");
            System.out.println("\n=== Login ===");
            System.out.println("---------------------------------------------------");
            System.out.println("Enter your username: ");
            String username = scanner.nextLine().trim();
            System.out.println("Enter your password: ");
            System.out.println("---------------------------------------------------");
            String password = scanner.nextLine().trim();

            // validate user credentials 
            User result = Entry.authentication.login(username, password);

            if (result != null) {
                currentUser = result;
                System.out.println("Welcome: " + currentUser.getUsername());
                return true;
            }
            else {
                System.out.println("Invalid credentials");
                System.out.println("Would you like to re-enter your credentials (yes/no): ");
                String reentrance = scanner.nextLine().trim();
                    if (!reentrance.equalsIgnoreCase("yes") && !reentrance.equalsIgnoreCase("y")) {
                        return false;
                    }
                }
            }
        }

    private static void displayAdminMenu() {
        System.out.println("\n=== Main Menu (Administration) ===");
        System.out.println("-----------------------------------");
        System.out.println("Options:");
        System.out.println("1. View Student Info");
        System.out.println("2. View Course Info");
        System.out.println("3. View Assignment Info");
        System.out.println("4. Add New Student");
        System.out.println("5. Add New Course");
        System.out.println("6. Add New Assignment");
        System.out.println("7. Edit Assignment Score");
        System.out.println("8. View Class Roster");
        System.out.println("9. View Assignment By Due Date");
        System.out.println("10. Delete Student");
        System.out.println("11. Delete Course");
        System.out.println("12. Delete Assignment");
        System.out.println("13. Enroll Student in Course");
        System.out.println("14. Generate Student Report");
        System.out.println("15. Generate Course Report");
        System.out.println("16. Drop Student from Course");
        System.out.println("17. Assign Teacher to Course");        
        System.out.println("18. Remove Teacher from Course");      
        System.out.println("19. View Teacher's Courses");          
        System.out.println("20. List All Teachers");               
        System.out.println("21. Edit Student Details");
        System.out.println("22. View Detailed Student Info");
        System.out.println("23. Submit Assignment");
        System.out.println("24. View Submission Status");
        System.out.println("25. Mark Assignment Missing");
        System.out.println("26. View Missing Assignments");
        System.out.println("27. Take Attendance");
        System.out.println("28. View Attendance");
        System.out.println("29. Generate Attendance Report");
        System.out.println("30. Save and Exit");
    }

    private static void displayTeacherMenu() {
        System.out.println("\n=== Main Menu (Teacher) ===");
        System.out.println("-----------------------------");
        System.out.println("Options:");
        System.out.println("1. View Assignment Info");
        System.out.println("2. Add New Assignment");
        System.out.println("3. Edit Assignment Score");
        System.out.println("4. View Class Roster");
        System.out.println("5. View Assignment By Due Date");
        System.out.println("6. Delete Assignment");
        System.out.println("7. Enroll Student in Course");
        System.out.println("8. Generate Course Report");
        System.out.println("9. View My Courses");
        System.out.println("10. View Submission Status");
        System.out.println("11. Mark Assignment Missing");
        System.out.println("12. View Missing Assignments");
        System.out.println("13. Drop Student from Course");
        System.out.println("14. Take Attendance");
        System.out.println("15. View Attendance");
        System.out.println("16. Generate Attendance Report");
        System.out.println("17. Save and Exit");
    }

    private static void displayStudentMenu() {
        System.out.println("\n=== Main Menu (Student) ===");
        System.out.println("-----------------------------");
        System.out.println("Options:");
        System.out.println("1. View Student Info");
        System.out.println("2. View Course Info");
        System.out.println("3. View Assignment Info");
        System.out.println("4. View Assignment By Due Date");
        System.out.println("5. Generate My Report");
        System.out.println("6. Submit Assignment");
        System.out.println("7. View My Submissions");
        System.out.println("8. View Detailed Info");
        System.out.println("9. View My Attendance");
        System.out.println("10. Save and Exit");
    }

    private static void displayParentMenu() {
        System.out.println("\n=== Main Menu (Parent) ===");
        System.out.println("-----------------------------");
        System.out.println("Options:");
        System.out.println("1. View Student Info");
        System.out.println("2. View Course Info");
        System.out.println("3. View Assignment Info");
        System.out.println("4. View Submission Status");
        System.out.println("5. Generate Child's Report");
        System.out.println("6. Save and Exit");
    }

    private String mapInputToAction(String userInput, RoleUtil.Role role) {
        // Admin uses original numbering
        if (role == RoleUtil.Role.ADMIN) { return userInput; }
    
        // Teacher mapping: renumbered -> original
        if (role == RoleUtil.Role.TEACHER) {
            return switch (userInput) {
                case "1", "View Assignment Info" -> "3";
                case "2", "Add New Assignment" -> "6"; 
                case "3", "Edit Assignment Score" -> "7";  
                case "4", "View Class Roster" -> "8"; 
                case "5", "View Assignment By Due Date" -> "9";  
                case "6", "Delete Assignment" -> "12"; 
                case "7", "Enroll Student in Course" -> "13";
                case "8", "Generate Course Report" -> "15";
                case "9", "Drop Student from Course" -> "16"; 
                case "10", "View My Courses" -> "19";
                case "11", "View Submission Status" -> "23";
                case "12", "Mark Assignment Missing" -> "24";
                case "13", "View Missing Assignments" -> "25";
                case "14", "Take Attendance" -> "27";
                case "15", "View Attendance" -> "28";
                case "16", "Generate Attendance Report" -> "29";
                case "17", "Save and Exit" -> "30";
                default -> userInput; // Pass through text input
            };
        }
    
        // Student mapping: renumbered -> original
        if (role == RoleUtil.Role.STUDENT) {
            return switch (userInput) {
                case "1", "View Student Info" -> "1"; 
                case "2", "View Course Info" -> "2";  
                case "3", "View Assignment Info" -> "3";
                case "4", "View Assignment By Due Date" -> "9"; 
                case "5", "Generate My Report" -> "14"; 
                case "6", "Submit Assignment" -> "26";
                case "7", "View My Submissions" -> "23";
                case "8", "View My Detailed Info" -> "22"; 
                case "9", "View My Attendance" -> "28";
                case "10", "Save and Exit" -> "30"; 
                default -> userInput;
            };
        }
    
        // Parent mapping: renumbered -> original
        if (role == RoleUtil.Role.PARENT) {
            return switch (userInput) {
                case "1", "View Student Info" -> "1";  
                case "2", "View Course Info" -> "2";
                case "3", "View Assignment Info" -> "3";
                case "4", "Generate Child's Grade Report" -> "14";
                case "5", "View Child's Submission Status" -> "23";
                case "6", "Save and Exit" -> "30";
                default -> userInput;
            };
        }
    
        return userInput;
    }
}
