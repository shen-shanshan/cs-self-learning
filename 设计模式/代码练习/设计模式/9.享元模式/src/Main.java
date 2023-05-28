public class Main {
    public static void main(String[] args) {
        String s1 = "abc";
        String s2 = "abc";
        // “abc”存储在字符串常量池中，s1、s2是同一个对象
        String s3 = new String("abc");
        String s4 = new String("abc");
        // new是在堆空间创建对象，地址值不同

        System.out.println(s1 == s2); // true
        System.out.println(s1 == s3); // false
        System.out.println(s3 == s4); // false
        // 一个String对象，可以通过intern( )方法，拿到常量池中对应的字符串常量对象
        System.out.println(s1 == s3.intern()); // true
        System.out.println(s3.intern() == s4.intern()); // true
    }
}
