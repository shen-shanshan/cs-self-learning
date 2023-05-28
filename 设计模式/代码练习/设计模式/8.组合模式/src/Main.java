public class Main {
    public static void main(String[] args) {
        // 创建节点
        BranchNode root = new BranchNode("root");
        BranchNode chapter1 = new BranchNode("chapter1");
        BranchNode chapter2 = new BranchNode("chapter2");
        Node n11 = new LeafNode("n11");
        Node n12 = new LeafNode("n12");
        BranchNode chapter21 = new BranchNode("chapter21");
        Node n211 = new LeafNode("n211");
        Node n212 = new LeafNode("n212");

        // 添加节点
        root.add(chapter1);
        root.add(chapter2);
        chapter1.add(n11);
        chapter1.add(n12);
        chapter2.add(chapter21);
        chapter21.add(n211);
        chapter21.add(n212);
    }
}
