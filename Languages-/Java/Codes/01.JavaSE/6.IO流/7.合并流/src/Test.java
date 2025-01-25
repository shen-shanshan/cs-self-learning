import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

public class Test {
    // 合并多个 file 文件
    public static void main(String[] args) throws IOException {
        Vector<InputStream> v = new Vector<>();
        InputStream s1 = new FileInputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\7.合并流\\1.txt");
        InputStream s2 = new FileInputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\7.合并流\\2.txt");
        InputStream s3 = new FileInputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\7.合并流\\3.txt");
        v.add(s1);
        v.add(s2);
        v.add(s3);

        Enumeration<InputStream> en = v.elements();
        // 合并流
        SequenceInputStream sis = new SequenceInputStream(en);
        // 输出流
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\7.合并流\\123.txt"));

        byte[] bys = new byte[1024];
        int len = 0;
        while ((len = sis.read(bys)) != -1) {
            bos.write(bys, 0, len);
        }
        bos.close();
        sis.close();
    }
}
