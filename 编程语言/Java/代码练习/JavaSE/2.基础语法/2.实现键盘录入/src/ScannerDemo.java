import java.util.Scanner;

public class ScannerDemo {
    public static void main(String[] args){
        while(true){
            System.out.print("请输入：");
            Scanner sc = new Scanner(System.in);
            int x = sc.nextInt();
            System.out.println("你输入的是："+x);
        }
    }
}
