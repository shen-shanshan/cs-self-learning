#include <iostream>
#include <string>

using namespace std;

int main()
{
    // 整型
    int a = 1;
    long b = 10;
    cout << sizeof(a) << endl; // 4，int类型的长度为4字节
    cout << sizeof(b) << endl; // 4，long类型的长度为4字节（windows系统）
    cout << (int)0.01 << endl; // 0，整型被编译后会被去掉小数点

    // 浮点型
    float c = 3e-3f;
    cout << c << endl; // 0.003
    float d = c + 5e-8f;
    cout << d << endl; // 0.00300005

    // 字符型
    char e = 'a';
    cout << e << endl;        // a
    cout << (int)e << endl;   // 97
    cout << (int)'A' << endl; // 65

    // 转义字符
    cout << "\\" << endl; // "\"

    // 字符串
    char str1[] = "abc";
    string str2 = "abc";
    cout << str1[1] << endl;    // b
    cout << str2[1] << endl;    // b
    cout << str2.at(1) << endl; // b

    // 布尔类型
    bool flag = 0;                // 0就代表false
    cout << sizeof(flag) << endl; // 1
    if (!flag)
    {
        cout << "flag: " << flag << endl; // flag: 0
    }
    flag = 3; // 只要不是0就代表true
    if (flag)
    {
        cout << "flag: " << flag << endl; // flag: 1（注意不是3哦！）
    }
    // 布尔类型本质是整形，只能依靠整数转换。
    // 当输入0.1时，被编译后会被直接去掉小数点，即0.1变0，因此还是代表false。
    flag = 0.1;
    if (flag)
    {
        cout << flag << endl; // 1
    }

    return 0;
}