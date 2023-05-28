import java.util.*;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public int[] levelOrder(TreeNode root) {
        int[] ans = new int[]{};
        if(root == null){
            return ans;
        }
        List<Integer> ret = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while(!q.isEmpty()){
            TreeNode cur = q.poll();
            ret.add(cur.val);
            if(cur.left != null){
                q.offer(cur.left);
            }
            if(cur.right != null){
                q.offer(cur.right);
            }
        }
        // ans = ret.toArray(new int[ret.size()]);
        return ans;
    }
}
