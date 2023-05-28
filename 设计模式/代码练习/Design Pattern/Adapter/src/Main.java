import java.io.IOException;

/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-07
 * @Description: 适配器模式 - Client
 */
public class Main {
    public static void main(String[] args) {
        FileIO f = new FileProperties();
        try {
            f.readFromFile("E:\\Programs\\IntelliJ IDEA\\设计模式\\Design Pattern\\Adapter\\src\\file.txt");
            f.setValue("year", "2004");
            f.setValue("month", "4");
            f.setValue("day", "21");
            f.writeToFile("E:\\Programs\\IntelliJ IDEA\\设计模式\\Design Pattern\\Adapter\\src\\newfile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
