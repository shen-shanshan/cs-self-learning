public class StudentDemo {
    public static void main(String[] args) {
        // static 修饰成员变量时，该变量的值被所有成员共用
        // 静态变量一经修改，其他对象在调用该变量时都被修改
        Student s1 = new Student(10);
        System.out.println(s1.age);// 10

        Student s2 = new Student();
        System.out.println(s2.age);// 10

        Student.name = "吕晓克";
        System.out.println(Student.name);
    }
}
