import java.util.LinkedList;
import java.util.Queue;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 189.轮转数组
 * 总结：
 * 消灭的策略是，尽量消灭自己后面的对手。
 * 因为前面的对手已经使用过权利了，而后序的对手依然可以使用权利消灭自己的同伴。
 * 因此应该贪心地挑选按照投票顺序的下一名敌方的议员
 */
public class Solution189 {
    public String predictPartyVictory(String senate) {
        int n = senate.length();

        Queue<Integer> radiant = new LinkedList<Integer>();
        Queue<Integer> dire = new LinkedList<Integer>();

        // 按照投票顺序存储天辉方和夜魇方每一名议员的投票时间（即下标位置）
        for (int i = 0; i < n; i++) {
            if (senate.charAt(i) == 'R') {
                radiant.offer(i);
            } else {
                dire.offer(i);
            }
        }

        while (!radiant.isEmpty() && !dire.isEmpty()) {
            // 弹出队首元素
            int radiantIndex = radiant.poll();
            int direIndex = dire.poll();
            if (radiantIndex < direIndex) {
                radiant.offer(radiantIndex + n);
            } else {
                dire.offer(direIndex + n);
            }
        }

        return !radiant.isEmpty() ? "Radiant" : "Dire";
    }
}
