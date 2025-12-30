import time
import os
import torch_npu


def bytes_to_gb(x):
    return x / 1024 ** 3


def clear():
    os.system("clear" if os.name == "posix" else "cls")


def print_header():
    print("+------+------------+------------+------------+------------+")
    print("| NPU  | Alloc (GB) | Reserv (GB)| Free (GB)  | Total (GB) |")
    print("+------+------------+------------+------------+------------+")


def print_row(npu_id, alloc, reserv, free, total):
    print(
        f"| {npu_id:^4} |"
        f" {alloc:>10.2f} |"
        f" {reserv:>10.2f} |"
        f" {free:>10.2f} |"
        f" {total:>10.2f} |"
    )


def monitor(interval=1):
    npu_count = torch_npu.npu.device_count()

    while True:
        clear()
        print("Ascend NPU Memory Monitor (torch_npu)")
        print(time.strftime("%Y-%m-%d %H:%M:%S"))
        print()

        print_header()
        for i in range(npu_count):
            torch_npu.npu.set_device(i)

            alloc = bytes_to_gb(torch_npu.npu.memory_allocated())
            reserv = bytes_to_gb(torch_npu.npu.memory_reserved())
            free, total = torch_npu.npu.mem_get_info()
            free = bytes_to_gb(free)
            total = bytes_to_gb(total)

            print_row(i, alloc, reserv, free, total)

        print("+------+------------+------------+------------+------------+")
        time.sleep(interval)


if __name__ == "__main__":
    monitor(interval=1)
