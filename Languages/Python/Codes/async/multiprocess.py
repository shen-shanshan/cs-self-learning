import time
import multiprocessing
from multiprocessing import shared_memory  # 它允许多个进程直接读写同一块内存区域
from concurrent.futures import Future, ThreadPoolExecutor


NUM_TASKS = 64
# NUM_WORKERS = 8


def async_task(id):
    print("task: " + str(id))
    time.sleep(5)  # 5 seconds


if __name__ == "__main__":

    start_time = time.time()

    num_workers = multiprocessing.cpu_count()
    print("cpu count: " + str(num_workers))  # 8
    executor = ThreadPoolExecutor(max_workers=num_workers)

    future_list = []
    for i in range(NUM_TASKS):
        future = executor.submit(async_task, i + 1)
        # print(future)  # <Future at 0xffff7f956bc0 state=running>
        future_list.append(future)

    # Wait for all tasks to complete
    for future in future_list:
        future.result()

    end_time = time.time()
    print("end")
    print("Time: {:.2f}s".format(end_time - start_time))  # 40s = 64 / 8 * 5s
