/*堆排序：
 * 建立一个大根堆，做 k−1 次删除操作后堆顶元素就是我们要找的答案。
 * 在面试中，面试官更倾向于让面试者自己实现一个堆（用数组实现）。
 * 「堆排」在很多大公司的面试中都很常见。
 * */
public class Solution3 {
    public int findKthLargest(int[] nums, int k) {
        if (nums.length == 1) {
            return nums[0];
        }
        Heap heap = new Heap(nums);
        int ans = nums[nums.length - 1];
        while (k > 0) {
            ans = heap.getMax(nums);
            k--;
        }
        return ans;
    }
}

// 大根堆
class Heap {
    private int size;

    public Heap(int[] arr) {
        this.size = arr.length;
        // 从下往上建堆
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }
    }

    // 将数组下标为 i 处的节点加入大根堆（向上调整）
    public void heapInsert(int[] arr, int i) {
        if (i < 0 || i >= size) {
            return;
        }
        int father = (i - 1) / 2;
        while (arr[i] > arr[father]) {
            swap(arr, i, father);
            i = father;
        }
    }

    // 删除根节点，并调整堆仍然为大根堆（向下调整）
    public void heapify(int[] arr, int i, int size) {
        // 左子节点
        int left = i * 2 + 1;
        while (left < size) {
            // 记录较大节点的下标
            int large = left;
            // 如果右子节点存在
            if (left + 1 < size) {
                large = arr[left] > arr[left + 1] ? left : left + 1;
            }
            // 将当前子树的根节点和其左右子节点的较大者进行比较
            large = arr[i] > arr[large] ? i : large;
            // 如果当前节点已经是当前子树的最大者，则调整完成
            if (large == i) {
                break;
            }
            swap(arr, i, large);
            // 继续向下调整
            i = large;
            left = i * 2 + 1;
        }
    }

    public int getMax(int[] nums) {
        int max = nums[0];
        swap(nums, 0, --size);
        heapify(nums, 0, size);
        return max;
    }

    public void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
