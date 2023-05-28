import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\5.字符转换流 - 复制文本文件\\src\\Test.java"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\6.高效字符流 - 复制文本文件\\复制文件.txt"));

        // 一次读取一行，不含换行符，若到达流末尾，则返回 null。
        String line = null;
        while((line = br.readLine()) != null){
            bw.write(line);
            // 换行
            bw.newLine();
            bw.flush();
        }
        br.close();
        bw.close();
    }
}
