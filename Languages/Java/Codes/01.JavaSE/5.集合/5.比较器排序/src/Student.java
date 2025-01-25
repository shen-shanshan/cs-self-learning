public class Student implements Comparable<Student> {
    public int age;
    public String name;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int compareTo(Student s) {
        int num = this.age - s.age;
        int num2 = (num == 0) ? this.name.compareTo(s.name) : num;
        return num2;
    }
}
