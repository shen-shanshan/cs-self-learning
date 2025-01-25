public class Test {
    public static void main(String[] args) {
        Student s1 = Student.getStudent();
        Student s2 = Student.getStudent();
        // 获取的是同一个对象
        System.out.println(s1 == s2);// true
    }
}
