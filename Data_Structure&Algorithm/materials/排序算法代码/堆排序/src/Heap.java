public class Heap {
    private int heapSize;
    private int[] heap;

    public Heap() {
    }

    public Heap(int[] arr) {
        this.heap = arr;
        this.heapSize = arr.length;
        // 从下往上建堆
        for (int i = arr.length - 1; i >= 0; i--) {
            heapIfy(heap, i, heapSize);
        }
    }

    public void heapSort() {
        int tmpSize = heapSize;
        swap(heap, 0, --tmpSize);
        while (tmpSize > 0) {
            heapIfy(heap, 0, tmpSize);
            swap(heap, 0, --tmpSize);
        }
    }

    public void heapInsert(int[] arr, int index) {
        int father = (index - 1) / 2;
        while (arr[index] > arr[father]) {
            swap(arr, index, father);
            index = father;
            father = (index - 1) / 2;
        }
    }

    public void heapIfy(int[] arr, int index, int heapSize) {
        int leftSon = index * 2 + 1;
        while (leftSon < heapSize) {
            int large = leftSon;
            int rightSon = leftSon + 1;
            if (rightSon < heapSize) {
                large = arr[leftSon] > arr[rightSon] ? leftSon : rightSon;
            }
            large = arr[index] > arr[large] ? index : large;
            if (large == index) {
                // 说明已经调整完成
                return;
            } else {
                // 继续往下沉并调整
                swap(heap, index, large);
                index = large;
                leftSon = index * 2 + 1;
            }
        }
    }

    public void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
