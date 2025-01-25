import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {
        // 封装文件夹为 file 对象
        File srcFolder = new File("E:\\");
        File dstFolder = new File("E:\\Programs\\IntelliJ IDEA\\Java SE\\6.IO流\\4.高效字节流 - 复制单级目录\\E盘目录复制");

        // 若目的文件夹不存在，则创建
        if (!dstFolder.exists()) {
            dstFolder.mkdir();
        }

        File[] fileArray = srcFolder.listFiles();
        for (File f : fileArray) {
            String name = f.getName();
            File newFile = new File(dstFolder, name);
            copyFile(f, newFile);
        }
    }

    // 类中被 static 修饰的"变量，方法，内部类"可被该类直接调用，而不用实例化该类再使用。
    // 通常我们把被 static 修饰过的称为成员变量，成员方法，成员内部类。
    // 不被 static 修饰的我们一般称为实例变量，实例方法，实例内部类，它们必须实例化后才能被分配相应内存，之后才能使用。
    public static void copyFile(File src, File dst) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dst));
        byte[] bys = new byte[100];
        int len = 0;
        while ((len = bis.read()) != -1) {
            bos.write(bys, 0, len);
        }
        bos.close();
        bis.close();
    }
}
