#include <iostream>

using namespace std;

int main()
{
    // 在堆区开辟数据
    int *p = new int(10);
    cout << *p << endl; // 10
    cout << p << endl;  // 0x1861b035d80

    // 释放堆内存
    delete p;
    cout << *p << endl; // 1538809600，p指向的内存已经被释放了，p变为了野指针
    cout << p << endl;  // 0x1861b035d80

    // 在堆区开辟数组
    // 返回的是这一片连续空间的首地址
    int *arr = new int[10];
    cout << *arr << endl; // 1538809600，？？？
    cout << arr << endl; // 0x1861b035d80

    // 释放数组，要加[]
    delete[] arr;

    return 0;
}