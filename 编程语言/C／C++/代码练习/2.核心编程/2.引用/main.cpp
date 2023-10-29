#include <iostream>

using namespace std;

/**
 * @brief 利用引用让形参修饰实参，可以简化指针修改实参
 *
 * @param a
 * @param b
 */
void swap3(int &a, int &b)
{
    int tmp = a;
    a = b;
    b = tmp;
}

/**
 * @brief 返回a的别名
 *
 * @return int& a的别名
 */
int &test2()
{
    // 静态变量，存放在全局区
    // 该数据在程序结束后由系统释放
    static int a = 10;
    return a;
}

int main()
{
    int a = 10;

    // 引用必须要初始化
    // int &b; // error: 'b' declared as reference but not initialized

    // 给a起别名为b，a就是b
    int &b = a;

    int c = 20;
    b = c;

    cout << "a: " << a << endl; // a: 20，别名b赋值后，原名a也会变为别名赋值的值
    cout << "b: " << b << endl; // b: 20
    cout << "c: " << c << endl; // c: 20

    // 通过引用参数产生的效果同按地址传递是一样的，但引用的语法更清楚简单
    int d = 10;
    int e = 20;
    // 引用传递
    // 形参会修饰实参，实际发生了交换
    swap3(d, e);
    cout << "d: " << d << endl; // d: 20
    cout << "e: " << e << endl; // e: 10

    // 引用做函数的返回值
    int &ref = test2();
    cout << "ref: " << ref << endl; // ref: 10
    // 函数的返回值可以作为左值
    test2() = 1000;
    cout << "test2(): " << test2() << endl; // test2(): 1000

    return 0;
}