import java.util.*;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 并查集
 */
public class UnionSet<V> {

    // 值和节点的映射关系
    public Map<V, Node<V>> nodes;

    // 记录每个节点的父节点
    public Map<Node<V>, Node<V>> parents;

    // 记录一张表的大小（节点个数），只有代表节点会有该属性
    public Map<Node<V>, Integer> sizeMap;

    // 初始化：一开始每个节点各自作为一个集合
    public UnionSet(List<V> values) {
        nodes = new HashMap<>();
        parents = new HashMap<>();
        sizeMap = new HashMap<>();
        for (V cur : values) {
            Node<V> node = new Node<>(cur);
            nodes.put(cur, node);
            parents.put(node, node);
            sizeMap.put(node, 1);
        }
    }

    // 返回一个集合的代表结点
    // 给你一个节点，请你往上到不能再往上，把代表返回
    public Node<V> findFather(Node<V> cur) {
        Deque<Node<V>> stack = new LinkedList<>();
        while (cur != parents.get(cur)) {
            stack.push(cur);
            cur = parents.get(cur);
        }
        // cur == parents.get(cur)
        // cur 是当期集合的代表节点
        // 链条扁平化：
        // 将链上被遍历到的节点都挂到代表节点下（减少后续查询所需的时间）
        while (!stack.isEmpty()) {
            parents.put(stack.pop(), cur);
        }
        return cur;
    }

    // 查询两个节点是否属于同一个集合
    public boolean isSameSet(V a, V b) {
        return findFather(nodes.get(a)) == findFather(nodes.get(b));
    }

    // 合并两个元素所属的集合
    public void union(V a, V b) {
        // 找到当前元素所在集合的代表节点
        Node<V> aHead = findFather(nodes.get(a));
        Node<V> bHead = findFather(nodes.get(b));
        // 判断是否属于同一个集合
        if (aHead != bHead) {
            int aSetSize = sizeMap.get(aHead);
            int bSetSize = sizeMap.get(bHead);
            // 将小集合的代表节点的父节点设为大集合的代表节点
            Node<V> big = aSetSize > bSetSize ? aHead : bHead;
            Node<V> small = big == aHead ? bHead : aHead;
            parents.put(small, big);
            // 更新大集合的代表节点的集合大小为两个集合的大小之和
            sizeMap.put(big, aSetSize + bSetSize);
            // 在 sizeMap 中移除小集合的头节点
            sizeMap.remove(small);
        }
    }

}
