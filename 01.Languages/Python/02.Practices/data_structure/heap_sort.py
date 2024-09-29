import heapq


class Student:
    def __init__(self, name: str, age: int, score: int):
        self.name = name
        self.age = age
        self.score = score

    # 重载 < 运算符
    def __lt__(self, other):
        if self.age != other.age:
            return self.age < other.age
        else:
            return other.score < self.score


if __name__ == '__main__':
    # 创建堆
    heap_stu = []
    heapq.heappush(heap_stu, Student("张三", 3, 100))
    heapq.heappush(heap_stu, Student("李四", 4, 80))
    heapq.heappush(heap_stu, Student("王五", 4, 90))

    # 先按年龄升序排序，再按分数降序排序
    heap_sort = sorted(heap_stu)
    print(heap_sort[0].name)  # 张三
    print(heap_sort[1].name)  # 王五
    print(heap_sort[2].name)  # 李四
