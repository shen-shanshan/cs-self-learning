import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        int[] arr = {7, 5, 1, 56, 12, 56, 4, 23, 1, 456, 5, 85, 12, 6, 56, 6, 41, 31, 685, 41, 5, 2, 5, 4, 2};
        Heap heap = new Heap(arr);
        heap.heapSort();
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println("------------------------------------------------------");

        PriorityQueue<Integer> heap2 = new PriorityQueue<>();
        for (int i = 0; i < arr.length; i++) {
            heap2.add(arr[i]);
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(heap2.poll() + " ");
        }
    }
}
