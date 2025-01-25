public class Solution {
    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return process(root).diameter;
    }

    public Info process(TreeNode root) {
        if (root == null) {
            return new Info(0, 0);
        }
        // 递归
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);
        // 生成本节点信息
        int height = 1;
        int diameter = 0;
        // 计算高度
        height = Math.max(leftInfo.height + 1, rightInfo.height + 1);
        // 计算直径
        diameter = Math.max(leftInfo.diameter, rightInfo.diameter);
        diameter = Math.max(diameter, leftInfo.height + rightInfo.height);
        return new Info(height, diameter);
    }
}

class Info {
    int height;
    int diameter;

    public Info(int height, int diameter) {
        this.height = height;
        this.diameter = diameter;
    }
}