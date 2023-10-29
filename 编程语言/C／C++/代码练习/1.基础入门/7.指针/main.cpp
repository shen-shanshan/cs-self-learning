#include <iostream>

using namespace std;

int main()
{
    int a = 10;
    // &：取地址
    int *p = &a;
    cout << sizeof(p) << endl; // 8，指针变量在32位系统中占4个字节，在64位系统中占8个字节
    // *：解引用
    cout << *p << endl; // 10

    // 空指针
    int *p1 = NULL; // 指向内存中编号为0的空间（不可以被访问），用于初始化指针变量
    // cout << *p1 << endl;

    // 常量指针
    // 指针的指向可以修改，但是指针指向的值不可以改
    const int *p2 = &a;
    // *p2 = 11; // error: assignment of read-only location '* p2'
    int b = 5;
    p2 = &b;
    cout << *p2 << endl; // 5

    // 指针常量
    // 指针的指向不可以改，但是指针指向的值可以改
    int* const p3 = &a;
    // p3 = &b; // error: assignment of read-only variable 'p3'
    *p3 = 111;
    cout << a << endl; // 111，a的值被修改了

    // 常量指针常量
    const int* const p4 = &a;

    // 指针和数组
    int arr[3] = {1,2,3};
    int *p5 = arr; // arr就是数组的首地址
    cout << p5 << endl; // 0x6f019ff7bc
    for (int i = 0; i < 3; i++)
    {
        cout << *p5++ << endl; // 先解引用取值，后执行++，指针后移
    }

    return 0;
}