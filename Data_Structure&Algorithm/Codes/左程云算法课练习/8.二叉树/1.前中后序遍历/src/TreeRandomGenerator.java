public class TreeRandomGenerator {
    private static int maxNumber;
    private static int maxValue;

    private static final TreeRandomGenerator t = new TreeRandomGenerator();

    private TreeRandomGenerator() {
        maxNumber = 10;
        maxValue = 9;
    }

    public static TreeRandomGenerator getInstance() {
        return t;
    }

    public Node generate() {
        int number = (int) (Math.random() * maxNumber + 1);
        Node head = new Node((int) (Math.random() * maxValue + 1));
        int i = number;

        head = TreeRandomGenerator.gen(head,i);

        return head;
    }
    public static Node gen(Node n,int i){
        if(i == 0) return n;

        double k = Math.random();

        if(i == 1){
            if (k < 0.5) {
                n.left = new Node((int) (Math.random() * maxValue + 1));
                n.left = TreeRandomGenerator.gen(n.left, --i);
            } else {
                n.right = new Node((int) (Math.random() * maxValue + 1));
                n.right = TreeRandomGenerator.gen(n.right, --i);
            }
        }

        if(i > 1){
            if (k < 0.3) {
                n.left = new Node((int) (Math.random() * maxValue + 1));
                n.left = TreeRandomGenerator.gen(n.left, --i);
            } else if(k < 0.6){
                n.right = new Node((int) (Math.random() * maxValue + 1));
                n.right = TreeRandomGenerator.gen(n.right, --i);
            }else{
                n.left = new Node((int) (Math.random() * maxValue + 1));
                n.right = new Node((int) (Math.random() * maxValue + 1));
                i -= 2;
                n.left = TreeRandomGenerator.gen(n.left, i);
                n.right = TreeRandomGenerator.gen(n.right, i);
            }
        }
        return n;
    }
}
