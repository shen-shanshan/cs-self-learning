#include <iostream>

using namespace std;

int main()
{
    int num = 1;

    // 若case里没有写break，则程序会一直向下执行
    switch (num)
    {
    case 1:
        cout << 1 << endl;
    case 2:
        cout << 2 << endl;
    case 3:
        cout << 3 << endl;
        break;
    case 4:
        cout << 4 << endl;
        break;
    default:
        break;
    }
    // 1 2 3

    return 0;
}