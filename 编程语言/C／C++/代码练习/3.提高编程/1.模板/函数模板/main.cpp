#include <iostream>

using namespace std;

// 函数模板
template <typename T>
void getSum(T &a, T &b)
{
    cout << a + b << endl;
    cout << "template method" << endl;
}

// 函数模板（只读）
template <typename T>
void getSum2(const T &a, const T &b)
{
    cout << a + b << endl;
}

// 普通方法
void getSum(int &a, int &b)
{
    cout << a + b << endl;
    cout << "simple method" << endl;
}

void test1();
void test2();

int main()
{
    // test1()
    test2();
    return 0;
}

void test1()
{
    int a = 1;
    int b = 2;

    getSum(a, b);
    getSum<int>(a, b);

    // 函数模板调用时，如果利用自动类型推导，不会发生隐式类型转换
    // 自动类型推导：必须推导出一致的数据类型T，才可以使用
    char c = 'c';
    // getSum(a, c); // error: no matching function for call to 'getSum(int&, char&)'

    // 如果利用显示指定类型的方式，可以发生隐式类型转换
    // getSum<int>(a, c); // error: cannot bind non-const lvalue reference of type 'int&' to a value of type 'char'
    cout << (int)c << endl; // 99
    getSum2<int>(a, c);     // 100
}

void test2()
{
    int a = 1;
    int b = 2;
    getSum(a, b); // 3 simple method
}