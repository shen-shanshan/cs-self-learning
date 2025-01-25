import java.util.List;

public class Main {
    public static void main(String[] args) {
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2,node3,null);
        TreeNode node1 = new TreeNode(1,null,node2);

        Solution s = new Solution();
        List<Integer> ans = s.inorderTraversal(node1);

        for (Integer x : ans) {
            System.out.print(x+" ");
        }
    }
}
