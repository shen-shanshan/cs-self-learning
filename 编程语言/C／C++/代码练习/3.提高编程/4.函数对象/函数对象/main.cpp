#include <iostream>

using namespace std;

class MyAdd
{
public:
    // 重载函数调用操作符()
    int operator()(int v1, int v2)
    {
        return v1 + v2;
    }
};

void test1()
{
    MyAdd myAdd;
    cout << myAdd(1, 2) << endl;
}

class MyPrint
{
public:
    MyPrint()
    {
        this->count = 0;
    }

    void operator()(string str)
    {
        cout << str << endl;
        this->count++;
    }

    int count; // 函数对象可以有自己的状态
};

void test2(int count)
{
    MyPrint myPrint;
    for (int i = 0; i < count; i++)
    {
        myPrint("hello world");
    }
    cout << myPrint.count << endl; // 输出该函数被调用的次数
}

int main()
{
    cout << "test1:" << endl;
    test1();

    cout << "test2:" << endl;
    test2(5);

    return 0;
}