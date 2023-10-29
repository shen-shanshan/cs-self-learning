#include <iostream>

using namespace std;

int main()
{
    // 创建二维数组
    int matrix[3][3] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    cout << sizeof(matrix) << endl;       // 36
    cout << sizeof(matrix[0]) << endl;    // 12
    cout << sizeof(matrix[0][0]) << endl; // 4
    // 行数
    int m = sizeof(matrix) / sizeof(matrix[0]); // 3
    // 列数
    int n = sizeof(matrix[0]) / sizeof(matrix[0][0]); // 3

    // 遍历二维数组
    for (int i = 0; i < m; i++)
    {
        for (int j = 0; j < n; j++)
        {
            cout << matrix[i][j] << " ";
        }
        cout << endl;
    }

    return 0;
}