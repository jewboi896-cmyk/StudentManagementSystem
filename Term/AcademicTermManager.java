package Term;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import Main.Entry;
import User.User;
import Role.RoleUtil;
import Term.AcademicTerm.TermStatus;
import Term.AcademicTerm.TermType;

public class AcademicTermManager {
    private final Scanner scanner;
    public static Entry entryApp;

    public AcademicTermManager(Scanner scanner, Entry entryApp) {
        this.scanner = scanner;
        this.entryApp = entryApp;
    }

    public void viewTermInfo(User currentUser) {
        System.out.println("\n=== View Term Information ===");
        
        String termName;
        
        // All roles can view term info, but students might see limited info
        System.out.print("Enter term name (or press Enter for current term): ");
        termName = scanner.nextLine().trim();
        
        AcademicTerm term;
        
        if (termName.isEmpty()) {
            // Get current term
            term = Entry.currentTerm;
            if (term == null) {
                System.out.println("Error: No current term is set.");
                return;
            }
        } 
        else {
            // Validate term
            term = entryApp.termValidation(termName);
            
            // Check if term exists
            if (term == null) {
                System.out.println("Error: Term '" + termName + "' does not exist.");
                return;
            }
        }
        
        // Check if user can access term
        if (!currentUser.canAccessTerm(term.getTermName())) {
            System.out.println("Access denied: You do not have permission to access this term's information.");
            return;
        }
        
        // Display term information
        System.out.println("\n=== Term Details ===");
        entryApp.displayTermAttributes(term);
    }

    public void addNewTerm(User currentUser) {
        // Check if user has correct permissions (only admins can create terms)
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. You do not have the required permissions to create academic terms.");
            return;
        }

        System.out.println("\n=== Add New Academic Term ===");
        
        // Get term name
        System.out.print("Enter term name (e.g., 'Fall 2024'): ");
        String termName = scanner.nextLine().trim();
        
        // Check if term already exists
        if (Entry.termMap.containsKey(termName)) {
            System.out.println("Error: A term with the name '" + termName + "' already exists.");
            return;
        }
        
        // Get start date
        LocalDate startDate = getDateInput("Enter term start date (YYYY-MM-DD): ");
        if (startDate == null) return;
        
        // Get end date
        LocalDate endDate = getDateInput("Enter term end date (YYYY-MM-DD): ");
        if (endDate == null) return;
        
        // Validate date range
        if (!endDate.isAfter(startDate)) {
            System.out.println("Error: End date must be after start date.");
            return;
        }
        
        // Get term type
        System.out.println("\nSelect term type:");
        System.out.println("1. Semester");
        System.out.println("2. Quarter");
        System.out.println("3. Trimester");
        System.out.print("Enter choice (1-3): ");
        
        TermType termType;
        try {
            int typeChoice = Integer.parseInt(scanner.nextLine().trim());
            termType = switch (typeChoice) {
                case 1 -> TermType.SEMESTER;
                case 2 -> TermType.QUARTER;
                case 3 -> TermType.TRIMESTER;
                default -> {
                    System.out.println("Error: Invalid term type selection.");
                    yield null;
                }
            };
            if (termType == null) return;
        } 
        catch (NumberFormatException e) {
            System.out.println("Error: Invalid input. Please enter a number.");
            e.printStackTrace();
            return;
        }
        
        // Get term status
        System.out.println("\nSelect term status:");
        System.out.println("1. Active");
        System.out.println("2. Completed");
        System.out.println("3. Upcoming");
        System.out.print("Enter choice (1-3): ");
        
        TermStatus termStatus;
        try {
            int statusChoice = Integer.parseInt(scanner.nextLine().trim());
            termStatus = switch (statusChoice) {
                case 1 -> TermStatus.ACTIVE;
                case 2 -> TermStatus.COMPLETED;
                case 3 -> TermStatus.UPCOMING;
                default -> {
                    System.out.println("Error: Invalid status selection.");
                    yield null;
                }
            };
            if (termStatus == null) return;
        } 
        catch (NumberFormatException e) {
            System.out.println("Error: Invalid input. Please enter a number.");
            return;
        }
        
        // Create the new term
        AcademicTerm newTerm = new AcademicTerm(termName, startDate, endDate, termType, termStatus);
        Entry.addTerm(newTerm);
        
        // Ask if this should be the current term
        if (termStatus == TermStatus.ACTIVE) {
            System.out.print("\nSet this as the current term? (yes/no): ");
            String setAsCurrent = scanner.nextLine().trim().toLowerCase();
            if (setAsCurrent.equals("yes") || setAsCurrent.equals("y")) {
                Entry.currentTerm = newTerm;
                System.out.println("This term has been set as the current term.");
            }
        }
        
        System.out.println("Success! Term '" + termName + "' has been created.");
    }

    public void deleteTerm(User currentUser) {
        // Check if user has correct permissions
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. You do not have the required permissions to delete terms.");
            return;
        }

        System.out.println("\n=== Delete Academic Term ===");
        System.out.print("Enter term name to delete: ");
        String termName = scanner.nextLine().trim();

        // Validate term
        AcademicTerm term = entryApp.termValidation(termName);

        // Check if term exists
        if (term == null) {
            System.out.println("Error: Term '" + termName + "' not found.");
            return;
        }
        
        // Check if term is locked
        if (term.isLocked()) {
            System.out.println("Error: Cannot delete a locked term. Unlock it first.");
            return;
        }
        
        // Warning about data loss
        System.out.println("\nWARNING: Deleting this term may affect:");
        System.out.println("- Student grades associated with this term");
        System.out.println("- Course assignments linked to this term");
        
        // Confirm deletion
        System.out.print("\nAre you sure you want to delete term '" + termName + "'? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes") && !confirmation.equals("y")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        
        // Remove term from the system
        Entry.termMap.remove(termName);
        
        // If this was the current term, clear it
        if (Entry.currentTerm != null && Entry.currentTerm.equals(term)) {
            Entry.currentTerm = null;
            System.out.println("Note: This was the current term. No current term is now set.");
        }
        
        System.out.println("Success! Term '" + termName + "' has been deleted.");
    }

    public void setCurrentTerm(User currentUser) {
        // Check permissions
        if (currentUser.getRole() != RoleUtil.Role.ADMIN && currentUser.getRole() != RoleUtil.Role.TEACHER) {
            System.out.println("Access denied. Only administrators and teachers can set the current term.");
            return;
        }

        System.out.println("\n=== Set Current Term ===");
        
        // Show available terms
        if (Entry.termMap.isEmpty()) {
            System.out.println("Error: No terms exist in the system.");
            return;
        }
        
        System.out.println("\nAvailable terms:");
        int index = 1;
        for (String termName : Entry.termMap.keySet()) {
            AcademicTerm term = Entry.termMap.get(termName);
            String current = (Entry.currentTerm != null && Entry.currentTerm.equals(term)) ? " (CURRENT)" : "";
            System.out.println(index + ". " + termName + " [" + term.getTermStatus() + "]" + current);
            index++;
        }
        
        System.out.print("\nEnter term name to set as current: ");
        String termName = scanner.nextLine().trim();
        
        AcademicTerm term = entryApp.termValidation(termName);
        if (term == null) {
            System.out.println("Error: Term '" + termName + "' not found.");
            return;
        }
        
        // Set as current term
        Entry.currentTerm = term;
        System.out.println("Success! '" + termName + "' is now the current term.");
    }

    public void lockUnlockTerm(User currentUser) {
        // Only admins can lock/unlock terms
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. Only administrators can lock or unlock terms.");
            return;
        }

        System.out.println("\n=== Lock/Unlock Term ===");
        System.out.print("Enter term name: ");
        String termName = scanner.nextLine().trim();

        AcademicTerm term = entryApp.termValidation(termName);
        if (term == null) {
            System.out.println("Error: Term '" + termName + "' not found.");
            return;
        }
        
        if (term.isLocked()) {
            System.out.println("Term '" + termName + "' is currently LOCKED.");
            System.out.print("Unlock this term? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if (confirm.equals("yes") || confirm.equals("y")) {
                term.unlockTerm();
                System.out.println("Success! Term '" + termName + "' has been unlocked.");
            } else {
                System.out.println("Operation cancelled.");
            }
        } else {
            System.out.println("Term '" + termName + "' is currently UNLOCKED.");
            System.out.println("Locking will:");
            System.out.println("- Prevent modifications to grades");
            System.out.println("- Set status to COMPLETED");
            System.out.print("\nLock this term? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if (confirm.equals("yes") || confirm.equals("y")) {
                term.lockTerm();
                System.out.println("Success! Term '" + termName + "' has been locked.");
            } else {
                System.out.println("Operation cancelled.");
            }
        }
    }

    public void viewAllTerms(User currentUser, LocalDate currentDate) {
        System.out.println("\n=== All Academic Terms ===");
        
        if (Entry.termMap.isEmpty()) {
            System.out.println("No terms exist in the system.");
            return;
        }
        
        // Display current term if set
        if (Entry.currentTerm != null) {
            System.out.println("\nCurrent Term: " + Entry.currentTerm.getTermName());
            System.out.println("----------------------------------------");
        }
        
        System.out.println("\nAll Terms:");
        System.out.println("========================================");
        
        for (AcademicTerm term : Entry.termMap.values()) {
            System.out.println("\nTerm: " + term.getTermName());
            System.out.println("  Type: " + term.getTermType());
            System.out.println("  Status: " + term.getTermStatus());
            System.out.println("  Start: " + term.getStartDate());
            System.out.println("  End: " + term.getEndDate());
            System.out.println("  Locked: " + (term.isLocked() ? "Yes" : "No"));
        
            // Check if it's current
            if (term.isTermCurrent(currentDate)) {
                System.out.println("  [CURRENTLY ACTIVE]");
            }
        }
    }

    public void updateTermStatus(User currentUser) {
        // Only admins can update term status
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. Only administrators can update term status.");
            return;
        }

        System.out.println("\n=== Update Term Status ===");
        System.out.print("Enter term name: ");
        String termName = scanner.nextLine().trim();

        AcademicTerm term = entryApp.termValidation(termName);
        if (term == null) {
            System.out.println("Error: Term '" + termName + "' not found.");
            return;
        }
        
        if (term.isLocked()) {
            System.out.println("Error: Cannot update status of a locked term. Unlock it first.");
            return;
        }
        
        System.out.println("\nCurrent status: " + term.getTermStatus());
        System.out.println("\nSelect new status:");
        System.out.println("1. Active");
        System.out.println("2. Completed");
        System.out.println("3. Upcoming");
        System.out.print("Enter choice (1-3): ");
        
        try {
            int statusChoice = Integer.parseInt(scanner.nextLine().trim());
            TermStatus newStatus = switch (statusChoice) {
                case 1 -> TermStatus.ACTIVE;
                case 2 -> TermStatus.COMPLETED;
                case 3 -> TermStatus.UPCOMING;
                default -> {
                    System.out.println("Error: Invalid status selection.");
                    yield null;
                }
            };
            
            if (newStatus != null) {
                term.setTermStatus(newStatus);
                System.out.println("Success! Term status updated to " + newStatus);
            }
        } 
        catch (NumberFormatException e) {
            System.out.println("Error: Invalid input. Please enter a number.");
            e.printStackTrace();
        }
    }

    public void viewTermsByYear(User currentUser) {
        System.out.println("\n=== View Terms by Academic Year ===");
        
        if (Entry.yearMap.isEmpty()) {
            System.out.println("Error: No academic years exist in the system.");
            System.out.println("Would you like to see all terms instead? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                viewAllTerms(currentUser, LocalDate.now());
            }
            return;
        }
        
        // Show available years
        System.out.println("\nAvailable academic years:");
        int index = 1;
        for (String yearName : Entry.yearMap.keySet()) {
            System.out.println(index + ". " + yearName);
            index++;
        }
        
        System.out.print("\nEnter academic year name (e.g., '2024-2025'): ");
        String yearName = scanner.nextLine().trim();
        
        // Validate year exists
        AcademicYear year = Entry.yearMap.get(yearName);
        if (year == null) {
            System.out.println("Error: Academic year '" + yearName + "' not found.");
            return;
        }
        
        // Get all terms for this year
        java.util.List<AcademicTerm> termsInYear = new java.util.ArrayList<>();
        
        for (AcademicTerm term : Entry.termMap.values()) {
            // Check if term falls within the academic year
            // Assumes AcademicYear has getStartDate() and getEndDate() methods
            if (isTermInYear(term, year)) {
                termsInYear.add(term);
            }
        }
        
        if (termsInYear.isEmpty()) {
            System.out.println("\nNo terms found for academic year " + yearName);
            return;
        }
        
        // Display terms for this year
        System.out.println("\n=== Terms for Academic Year " + yearName + " ===");
        System.out.println("========================================");
        
        for (AcademicTerm term : termsInYear) {
            System.out.println("\nTerm: " + term.getTermName());
            System.out.println("  Type: " + term.getTermType());
            System.out.println("  Status: " + term.getTermStatus());
            System.out.println("  Start: " + term.getStartDate());
            System.out.println("  End: " + term.getEndDate());
            System.out.println("  Locked: " + (term.isLocked() ? "Yes" : "No"));
            
            if (Entry.currentTerm != null && Entry.currentTerm.equals(term)) {
                System.out.println("  [CURRENT TERM]");
            }
        }
        
        System.out.println("\nTotal terms in " + yearName + ": " + termsInYear.size());
    }
    
    // Helper method to check if a term falls within an academic year
    private boolean isTermInYear(AcademicTerm term, AcademicYear year) {
        LocalDate termStart = term.getStartDate();
        LocalDate termEnd = term.getEndDate();
        LocalDate yearStart = year.getYearStartDate();
        LocalDate yearEnd = year.getYearEndDate();
        
        // Term is in year if it overlaps with the year's date range
        return !(termEnd.isBefore(yearStart) || termStart.isAfter(yearEnd));
    }

    // Helper method to get date input from user
    private LocalDate getDateInput(String prompt) {
        System.out.print(prompt);
        String dateStr = scanner.nextLine().trim();
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateStr, formatter);
        } 
        catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
            e.printStackTrace();
            return null;
        }
    }
}