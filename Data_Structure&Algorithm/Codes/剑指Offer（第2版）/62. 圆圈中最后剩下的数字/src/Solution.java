import java.util.LinkedList;
import java.util.List;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    // 暴力：超时
    public int lastRemaining(int n, int m) {
        // 使用链表存储数据
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        // 依次弹出数字
        int index = 0;
        while (n > 1) {
            // 计算位置
            index = (index + m - 1) % n;
            list.remove(index);
            n--;
            index = index % n;
        }
        return list.get(0);
    }

}
