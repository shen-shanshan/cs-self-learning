if __name__ == '__main__':
    # 可以直接使用列表来模拟栈
    stack = []
    # 压栈
    stack.append(1)
    stack.append(2)
    stack.append(3)
    print(stack)  # [1, 2, 3]
    # 弹栈
    print(stack.pop())  # 3
    # 获取栈顶元素
    print(stack[-1])  # 2
    # 判空
    if len(stack) != 0:
        print("stack is not empty")
