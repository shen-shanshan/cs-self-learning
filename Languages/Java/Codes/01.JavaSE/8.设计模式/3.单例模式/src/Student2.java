// 懒汉式：用的时候才去创建对象，可能会有线程安全问题
public class Student2 {
    private Student2() {
    }

    private static Student2 s2 = null;

    public synchronized static Student2 getStudent2() {
        if (s2 == null) {
            s2 = new Student2();
        }
        return s2;
    }
}
