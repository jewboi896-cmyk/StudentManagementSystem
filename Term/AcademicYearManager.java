package Term;

import Main.Main;
import Report.ReportGenerator;
import Role.RoleUtil;
import User.User;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AcademicYearManager {
    private final Scanner scanner;
    private final Main mainApp;

    public AcademicYearManager(Scanner scanner, Main mainApp) {
        this.scanner = scanner;
        this.mainApp = mainApp;
    }
    // only admins can do this
    public void createAcademicYear(User currentUser) {
        System.out.println("\n=== Create Academic Year ===");

        // validate permissions
        if (!currentUser.getRole().equals(RoleUtil.Role.ADMIN)) {
            System.out.print("Error: You do not have permission to create academic years.");
            return;
        }

        System.out.print("Enter Year: ");
        String yearInput = scanner.nextLine().trim();
        int year;
        try {
            year = Integer.parseInt(yearInput);
        } catch (NumberFormatException e) {
            System.out.print("Error: Invalid year. Please enter a number.");
            return;
        }

        // make sure that year isn't negative
        if (year < 0) {
            System.out.print("Error: A year cannot be negative.");
            return;
        }

        // validate year start date
        LocalDate yearStartDate = getDateInput("Enter year start date (YYYY-MM-DD): ");
        if (yearStartDate == null) {
            return;
        }

        // validate year-end date
        LocalDate yearEndDate = getDateInput("Enter year end date (YYYY-MM-DD): ");
        if (yearEndDate == null) {
            return;
        }
        // check if end date is before start date
        if (!yearEndDate.isAfter(yearStartDate)) {
            System.out.print("Error: Year end date must be after the start date.");
            return;
        }
        // check if start date year and the entered year is the same
        if (yearStartDate.getYear() != year) {
            System.out.print("Error: Start date year must match the entered year.");
            return;
        }

        String yearName = year + "-" + (year + 1);
        // check if year is a duplicate
        if (Main.yearMap.containsKey(yearName)) {
            System.out.print("Error: Academic year '" + yearName + "' already exists.");
            return;
        }
        // create a new AcademicYear instance
        AcademicYear newYear = new AcademicYear(yearName, yearStartDate, yearEndDate);
        // add it to the map
        Main.yearMap.put(yearName, newYear);

        System.out.print("Success! Academic year '" + yearName + "' has been created.");
    }
    // admins and teachers only
    public void viewAllAcademicYears(User currentUser) {
        System.out.println("\n=== View All Academic Years ===");

        // validate permissions
        if (!currentUser.getRole().equals(RoleUtil.Role.ADMIN) || currentUser.getRole().equals(RoleUtil.Role.TEACHER)) {
            System.out.println("Error: You do not have permission to view all academic years.");
        }
    }
    // admin only
    public void deleteAcademicYear(User currentUser) {
        System.out.println("\n=== Delete Academic Year ===");

        // validate permissions
        if (!currentUser.getRole().equals(RoleUtil.Role.ADMIN)) {
            System.out.print("Error: You do not have permission to create academic years.");
            return;
        }

        System.out.print("Enter Year To Delete: ");
        String yearInput = scanner.nextLine().trim();
        int year;
        try {
            year = Integer.parseInt(yearInput);
        } catch (NumberFormatException e) {
            System.out.print("Error: Invalid year. Please enter a number.");
            return;
        }

        // get the year from the map
        Main.yearMap.get(yearInput);

        // ask for confirmation
        System.out.println("Are you sure you want to delete this year (yes/no)? ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("yes")) {
            Main.yearMap.remove(yearInput);
        }
    }
    // admin only
    public void generateTermsForYear(User currentUser) {
        System.out.println("\n=== Generate Terms For Year ===");

        // validate permissions
        if (!currentUser.getRole().equals(RoleUtil.Role.ADMIN)) {
            System.out.print("Error: You do not have permission to create academic years.");
            return;
        }

        // ask for year
        System.out.print("Enter Year To Generate Terms For: ");
        String yearInput = scanner.nextLine().trim();
        int year;
        try {
            year = Integer.parseInt(yearInput);
        } catch (NumberFormatException e) {
            System.out.print("Error: Invalid year. Please enter a number.");
            return;
        }

        String yearName = year + "-" + (year + 1);
        AcademicYear academicYear = Main.yearMap.get(yearName);
        if (academicYear == null) {
            System.out.print("Error: Academic year '" + yearName + "' not found.");
            return;
        }

        boolean foundAny = false;
        for (AcademicTerm term : Main.termMap.values()) {
            if (isTermInYear(term, academicYear)) {
                foundAny = true;
                System.out.println(ReportGenerator.generateTermReport(term));
            }
        }

        if (!foundAny) {
            System.out.print("No terms found for academic year '" + yearName + "'.");
        }
    }
    // admin only
    public void linkCourseToYear(User currentUser) {
        System.out.println("\n=== Link Course To Year ===");

        // validate permissions
        if (!currentUser.getRole().equals(RoleUtil.Role.ADMIN)) {
            System.out.print("Error: You do not have permission to create academic years.");
            return;
        }
    }
    // open to all except parents
    public void getYearSpecificStats(User currentUser) {
        System.out.println("\n=== Get Year Specific Stats ===");

        // validate permissions
        if(currentUser.getRole().equals(RoleUtil.Role.PARENT)) {
            System.out.println("Error: You do not have permission to get year specific stats.");
        }

        System.out.print("Enter Year To Get Specific Stats: ");
        String yearInput = scanner.nextLine().trim();
        int year;
        try {
            year = Integer.parseInt(yearInput);
        } catch (NumberFormatException e) {
            System.out.print("Error: Invalid year. Please enter a number.");
            return;
        }
    }

    private boolean isTermInYear(AcademicTerm term, AcademicYear year) {
        LocalDate termStart = term.getStartDate();
        LocalDate termEnd = term.getEndDate();
        LocalDate yearStart = year.getYearStartDate();
        LocalDate yearEnd = year.getYearEndDate();

        return !(termEnd.isBefore(yearStart) || termStart.isAfter(yearEnd));
    }

    private LocalDate getDateInput(String prompt) {
        System.out.print(prompt);
        String dateStr = scanner.nextLine().trim();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
            return null;
        }
    }
}
