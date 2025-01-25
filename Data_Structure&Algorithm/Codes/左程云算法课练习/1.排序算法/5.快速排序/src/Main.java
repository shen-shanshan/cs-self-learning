public class Main {
    public void quickSort(int[] arr) {
        if (arr.length == 0 || arr.length == 1) return;
        process(arr, 0, arr.length - 1);
    }

    public void process(int[] arr, int left, int right) {
        if (left >= right) return;
        swap(arr, left + (int) (Math.random() * (right - left + 1)), right);
        // 分层
        int[] equal = partition(arr, left, right);
        // 递归
        process(arr, left, equal[0] - 1);
        process(arr, equal[1] + 1, right);
    }

    public int[] partition(int[] arr, int left, int right) {
        // if (left > right) return new int[]{-1, -1};
        // if (left == right) return new int[]{left, right};
        int compare = arr[right];
        int tmp = left;
        int lessR = left - 1;
        int moreL = right;
        while (tmp < moreL) {
            if (arr[tmp] < compare) {
                swap(arr, ++lessR, tmp++);
            } else if (arr[tmp] > compare) {
                swap(arr, --moreL, tmp);
            } else {
                tmp++;
            }
        }
        swap(arr, moreL, right);
        return new int[]{lessR + 1, moreL};
    }

    public void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    public static void main(String[] args) {
        Main m = new Main();
        for (int i = 0; i < 10; i++) {
            System.out.print("排序前的数组：");
            ArrayTools t = new ArrayTools(100, 20);
            int[] test = t.getArray();
            ArrayTools.print(test);
            System.out.print("排序后的数组：");
            m.quickSort(test);
            ArrayTools.print(test);
            System.out.println("----------------------");
        }
    }
}
