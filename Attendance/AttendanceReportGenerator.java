/*
This file generates attendence reports
 */

package Attendance;

import Main.Entry;
import Student.Student;
import java.util.*;

public class AttendanceReportGenerator {
    
    public static String generateStudentAttendanceReport(Student student) {
        StringBuilder attendenceReport = new StringBuilder();
        
        attendenceReport.append("═══════════════════════════════════════════════════════\n");
        attendenceReport.append("              ATTENDANCE REPORT                        \n");
        attendenceReport.append("═══════════════════════════════════════════════════════\n\n");
        
        attendenceReport.append("Student: ").append(student.getStudentName()).append("\n");
        attendenceReport.append("Year: ").append(student.getStudentYear()).append("\n\n");
        
        // Count attendance by course
        Map<String, int[]> attendenceByCourse = new HashMap<>();
        // int[] = {present, absent, tardy, excused}
        
        for (AttendanceRecord attendanceRecord : Entry.attendanceMap.values()) {
            if (attendanceRecord.getStudent().equals(student)) {
                String courseName = attendanceRecord.getCourse().getCourseName();
                int[] attendenceCounts = attendenceByCourse.computeIfAbsent(courseName, k -> new int[4]);
                
                switch (attendanceRecord.getStatus()) {
                    case PRESENT -> attendenceCounts[0]++;
                    case ABSENT -> attendenceCounts[1]++;
                    case TARDY -> attendenceCounts[2]++;
                    case EXCUSED -> attendenceCounts[3]++;
                }
            }
        }
        
        if (attendenceByCourse.isEmpty()) {
            attendenceReport.append("No attendance records found.\n");
        } 
        else {
            attendenceReport.append("───────────────────────────────────────────────────────\n");
            attendenceReport.append("                 BY COURSE                             \n");
            attendenceReport.append("───────────────────────────────────────────────────────\n\n");
            
            for (Map.Entry<String, int[]> attendenceEntry : attendenceByCourse.entrySet()) {
                int[] attendenceCounts = attendenceEntry.getValue();
                int total = attendenceCounts[0] + attendenceCounts[1] + attendenceCounts[2] + attendenceCounts[3];
                double rate = total > 0 ? (attendenceCounts[0] * 100.0 / total) : 0;
                
                attendenceReport.append("Course: ").append(attendenceEntry.getKey()).append("\n");
                attendenceReport.append("  Present: ").append(attendenceCounts[0]).append("\n");
                attendenceReport.append("  Absent: ").append(attendenceCounts[1]).append("\n");
                attendenceReport.append("  Tardy: ").append(attendenceCounts[2]).append("\n");
                attendenceReport.append("  Excused: ").append(attendenceCounts[3]).append("\n");
                attendenceReport.append("  Attendance Rate: ").append(String.format("%.1f%%", rate)).append("\n\n");
            }
        }
        
        attendenceReport.append("═══════════════════════════════════════════════════════\n");
        attendenceReport.append("              End of Report                            \n");
        attendenceReport.append("═══════════════════════════════════════════════════════\n");
        
        return attendenceReport.toString();
    }
}