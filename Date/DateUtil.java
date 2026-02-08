/*
This file deals with date formatting and assignment date checking(helper methods)
 */

package Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Parse a date string to LocalDate
    public static LocalDate parseDate(String dateString) {
        // Try multiple date formats
        DateTimeFormatter[] formats = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),      // 2024-01-15
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),      // 01/15/2024
            DateTimeFormatter.ofPattern("MMM dd, yyyy"),    // Jan 15, 2024
            DateTimeFormatter.ofPattern("MMMM dd, yyyy"),   // January 15, 2024
            DateTimeFormatter.ofPattern("dd-MMM-yyyy")      // 15-Jan-2024
        };
    
        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(dateString, format);
            } 
            catch (DateTimeParseException e) {
            }
        }
    
    // If none worked, try the default parser
        try {
            return LocalDate.parse(dateString);
        } 
        catch (DateTimeParseException e) {
            return null;
        }
    }
    
    // Check if a date is overdue
    public static boolean isOverdue(String dateString) {
        // parse the date
        LocalDate date = parseDate(dateString);

        // if date is null, return false
        if (date == null) return false;

        // check if date is before today
        return date.isBefore(LocalDate.now());
    }
    
    // Check if a date is within the next 7 days
    public static boolean isDueSoon(String dateString) {
        // parse the date
        LocalDate date = parseDate(dateString);

        // if date is null, return false
        if (date == null) return false;

        // check if date is within the next 7 days
        LocalDate today = LocalDate.now();
        LocalDate weekFromNow = today.plusDays(7);
        return !date.isBefore(today) && !date.isAfter(weekFromNow);
    }
    
    // Check if a date is in the future (beyond 7 days)
    public static boolean isUpcoming(String dateString) {
        LocalDate date = parseDate(dateString);
        if (date == null) return false;

        // check if date is after 7 days from now
        LocalDate weekFromNow = LocalDate.now().plusDays(7);
        return date.isAfter(weekFromNow);
    }
    
    // Get a human-readable status
    public static String getAssignmentStatus(String dateString, boolean hasScore) {
        if (hasScore) {
            return "Completed";
        }
        
        if (isOverdue(dateString)) {
            return "Overdue";
        } 

        else if (isDueSoon(dateString)) {
            return "Due Soon";
        } 

        else if (isUpcoming(dateString)) {
            return "Upcoming";
        }
        
        return "Unknown";
    }
    
    // Format date for display
    public static String formatDate(String dateString) {
        LocalDate date = parseDate(dateString);
        if (date == null) return dateString;
            // return in "MM, dd, yyyy" format  
            return date.format(DateTimeFormatter.ofPattern("MM, dd, yyyy"));
        }
}
