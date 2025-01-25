import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {

        // File file = new File("写数据.txt");
        // FileOutputStream fos = new FileOutputStream(file);
        FileOutputStream fos = new FileOutputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\2.字节流 - 读写数据\\写数据.txt");
        // 以字节流写数据
        fos.write("hello world".getBytes());
        // 释放资源
        fos.close();

        // 实现数据的追加写入
        FileOutputStream fos2 = new FileOutputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\2.字节流 - 读写数据\\写数据.txt", true);
        fos2.write("\n".getBytes());
        fos2.write("IO".getBytes());
        fos2.close();

        FileInputStream fis = new FileInputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\2.字节流 - 读写数据\\写数据.txt");
        int by = 0;
        // 以字节流读数据
        // read() 返回的是每次实际读取的字节个数
        while ((by = fis.read()) != -1) {
            System.out.print((char) by);
        }
        fis.close();
    }
}
