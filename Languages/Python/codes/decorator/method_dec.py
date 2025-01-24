import time


def logger(type: str):

    # type = "default"

    def decorator(f):

        def inner(*arg, **kwargs):
            if type == "default":
                print("start.")
                n = f(*arg, **kwargs)
                print("end.")
                return n
            else:
                print("error: wrong type.")
                return 0
        
        return inner
    
    return decorator  # 返回结果是一个可调用对象，它接受一个函数作为参数并包装它


@logger("default")
def test(n, dictp=0):
    for i in range(n):
        print("test " + str(i + 1))
        time.sleep(1)
    print("dictp = " + str(dictp))
    return n


if __name__ == '__main__':
    n = test(3, dictp=5)
    print("n = " + str(n))
