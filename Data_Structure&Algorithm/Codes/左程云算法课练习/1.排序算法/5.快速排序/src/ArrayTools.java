public class ArrayTools {
    public int maxValue;
    public int maxLength;

    public ArrayTools(int maxValue, int maxLength) {
        this.maxValue = maxValue;
        this.maxLength = maxLength;
    }

    private int getRandomNumber(int a) {
        return (int) (Math.random() * a) + 1;
    }

    public int[] getArray() {
        int len = getRandomNumber(maxLength);
        if (len == 0) return null;
        int[] res = new int[len];
        for (int i = 0; i < len; i++) {
            res[i] = getRandomNumber(maxValue);
        }
        return res;
    }

    public static void print(int[] arr) {
        int len = arr.length;
        System.out.print("[");
        for (int i = 0; i < len - 1; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.print(arr[len - 1]);
        System.out.println("]");
    }
}
