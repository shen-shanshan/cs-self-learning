import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    List<List<Integer>> ans = new ArrayList<>();

    public List<List<Integer>> pathSum(TreeNode root, int target) {
        if (root == null) {
            return ans;
        }
        List<Integer> cur = new ArrayList<>();
        process(root, target, cur);
        return ans;
    }

    public void process(TreeNode root, int target, List<Integer> cur) {
        target -= root.val;
        cur.add(root.val);
        // base case
        if (root.left == null && root.right == null && target == 0) {
            ans.add(new LinkedList<>(cur));
        }
        // 递归
        if (root.left != null) {
            process(root.left, target, cur);
        }
        if (root.right != null) {
            process(root.right, target, cur);
        }
        // 回溯
        cur.remove(cur.size() - 1);
    }

}
