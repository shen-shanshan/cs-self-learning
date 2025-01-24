import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class MaxQueue {

    // 记录所有被插入的值，用于进行 pop
    Queue<Integer> q;

    // 维护一个单调递减的双端队列
    Deque<Integer> d;

    public MaxQueue() {
        q = new LinkedList<Integer>();
        d = new LinkedList<Integer>();
    }

    public int max_value() {
        if (d.isEmpty()) {
            return -1;
        }
        // 双端队列的第一个元素即为当前队列的最大值
        return d.peekFirst();
    }

    public void push_back(int value) {
        // 若加入元素比 d 末尾的元素大，则从后依次弹出，直到遇到比 value 大的数为止，然后加入 value
        while (!d.isEmpty() && d.peekLast() < value) {
            d.pollLast();
        }
        d.offerLast(value);
        q.offer(value);
    }

    public int pop_front() {
        if (q.isEmpty()) {
            return -1;
        }
        int ans = q.poll();
        if (ans == d.peekFirst()) {
            d.pollFirst();
        }
        return ans;
    }
}
