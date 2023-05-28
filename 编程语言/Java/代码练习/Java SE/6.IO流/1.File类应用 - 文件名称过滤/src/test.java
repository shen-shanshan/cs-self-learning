import java.io.File;
import java.io.FilenameFilter;

public class test {
    public static void main(String[] args) throws AssertionError {
        // 将指定的文件目录封装为 file 对象
        File file = new File("E:\\图片\\证件");

        // 方法一：
        // 获取该目录下的 file 文件对象数组
        File[] fileArray = file.listFiles();
        if (fileArray == null) {
            throw new AssertionError();
        }
        for (File f : fileArray) {
            if (f.isFile()) {
                if (f.getName().endsWith(".jpg")) {
                    System.out.println(f.getName());
                }
            }
        }
        System.out.println("----------");

        // 方法二：使用文件名称过滤器 - 以匿名内部类的方式实现
        String[] fileNameArray = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File file = new File(dir, name);
                boolean flag1 = file.isFile();
                boolean flag2 = file.getName().endsWith(".jpg");
                return flag1 && flag2;
            }
        });
        for (String s : fileNameArray) {
            System.out.println(s);
        }
    }
}
