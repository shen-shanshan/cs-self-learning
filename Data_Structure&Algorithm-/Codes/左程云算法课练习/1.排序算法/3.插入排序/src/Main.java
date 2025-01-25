public class Main {

    public void insertSort(int[] arr) {
        if (arr.length == 0 || arr.length == 1) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            int j = i;
            while (j > 0 && arr[j] < arr[j - 1]) {
                int tmp = arr[j - 1];
                arr[j - 1] = arr[j];
                arr[j] = tmp;
                j--;
            }
        }
    }

    public static void main(String[] args) {
        Main m = new Main();
        for (int i = 0; i < 5; i++) {
            System.out.print("排序前的数组：");
            ArrayTools t = new ArrayTools(100, 20);
            int[] test = t.getArray();
            ArrayTools.print(test);
            System.out.print("排序后的数组：");
            m.insertSort(test);
            ArrayTools.print(test);
            System.out.println("----------------------");
        }
    }
}
