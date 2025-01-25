public class Main {

    public void selectSort(int[] arr) {
        if (arr.length == 0 || arr.length == 1) {
            return;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
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
            m.selectSort(test);
            ArrayTools.print(test);
            System.out.println("----------------------");
        }
    }
}
