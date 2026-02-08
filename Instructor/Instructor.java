/*
This is the instructor clas which i dont think i am using but just keep it for now
 */

package Instructor;

import Course.Course;

public class Instructor {
    private final String name;
    private final String email;
    private final int age;
    private final Course courseClass; // potentially change this to say Course courseClass

    public Instructor(String instructorName, String instructorEmail, int instructorAge, Course courseClass) {
        this.name = instructorName;
        this.email = instructorEmail;
        this.age = instructorAge;
        this.courseClass = courseClass;
    }

    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public int getAge() { return this.age; }
    public Course getClazz() { return this.courseClass; }
}