import heapq

if __name__ == '__main__':
    # 创建堆（默认是小根堆）
    heap_1 = []

    # 添加元素
    heapq.heappush(heap_1, 1)
    heapq.heappush(heap_1, 5)
    heapq.heappush(heap_1, 2)
    print(heap_1) # [1, 5, 2]

    # 根据列表创建堆
    heap_2 = [2, 3, 1, 4]
    heapq.heapify(heap_2)
    print(heap_2) # [1, 3, 2, 4]

    # 弹出堆顶元素
    print(heapq.heappop(heap_2)) # 1
    print(heapq.heappop(heap_2)) # 2
    print(heapq.heappop(heap_2)) # 3

    heap_3 = [7, 6, 3, 2, 9, 0, 1, 5, 4]
    # 返回堆中最大的n个元素
    print(heapq.nlargest(3, heap_3)) # [9, 7, 6]
    # 返回堆中最小的n个元素
    print(heapq.nsmallest(3, heap_3)) # [0, 1, 2]
