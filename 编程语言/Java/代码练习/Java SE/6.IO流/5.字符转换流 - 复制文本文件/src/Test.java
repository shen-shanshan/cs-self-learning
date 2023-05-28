import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// 字符流只能用于复制文本文件，不能复制图片、视频。

public class Test {
    public static void main(String[] args) throws IOException{
        FileReader fr = new FileReader("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\4.高效字节流 - 复制单级目录\\src\\Test.java");
        FileWriter fw = new FileWriter("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\5.字符转换流 - 复制文本文件\\文件复制.txt");

        char[] chs = new char[1024];
        int len = 0;
        while((len = fr.read(chs)) != -1){
            fw.write(chs,0,len);
            // 字符流写数据，只有在流对象 close 以后，数据才会被写入。
            // 可以通过不断执行 flush 来刷新写入的数据。
            fw.flush();
        }
        fw.close();
        fr.close();
    }
}
