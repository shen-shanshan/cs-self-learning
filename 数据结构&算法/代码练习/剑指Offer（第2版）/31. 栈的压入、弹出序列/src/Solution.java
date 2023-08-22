import java.util.Deque;
import java.util.LinkedList;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        if (pushed.length == 0) {
            return true;
        }
        Deque<Integer> stack = new LinkedList<>();
        int push = 0;
        int pop = 0;
        while (push < popped.length) {
            if (pushed[push] != popped[pop]) {
                // 入栈
                stack.push(pushed[push]);
                push++;
            } else {
                // 弹栈
                push++;
                pop++;
                while (!stack.isEmpty() && stack.peek() == popped[pop]) {
                    stack.pop();
                    pop++;
                }
            }
        }
        while (!stack.isEmpty() && pop < popped.length && stack.peek() == popped[pop]) {
            stack.pop();
            pop++;
        }
        return stack.isEmpty() ? true : false;
    }
}
