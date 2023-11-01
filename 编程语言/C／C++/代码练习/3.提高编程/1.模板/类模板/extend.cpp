#include <iostream>

#include "extend.h"

using namespace std;

int main()
{
    Son1 s1(10);
    s1.print(); // 10

    Son2<string, int> s2("shen shanshan", 10);
    s2.print(); // shen shanshan 10

    return 0;
}

// 构造函数的实现
Son1::Son1(int t)
{
    this->t = t;
}

template <class T1, class T2>
Son2<T1, T2>::Son2(T1 t, T2 m)
{
    this->t = t;
    this->m = m;
}

// 成员函数的实现
void Son1::print()
{
    cout << t << endl;
}

template <class T1, class T2>
void Son2<T1, T2>::print()
{
    // cout << t << endl; // error: 't' was not declared in this scope
    cout << Base<T1>::t << endl;
    cout << m << endl;
}