import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 写对象
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\8.序列化流\\oos.txt"));
        Person p = new Person("陈冠希",30);
        oos.writeObject(p);
        oos.close();
        // 读对象
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\8.序列化流\\oos.txt"));
        Object obj = ois.readObject();
        ois.close();
        // 调用的是 Person 类的 toString 方法
        System.out.println(obj);
    }
}
