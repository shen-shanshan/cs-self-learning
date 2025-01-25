#include <iostream>
#include <string>

using namespace std;

// 定义常量
#define MAX 100
const int MIN = 0;

int main()
{
    // 常量
    cout << "MAX is " << MAX << endl;
    cout << "MIN is " << MIN << endl;
    // 修改常量会怎么样？
    // MAX = 1000;  // error: lvalue required as left operand of assignment

    return 0;
}