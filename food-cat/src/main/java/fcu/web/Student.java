package fcu.web;

public class Student
{



    private String gender;
    private String course;
    private String major;
    private String classes;
    private int id;



    public Student(int id, String classes, String major, String course, String gender, int age, String name) {
        this.id = id;
        this.classes = classes;
        this.major = major;
        this.course = course;
        this.gender = gender;
        this.age = age;
        this.name = name;
    }

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int age;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", course='" + course + '\'' +
                ", major='" + major + '\'' +
                ", classes='" + classes + '\'' +
                ", id=" + id +
                '}';
    }


}
