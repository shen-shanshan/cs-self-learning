import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\2.字节流 - 读写数据\\src\\Test.java");
        FileOutputStream fos = new FileOutputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\3.字节流 - 复制文本文件\\文本文件复制.txt");

        // 一次读一个字节数组
        byte[] bys = new byte[100];
        int len = 0;
        while ((len = fis.read(bys)) != -1) {
            fos.write(bys);
        }
        fos.close();
        fis.close();
    }
}
