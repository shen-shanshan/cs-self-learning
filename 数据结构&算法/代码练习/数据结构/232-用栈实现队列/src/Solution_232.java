import java.util.LinkedList;
import java.util.Stack;

class MyQueue {
    Stack<Integer> inStack;
    Stack<Integer> outStack;

    public MyQueue() {
        inStack = new Stack<Integer>();
        outStack = new Stack<Integer>();
    }

    public void push(int x) {
        inStack.push(x);
    }

    public int pop() {
        if (outStack.isEmpty()) {
            in2out();
        }
        return outStack.pop();
    }

    public int peek() {
        if (outStack.isEmpty()) {
            in2out();
        }
        return outStack.peek();
    }

    public boolean empty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }

    private void in2out() {
        while (!inStack.isEmpty()) {
            outStack.push(inStack.pop());
        }
    }
}

public class Solution_232 {
    public static void main(String[] args) {
        MyQueue mq = new MyQueue();

        mq.push(1);
        mq.push(2);
        mq.push(3);
        System.out.println(mq);

        int x = mq.pop();
        System.out.println(x);
        System.out.println(mq);

        int y = mq.peek();
        System.out.println(y);
        System.out.println(mq);

        boolean z = mq.empty();
        System.out.println(z);
    }
}
