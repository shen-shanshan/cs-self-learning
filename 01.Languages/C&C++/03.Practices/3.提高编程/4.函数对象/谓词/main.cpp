#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

class MyCompare // 类似于java中的comparator比较器
{
public:
    bool operator()(int val1, int val2)
    {
        return val1 > val2;
    }
};

template <typename T>
void print(const vector<T>& v)
{
    for (typename::vector<T>::const_iterator it = v.begin(); it != v.end(); it++)
    {
        cout << *it << " ";
    }
    cout << endl;
}

void myPrint(int val)
{
    cout << val << " ";
}

void test1()
{
    vector<int> v;

    v.push_back(1);
    v.push_back(4);
    v.push_back(2);
    v.push_back(3);
    v.push_back(5);

    // 默认为从小到大排序
    sort(v.begin(), v.end());
    print(v); // 1 2 3 4 5

    // 使用函数对象，改变算法策略，从大到小排序
    // 这里第3个参数需要传入的是一个函数对象，因此MyCompare后面需要加()，表示创建一个对象
    sort(v.begin(), v.end(), MyCompare());
    // 利用STL提供的遍历算法打印
    // 这里第3个参数需要的是一个函数的引用，因此myPrint后面不用加()
    for_each(v.begin(), v.end(), myPrint); // 5 4 3 2 1
}

int main()
{
    test1();
    return 0;
}