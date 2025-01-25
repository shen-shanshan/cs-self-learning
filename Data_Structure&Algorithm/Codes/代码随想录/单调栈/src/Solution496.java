import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 496.下一个更大元素 I
 */
public class Solution496 {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;

        int[] ans = new int[len1];

        // 存的元素值
        Deque<Integer> stack = new LinkedList<>();

        // <元素值, 下一个更大的元素值>
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < len2; i++) {
            // 弹栈
            while (!stack.isEmpty() && stack.peek() < nums2[i]) {
                int val = stack.pop();
                map.put(val, nums2[i]);
            }
            // 入栈
            stack.push(nums2[i]);
        }

        for (int i = 0; i < len1; i++) {
            ans[i] = map.getOrDefault(nums1[i], -1);
        }

        return ans;
    }
}
