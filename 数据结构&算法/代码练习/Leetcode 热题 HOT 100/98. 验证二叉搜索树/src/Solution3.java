// 判断一棵树是否为平衡二叉树
public class Solution3 {
    public boolean isAvl(TreeNode root) {
        if (root == null) ;
        return process(root).isAvl;
    }

    public AvlInfo process(TreeNode root) {
        // base case
        if (root == null) {
            return new AvlInfo(true, 0);
        }
        // 向下递归
        AvlInfo leftInfo = process(root.left);
        AvlInfo rightInfo = process(root.right);
        // 生成本节点的信息
        int height = 0;
        height = Math.max(leftInfo.height, rightInfo.height) + 1;
        boolean isAvl = true;
        if (!leftInfo.isAvl || !rightInfo.isAvl
                || Math.abs(leftInfo.height - rightInfo.height) > 1) {
            isAvl = false;
        }
        return new AvlInfo(isAvl, height);
    }
}

class AvlInfo {
    public boolean isAvl;
    public int height;

    public AvlInfo(boolean isAvl, int height) {
        this.isAvl = isAvl;
        this.height = height;
    }
}