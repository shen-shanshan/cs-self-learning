#include <iostream>
#include <windows.h> // SetConsoleOutputCP()函数所在的库

using namespace std;

// 定义结构体
struct Student
{
    string name;
    int age;
    int score;
} s2;

/**
 * @brief 打印结构体的成员
 *
 * @param stu 结构体变量的常量指针
 */
void printStudent(const Student *stu)
{
    // 该结构体变量是只读的，可以防止外部修改其成员的值。
    // stu->age = 10; // 错
    cout << stu->name << endl;
    cout << stu->age << endl;
    cout << stu->score << endl;
}

int main()
{
    // cmd程序默认的编码格式为gbk，而C++程序的编码格式为utf-8。
    // 可以让cmd程序的编码变为utf-8
    SetConsoleOutputCP(65001);

    // 创建结构体变量
    // 方式一：
    struct Student s1;
    s1.name = "张三";
    s1.age = 10;
    s1.score = 66;
    // 方式二：
    s2 = {"李四", 20, 88};

    // 结构体指针
    struct Student *p = &s2;
    // 使用->访问成员
    cout << p->name << endl;
    cout << p->age << endl;
    cout << p->score << endl;

    // 打印结构体的成员
    printStudent(&s1);

    return 0;
}