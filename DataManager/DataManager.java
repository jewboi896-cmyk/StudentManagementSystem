/*
This file deals with saving data across sessions. i know its full of errors but it works like this so
dont change anything
 */

package DataManager;

import Assignments.Assignment;
import Attendance.AttendanceRecord;
import Course.Course;
import Main.Main;
import Student.Student;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.*;

public class DataManager {
    // Gson instance for JSON serialization/deserialization
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();
    private static final String COURSES_FILE = "courses.json";
    private static final String STUDENTS_FILE = "students.json";
    private static final String ASSIGNMENTS_FILE = "assignments.json";
    private static final String ATTENDANCE_FILE = "attendance.json";
    private static final String TERM_FILE = "terms.json";

    // Save all courses to JSON file
    public static void saveCourses(Map<String, Course> courses) {
        try (FileWriter writer = new FileWriter(COURSES_FILE)) {
            gson.toJson(courses, writer);
            System.out.println("Courses saved successfully.");
        } 
        catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    // Save all students to JSON file
    public static void saveStudents(Map<String, Student> students) {
        try (FileWriter writer = new FileWriter(STUDENTS_FILE)) {
            gson.toJson(students, writer);
            System.out.println("Students saved successfully.");
        } 
        catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }

    // Save all assignments to JSON file
    public static void saveAssignments(Map<String, Assignment> assignments) {
        try (FileWriter writer = new FileWriter(ASSIGNMENTS_FILE)) {
            gson.toJson(assignments, writer);
            System.out.println("Assignments saved successfully.");
        } 
        catch (IOException e) {
            System.err.println("Error saving assignments: " + e.getMessage());
        }
    }
    // Save all data (courses, students, assignments)
    public static void saveAllData(Map<String, Course> courses, Map<String, Student> students, Map<String, Assignment> assignments, Map<String, AttendanceRecord> attendance) {
        saveCourses(courses);
        saveStudents(students);
        saveAssignments(assignments);
        saveAttendance(attendance);
    }

    // Load all courses from JSON file
    public static Map<String, Course> loadCourses() {
        // noinspection unchecked
        try (FileReader reader = new FileReader(COURSES_FILE)) {
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<String, Course>>(){}.getType();
            Map<String, Course> courses = gson.fromJson(reader, type);
            System.out.println("Courses loaded successfully.");
            return courses != null ? courses : new java.util.concurrent.ConcurrentHashMap<>();
        } 
        catch (IOException e) {
            System.err.println("Error loading courses (file may not exist yet): " + e.getMessage());
            return new java.util.concurrent.ConcurrentHashMap<>();
        }
    }

    // Load all students from JSON file
    public static Map<String, Student> loadStudents() {
        // noinspection unchecked
        try (FileReader reader = new FileReader(STUDENTS_FILE)) {
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<String, Student>>(){}.getType();
            Map<String, Student> students = gson.fromJson(reader, type);
            System.out.println("Students loaded successfully.");
            return students != null ? students : new java.util.concurrent.ConcurrentHashMap<>();
        } 
        catch (IOException e) {
            System.err.println("Error loading students (file may not exist yet): " + e.getMessage());
            return new java.util.concurrent.ConcurrentHashMap<>();
        }
    }

    // Load all assignments from JSON file
    public static Map<String, Assignment> loadAssignments() {
        try (FileReader reader = new FileReader(ASSIGNMENTS_FILE)) {
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<String, Assignment>>(){}.getType();
            Map<String, Assignment> assignments = gson.fromJson(reader, type);
            System.out.println("Assignments loaded successfully.");
            return assignments != null ? assignments : new java.util.concurrent.ConcurrentHashMap<>();
        } 
        catch (IOException e) {
            System.err.println("Error loading assignments (file may not exist yet): " + e.getMessage());
            return new java.util.concurrent.ConcurrentHashMap<>();
        }
    }

    // Load everything at once
    public static void loadAllData() {
        Main.courseMap.putAll(loadCourses());
        Main.studentMap.putAll(loadStudents());
        Main.assignmentMap.putAll(loadAssignments());
        Main.attendanceMap.putAll(loadAttendance());
    }

    public static void saveAttendance(Map<String, AttendanceRecord> attendance) {
    try (FileWriter writer = new FileWriter(ATTENDANCE_FILE)) {
        gson.toJson(attendance, writer);
        System.out.println("Attendance saved successfully.");
    } catch (IOException e) {
        System.err.println("Error saving attendance: " + e.getMessage());
    }
}

public static Map<String, AttendanceRecord> loadAttendance() {
    try (FileReader reader = new FileReader(ATTENDANCE_FILE)) {
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<String, AttendanceRecord>>(){}.getType();
        Map<String, AttendanceRecord> attendance = gson.fromJson(reader, type);
        System.out.println("Attendance loaded successfully.");
        return attendance != null ? attendance : new ConcurrentHashMap<>();
    } catch (IOException e) {
        System.err.println("Error loading attendance (file may not exist yet): " + e.getMessage());
        return new ConcurrentHashMap<>();
    }
}

private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.toString());
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return LocalDate.parse(json.getAsString());
        }
    }
    
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime dateTime, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateTime.toString());
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return LocalDateTime.parse(json.getAsString());
        }
    }
}