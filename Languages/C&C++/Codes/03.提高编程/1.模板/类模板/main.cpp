#include <iostream>

using namespace std;

// 类模板
template <class T1, class T2>
class Person
{
public:
    Person(T1 name, T2 age)
    {
        this->name = name;
        this->age = age;
    }
    T1 name;
    T2 age;
};

// 1.指定传入类型
void printPerson1(Person<string, int> &p)
{
    cout << p.name << endl;
    cout << p.age << endl;
}

// 2.参数模板化
template <class T1, class T2>
void printPerson2(Person<T1, T2> &p)
{
    cout << typeid(T1).name() << endl;
    cout << typeid(T2).name() << endl;
}

// 3.整个类模板化
template <class T>
void printPerson3(T &p)
{
    cout << typeid(T).name() << endl;
}

int main()
{
    Person<string, int> p("shen shanshan", 100);
    printPerson1(p);
    printPerson2(p); // NSt7__cxx1112basic_stringIcSt11char_traitsIcESaIcEEE i
    printPerson3(p); // 6PersonINSt7__cxx1112basic_stringIcSt11char_traitsIcESaIcEEEiE
}