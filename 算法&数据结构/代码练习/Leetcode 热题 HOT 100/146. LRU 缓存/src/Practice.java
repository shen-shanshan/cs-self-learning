import java.util.HashMap;

public class Practice {
}

class LRUCache {
    // LRU 当前大小与 LRU 容量
    private int size;
    private int capacity;

    // 双向链表
    class LRUNode {
        int key;
        int value;
        LRUNode prev;
        LRUNode next;

        public LRUNode() {
        }

        public LRUNode(int key, int value) {
            this.key = key;
            this.value = value;
        }

        public LRUNode(int key, int value, LRUNode prev, LRUNode next) {
            this.key = key;
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    LRUNode dumpHead;
    LRUNode dumpTail;

    // 哈希表
    HashMap<Integer, LRUNode> map = new HashMap<>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        // 创建头、尾伪节点
        dumpHead = new LRUNode();
        dumpTail = new LRUNode();
        dumpHead.next = dumpTail;
        dumpTail.prev = dumpHead;
    }

    public int get(int key) {
        // 查找缓存
        LRUNode cur = map.get(key);
        if (cur != null) {
            // 如果 key 存在于缓存中
            // 1.将当前节点移动到链表头部
            removeNode(cur);
            addToHead(cur);
            // 2.返回结果
            return cur.value;
        } else {
            // 如果 key 不存在，返回 -1
            return -1;
        }
    }

    public void put(int key, int value) {
        // 查找缓存
        LRUNode cur = map.get(key);
        if (cur != null) {
            // 如果 key 存在于缓存中
            // 1.更新节点值
            cur.value = value;
            // 2.将当前节点移动到链表头部
            removeNode(cur);
            addToHead(cur);
        } else {
            // 如果 key 不存在
            // 1.在链表头部插入新节点
            LRUNode node = new LRUNode(key, value);
            addToHead(node);
            // 2.将新数据加入哈希表
            map.put(key, node);
            size++;
            // 3.检查缓存容量，判断是否执行缓存置换
            if (size > capacity) {
                // 删除哈希表中的数据
                map.remove(dumpTail.prev.key);
                // 移除链表末尾的节点
                removeNode(dumpTail.prev);
                size--;
            }
        }
    }

    private void removeNode(LRUNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addToHead(LRUNode node) {
        node.prev = dumpHead;
        node.next = dumpHead.next;
        dumpHead.next.prev = node;
        dumpHead.next = node;
    }
}
