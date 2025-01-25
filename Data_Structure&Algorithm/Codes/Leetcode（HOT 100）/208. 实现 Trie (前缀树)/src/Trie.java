/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */
public class Trie {
    private Node root;

    public Trie() {
        root = new Node();
    }

    public void insert(String word) {
        if (word.length() == 0) {
            return;
        }
        Node cur = root;
        int next = 0;
        for (int i = 0; i < word.length(); i++) {
            char tmp = word.charAt(i);
            next = tmp - 'a';
            if (cur.nexts[next] == null) {
                cur.nexts[next] = new Node();
            }
            cur = cur.nexts[next];
            cur.pass++;
        }
        cur.end++;
    }

    public boolean search(String word) {
        if (word.length() == 0) {
            return false;
        }
        Node cur = root;
        int next = 0;
        for (int i = 0; i < word.length(); i++) {
            char tmp = word.charAt(i);
            next = tmp - 'a';
            if (cur.nexts[next] == null) {
                return false;
            }
            cur = cur.nexts[next];
        }
        if (cur.end > 0) {
            return true;
        }
        return false;
    }

    public boolean startsWith(String prefix) {
        if (prefix.length() == 0) {
            return false;
        }
        Node cur = root;
        int next = 0;
        for (int i = 0; i < prefix.length(); i++) {
            char tmp = prefix.charAt(i);
            next = tmp - 'a';
            if (cur.nexts[next] == null) {
                return false;
            }
            cur = cur.nexts[next];
        }
        return true;
    }
}

class Node {
    // 记录有多少个字符串经过了该节点
    public int pass;
    // 记录有多少个字符串以该节点结尾
    public int end;
    // 记录当前节点的子节点
    public Node[] nexts;

    public Node() {
        this.pass = 0;
        this.end = 0;
        nexts = new Node[26];
    }
}