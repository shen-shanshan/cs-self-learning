public class Main {
    public static void main(String[] args) {
        int[] arr = {7, 5, 1, 56, 12, 56, 4, 23, 1, 456, 5, 85, 12, 6, 56, 6, 41, 31, 685, 41, 5, 2, 5, 4, 2};
        quickSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

    public static void quickSort(int[] arr) {
        if (arr.length == 0 || arr.length == 1) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    public static void process(int[] arr, int left, int right) {
        // base case
        if (left >= right) {
            return;
        }
        // 随机化
        swap(arr, left + (int) (Math.random() * (right - left + 1)), right);
        // 分层
        int[] equal = partition(arr, left, right);
        // 递归
        process(arr, left, equal[0] - 1);
        process(arr, equal[1] + 1, right);
    }

    public static int[] partition(int[] arr, int left, int right) {
        int less = left - 1;
        int more = right;
        int index = left;
        while (index < more) {
            if (arr[index] < arr[right]) {
                swap(arr, ++less, index++);
            } else if (arr[index] > arr[right]) {
                swap(arr, --more, index);
            } else {
                index++;
            }
        }
        swap(arr, more, right);
        return new int[]{less + 1, more};
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
