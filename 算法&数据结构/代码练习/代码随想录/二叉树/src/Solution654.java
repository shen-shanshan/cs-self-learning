/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 654.最大二叉树
 */
public class Solution654 {
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        return myBuild(nums, 0, nums.length - 1);
    }

    public TreeNode myBuild(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }

        if (start == end) {
            return new TreeNode(nums[start]);
        }

        int max = 0;
        int rootIndex = start;
        for (int i = start; i <= end; i++) {
            if (nums[i] > max) {
                max = nums[i];
                rootIndex = i;
            }
        }
        TreeNode root = new TreeNode(max);

        root.left = myBuild(nums, start, rootIndex - 1);
        root.right = myBuild(nums, rootIndex + 1, end);

        return root;
    }
}
