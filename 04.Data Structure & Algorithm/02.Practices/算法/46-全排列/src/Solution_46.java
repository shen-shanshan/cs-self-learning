import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution_46 {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        // 初始化我们要维护的动态数组，先将所有元素依次填入
        List<Integer> output = new ArrayList<>();
        for (int num : nums) {
            output.add(num);
        }
        int n = nums.length;
        // 开始递归
        backtrack(n, output, res, 0);
        return res;
    }

    public void backtrack(int n, List<Integer> output, List<List<Integer>> res, int first) {
        // first 为当前需要填的位置的下标，first 之前的位置都已经填过了
        // 所有数都填完了，将当前的情况加入返回数组
        if (first == n) {
            res.add(new ArrayList<>(output));
        }
        for (int i = first; i < n; i++) {
            // 动态维护数组：交换下标为 first 与下标为 i 的元素
            Collections.swap(output, first, i);
            // 递归：继续填下一个数
            backtrack(n, output, res, first + 1);
            // 回溯：撤销操作
            Collections.swap(output, first, i);
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        // int[] nums = {0, 1};
        // int[] nums = {1};

        Solution_46 s = new Solution_46();
        List<List<Integer>> res = s.permute(nums);
        // 打印
        for (List<Integer> x : res) {
            for (Integer y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
}
