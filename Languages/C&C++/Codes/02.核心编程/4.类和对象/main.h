#ifndef MAIN_H
#define MAIN_H

#include <iostream>

using namespace std;

// 类结构的声明
class Person
{
private:
    int age; // 成员变量

public:
    Person();                // 无参构造函数
    Person(int age);         // 有参构造函数
    Person(const Person &p); // 拷贝构造函数
    ~Person();               // 析构函数
};

#endif