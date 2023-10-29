#include <iostream>
#include <windows.h>

#include "main.h"

using namespace std;

int main()
{
    SetConsoleOutputCP(65001); // 可以让cmd程序的编码变为utf-8

    // 1.括号法
    Person p1;     // 默认构造函数
    Person p2(10); // 有参构造函数
    Person p3(p2); // 拷贝构造函数

    // 2.显示法
    Person p4 = Person(10); // 有参构造函数
    Person p5 = Person(p1); // 拷贝构造函数

    // 匿名对象：当前行执行结束后，该对象就会被系统回收
    Person(10);
    // Person(p1); // error: redeclaration of 'Person p1'，不要用拷贝构造函数初始化匿名对象，编译器会认为：Person(p1) === Person p1;

    // 3.隐式转换法
    Person p6 = 10; // 有参构造方法，等价于：Person p6 = Person(10);
    Person p7 = p6; // 拷贝构造方法

    // 注意：这里的p1-p7都是局部变量（是在栈上创建的），不是在堆上创建的（需要用new），因此在main()函数运行结束后都会被释放，会依次调用它们的析构函数。

    return 0;
}

// 无参构造函数
Person::Person()
{
    cout << "这是无参构造函数" << endl;
    this->age = 0;
}

// 有参构造函数
Person::Person(int age)
{
    cout << "这是有参构造函数" << endl;
    this->age = age;
}

// 拷贝构造函数
Person::Person(const Person &p)
{
    cout << "这是拷贝构造函数" << endl;
    age = p.age;
}

// 析构函数
Person::~Person()
{
    cout << "这是析构函数" << endl;
}