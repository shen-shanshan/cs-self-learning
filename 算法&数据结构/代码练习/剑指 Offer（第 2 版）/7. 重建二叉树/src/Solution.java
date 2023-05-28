import java.util.HashMap;
import java.util.Map;

public class Solution {
    private Map<Integer, Integer> indexMap;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        int len = preorder.length;
        // 方便快速找到头节点
        indexMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < len; i++) {
            indexMap.put(inorder[i], i);
        }
        return build(preorder, 0, len - 1, inorder, 0, len - 1);
    }

    public TreeNode build(int[] preorder, int preStart, int preEnd
            , int[] inorder, int inStart, int inEnd) {
        if (preEnd - preStart < 0) {
            return null;
        }
        // 只有一个节点
        if (preEnd - preStart == 0) {
            return new TreeNode(preorder[preStart]);
        }
        // 定位头节点
        TreeNode head = new TreeNode(preorder[preStart]);
        /*int i = 0;
        while (inorder[inStart + i] != head.val) {
            i++;
        }*/
        int inorder_root = indexMap.get(preorder[preStart]);
        int i = inorder_root - inStart;
        // 生成左右子节点
        head.left = build(preorder, preStart + 1, preStart + i
                , inorder, inStart, inStart + i - 1);
        head.right = build(preorder, preStart + i + 1, preEnd
                , inorder, inStart + i + 1, inEnd);
        return head;
    }
}
