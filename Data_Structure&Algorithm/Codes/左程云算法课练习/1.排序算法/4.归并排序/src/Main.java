public class Main {

    public void mergeSort(int[] arr) {
        if (arr.length == 0) return;
        process(arr, 0, arr.length - 1);
    }

    public void process(int[] arr, int left, int right) {
        if (left == right) return;
        int mid = left + ((right - left) >> 1);
        // 递归
        process(arr, left, mid);
        process(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    public void merge(int[] arr, int left, int mid, int right) {
        int[] help = new int[right - left + 1];
        int i = 0;
        int p1 = left;
        int p2 = mid + 1;
        // 归并排序
        while (p1 <= mid && p2 <= right) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        // 其中一个指针越界了
        if (p1 > mid) {
            while (i < help.length) {
                help[i++] = arr[p2++];
            }
        } else if (p2 > right) {
            while (i < help.length) {
                help[i++] = arr[p1++];
            }
        }
        // 覆盖原数组
        for (int j = 0; j < help.length; j++) {
            arr[left++] = help[j];
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
            m.mergeSort(test);
            ArrayTools.print(test);
            System.out.println("----------------------");
        }
    }
}
