import java.util.ArrayList;
import java.util.List;

// 递归方式
public class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        reverse(root, ans);
        return ans;
    }

    public void reverse(TreeNode root, List<Integer> ans) {
        if (root == null) {
            return;
        }
        reverse(root.left, ans);
        ans.add(root.val);
        reverse(root.right, ans);
    }
}