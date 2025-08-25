import asyncio
import time


async def compute():  # 使用 async 定义异步（协程）函数
    """
    await:
    等到对象的结果返回，才会继续执行后续的代码
    使用方式: await + 可等待对象（如：协程，Future，Task（IO等待））
    """
    await asyncio.sleep(5)
    print('compute finished.')


if __name__ == '__main__':
    print(f"started at {time.strftime('%X')}")

    # print(compute())  # <coroutine object compute at 0x0000026AC3E65440>，执行协程函数得到协程对象

    """
    asyncio event loop:
    所谓的事件循环，并非是一个真正意义上的循环，可以理解为是主线程不断的从事件队列里面取值/函数的过程。
    因为这一过程是不断发生的所以我们为了方便把这个过程叫事件循环。
    """
    tasks = [compute(), compute()]
    loop = asyncio.get_event_loop()
    loop.run_until_complete(asyncio.wait(tasks))  # 两个 compute 任务同时并行执行，总共耗时 5s
    # asyncio.run(tasks)

    print(f"started at {time.strftime('%X')}")
