public class Solution2 {
    public boolean verifyPostorder(int[] postorder) {
        return recur(postorder, 0, postorder.length - 1);
    }

    boolean recur(int[] postorder, int i, int j) {
        if (i >= j) return true;
        int p = i;
        // 找到第一个大于当前子树 root 值的节点
        while (postorder[p] < postorder[j]) p++;
        int m = p;
        // 判断右树上节点的值是否都大于 root 的值
        while (postorder[p] > postorder[j]) p++;
        return p == j && recur(postorder, i, m - 1) && recur(postorder, m, j - 1);
    }
}
