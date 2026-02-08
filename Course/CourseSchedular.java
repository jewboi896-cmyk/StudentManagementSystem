package Course;

import java.util.HashMap;
import java.util.Map;

public class CourseSchedular {
    private final Map<Course, TimeSlots> courseTimeSlots = new HashMap<>();

    public enum DaysOfTheWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
    }

    public enum TimeSlots {
        FIRSTPERIOD("1ST Period: 8:00-8:50"), SECONDPERIOD("2ND Period: 8:55-9:05"),
        THIRDPERIOD("3RD Period: 9:10-10:00"), FOURTHPERIOD("4TH Period: 10:05-10:50"),
        FIFTHPERIOD("5TH Period: 10:55-11:45"), SIXTHPERIOD("6TH Period: 11:45-12:40"),
        SEVENTHPERIOD("7TH Period: 12:45-1:35"), EIGHTHPERIOD("8TH Period: 1:40-2:30");

        private final String displayName;
        TimeSlots(String displayName) { this.displayName = displayName; }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }

        public TimeSlots getTimeSlot(TimeSlots timeSlot) {
            switch (timeSlot) {
                case FIRSTPERIOD -> { return TimeSlots.FIRSTPERIOD; }
                case SECONDPERIOD -> { return TimeSlots.SECONDPERIOD; }
                case THIRDPERIOD -> { return TimeSlots.THIRDPERIOD; }
                case FOURTHPERIOD -> { return TimeSlots.FOURTHPERIOD; }
                case FIFTHPERIOD -> { return TimeSlots.FIFTHPERIOD; }
                case SIXTHPERIOD -> { return TimeSlots.SIXTHPERIOD; }
                case SEVENTHPERIOD ->  { return TimeSlots.SEVENTHPERIOD; }
                case EIGHTHPERIOD -> { return TimeSlots.EIGHTHPERIOD; }
            }
            return null;
        }
    }

    public DaysOfTheWeek getDay(DaysOfTheWeek day) {
        switch (day) {
            case MONDAY -> { return DaysOfTheWeek.MONDAY; }
            case TUESDAY -> { return DaysOfTheWeek.TUESDAY; }
            case WEDNESDAY ->  { return DaysOfTheWeek.WEDNESDAY; }
            case THURSDAY -> { return DaysOfTheWeek.THURSDAY; }
            case FRIDAY -> { return DaysOfTheWeek.FRIDAY; }
        }
        return null;
    }

    public void setCourseTimeSlot(Course course, TimeSlots timeSlot) {
        if (course == null || timeSlot == null) {
            return;
        }
        courseTimeSlots.put(course, timeSlot);
    }

    public TimeSlots getCourseTimeSlot(Course course) {
        return courseTimeSlots.get(course);
    }

    public boolean conflictChecker(DaysOfTheWeek day, TimeSlots timeSlot, Course course1, Course course2) {

        // check if course exists
        if (course1 == null || course2 == null) {
            return false;
        }

        DaysOfTheWeek course1Day = day != null ? day : course1.getDaysOffered();
        DaysOfTheWeek course2Day = course2.getDaysOffered();
        if (course1Day == null || course2Day == null) {
            return false;
        }

        TimeSlots course1Slot = timeSlot != null ? timeSlot : courseTimeSlots.get(course1);
        TimeSlots course2Slot = courseTimeSlots.get(course2);
        if (course1Slot == null || course2Slot == null) {
            return false;
        }

        return course1Day == course2Day && course1Slot == course2Slot;
    }
}
