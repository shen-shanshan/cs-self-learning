public class ArrayDemo {
    public static void main(String[] args) {
        int[][] a = new int[3][3];
        // 列数可以省略

        // 初始化二维数组
        int k = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                a[i][j] = k++;
            }
        }

        System.out.println(a[0]);
        // 若在定义时，不指定列数：Exception in thread "main" java.lang.NullPointerException
        System.out.println(a[0][0]);

        // 遍历二维数组
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(a[i][j]);
            }
            System.out.println();
        }
    }
}
