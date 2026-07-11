package learnings.fun_interface;

public class Student {
    String name;
    String department;
    int age;

    public Student() {}
    public Student(String name, String department, int age) {
        this.name = name;
        this.department = department;
        this.age = age;
    }

    public String getName() {
        return name;
    }
}
