#include <iostream>
#include <vector>
#include <iterator>

using namespace std;

void test1();
void test2();
void test3();
void test4();
void string_split(const string &str, const string &split, vector<string> &res);

int main()
{
    // test1();
    // test2();
    // test3();
    test4();

    return 0;
}

// 创建操作
void test1()
{
    string s1 = string();
    cout << s1 << endl; // ""

    // 使用字符串s初始化
    const char *chars = "abc"; // 注意：这里前面必须要加const
    string s2 = string(chars);
    cout << s2 << endl; // abc

    string s3 = string("def");
    cout << s3 << endl; // def

    // 使用n个字符c初始化
    string s4 = string(3, 's');
    cout << s4 << endl; // sss

    // 使用string对象初始化
    string s5 = string(s4);
    cout << s5 << endl; // sss
}

// 赋值操作
void test2()
{
    string s1;
    s1 = "hello world";
    cout << s1 << endl; // hello world

    string s2;
    s2.assign("hello world");
    cout << s2 << endl; // hello world

    string s3;
    s3.assign("hello world", 4);
    cout << s3 << endl; // hell

    string s4;
    s4.assign(5, 'a');
    cout << s4 << endl; // aaaaa
}

// 拼接操作
void test3()
{
    string s1 = "12345";
    s1 += "678";
    cout << s1 << endl; // 12345678

    string s2;
    s2.append(s1, 0, 5);
    cout << s2 << endl; // 12345
}

// 查找和替换
// find()、rfind()、replace()

// 比较操作
// str1.compare(str2) == 0

// 字符存取
// str[0] = ...
// str.at(0) = ...

// 插入和删除
// insert()、erase()

// 截取子串
// substr()

// 字符串分割
void test4()
{
    string str = "a-b-ccc-dd-efg";
    vector<string> strList;
    string_split(str, "-", strList);
    for (vector<string>::iterator it = strList.begin(); it != strList.end(); it++)
    {
        cout << *it << endl;
    }
}

void string_split(const string &str, const string &split, vector<string> &res)
{
    if (str == "")
    {
        return;
    }

    // 在字符串末尾也加入分隔符，方便截取最后一段
    string strs = str + split;
    size_t pos = strs.find(split); // 注意：这里的idx必须是size_t类型，不能是int类型！

    // 若找不到内容则字符串搜索函数返回npos
    while (pos != strs.npos)
    {
        string temp = strs.substr(0, pos);
        res.push_back(temp);
        // 去掉已分割的字符串，并在剩下的字符串中进行分割
        strs = strs.substr(pos + 1, strs.size());
        pos = strs.find(split);
    }
}