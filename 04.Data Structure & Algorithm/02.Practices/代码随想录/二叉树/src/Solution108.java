/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 108.将有序数组转换为二叉搜索树
 */
public class Solution108 {
    public TreeNode sortedArrayToBST(int[] nums) {
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

        // 找到根节点
        int rootIndex = start + ((end - start) >> 1);
        TreeNode root = new TreeNode(nums[rootIndex]);

        root.left = myBuild(nums, start, rootIndex - 1);
        root.right = myBuild(nums, rootIndex + 1, end);

        return root;
    }
}
