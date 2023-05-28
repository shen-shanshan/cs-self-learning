public class Test {
    public static void main(String[] args) {
        int[] b = {1, 2, 3, 4, 5};
        System.out.println(sum(100, b));
    }

    // 这里传入的 b 是一个数组
    public static int sum(int a, int... b) {
        for (int x : b) {
            a += x;
        }
        return a;
    }
}
