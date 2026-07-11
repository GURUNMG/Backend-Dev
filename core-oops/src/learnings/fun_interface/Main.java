package learnings.fun_interface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Operation add = (a, b) -> a+b;
        System.out.println(add.add(10, 20));

        Student s1 = new Student("Guru", "CSE", 23);
        Student s2 = new Student("Rishi", "ECE", 25);

        Predicate<Student> oldStudent = student -> student.age>20;
        Predicate<Student> eceStudent = student -> "ECE".equals(student.department);
        Predicate<Student> combined = oldStudent.and(eceStudent);
        System.out.println(combined.test(s1));
        System.out.println(combined.test(s2));

        List<Student> students = List.of(s1, s2);

        List<Student> filtered = students.stream().filter(combined).toList();
        System.out.println(filtered.get(0).name);

        Function<Student, String> studenName = student -> student.name;
        System.out.println(students.stream().map(studenName).toList());
        System.out.println(students.stream().map(Student :: getName).collect(Collectors.toList()));

    }
}
