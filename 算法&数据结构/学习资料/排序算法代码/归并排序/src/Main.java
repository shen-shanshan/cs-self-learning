public class Main {
    public static void main(String[] args) {
        int[] arr = {7, 5, 1, 56, 12, 56, 4, 23, 1, 456, 5, 85, 12, 6, 56, 6, 41, 31, 685, 41, 5, 2, 5, 4, 2};
        mergeSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

    public static void mergeSort(int[] arr) {
        if (arr.length == 0) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    // 二分 -> merge
    public static void process(int[] arr, int left, int right) {
        // base case
        if (left >= right) {
            return;
        }
        // 二分
        int mid = left + ((right - left) >> 1);
        process(arr, left, mid);
        process(arr, mid + 1, right);
        // 归并
        merge(arr, left, mid, right);
    }

    public static void merge(int[] arr, int left, int mid, int right) {
        int[] help = new int[right - left + 1];
        int index = 0;
        int p1 = left;
        int p2 = mid + 1;
        while (p1 <= mid && p2 <= right) {
            help[index++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid) {
            help[index++] = arr[p1++];
        }
        while (p2 <= right) {
            help[index++] = arr[p2++];
        }
        for (int i = 0; i < help.length; i++) {
            arr[left++] = help[i];
        }
    }
}
