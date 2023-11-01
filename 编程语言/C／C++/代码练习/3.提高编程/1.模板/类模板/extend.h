#ifndef EXTEND_H
#define EXTEND_H

using namespace std;

// 类模板（基类）
template <class T>
class Base
{
public:
    T t;
};

// 1.当子类继承的父类是一个类模板时，子类在声明的时候，要指定出父类中T的类型
class Son1 : public Base<int>
{
public:
    Son1(int t);
    void print();
};

// 2.如果想灵活地指定出父类中T的类型，子类也需要变为类模板
template <class T1, class T2>
class Son2 : public Base<T1>
{
public:
    Son2(T1 t, T2 m);
    void print();
    T2 m;
};

#endif