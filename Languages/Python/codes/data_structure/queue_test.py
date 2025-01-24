import collections
import queue

if __name__ == '__main__':
    # 普通列表可以作为队列，但从性能角度来看并不理想，因此不推荐在Python中用列表来实现队列（除非只处理少量元素）。
    queue_list = [1, 2, 3]
    # 入队
    queue_list.append(4)
    print(queue_list)  # [1, 2, 3, 4]
    # 出队
    print(queue_list.pop(0))  # 1

    # 先进先出队列（默认）
    queue_1 = queue.Queue()
    # 入队
    queue_1.put(1)
    queue_1.put(2)
    queue_1.put(3)
    # 出队
    print(queue_1.get())  # 1

    # 后进先出队列（栈）
    queue_2 = queue.LifoQueue()

    # 优先级队列（堆）
    queue_3 = queue.PriorityQueue()

    # 双端队列
    queue_4 = collections.deque()
    # 右边入队
    queue_4.append(1)
    queue_4.append(2)
    # 右边出队
    print(queue_4.pop())  # 2
    # 左边入队
    queue_4.appendleft(3)
    queue_4.appendleft(4)
    # 左边出队
    print(queue_4.popleft())  # 4
