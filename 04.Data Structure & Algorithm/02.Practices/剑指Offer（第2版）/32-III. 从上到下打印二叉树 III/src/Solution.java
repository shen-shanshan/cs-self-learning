import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        boolean flag = true;
        Deque<TreeNode> stack = new LinkedList<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            int size = stack.size();
            List<Integer> now = new ArrayList<>();
            List<TreeNode> cur = new ArrayList<>();
            // 先将当前层的节点弹空
            for (int i = 0; i < size; i++) {
                cur.add(stack.pop());
            }
            // 依次加入下一层的节点
            for (int i = 0; i < size; i++) {
                TreeNode node = cur.get(i);
                now.add(node.val);
                if (flag) {
                    // 先左后右
                    if (node.left != null) {
                        stack.push(node.left);
                    }
                    if (node.right != null) {
                        stack.push(node.right);
                    }
                } else {
                    // 先右后左
                    if (node.right != null) {
                        stack.push(node.right);
                    }
                    if (node.left != null) {
                        stack.push(node.left);
                    }
                }
            }
            flag = !flag;
            ans.add(now);
        }
        return ans;
    }
}
