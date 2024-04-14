#include <iostream>
#include <vector>
using namespace std;

void parseInput(vector<vector<int>>& matrix, int n, int maxX, int maxY){
    int x, y, value;
    for(int i = 0; i < n; i++){
        scanf("%d %d %d", &x, &y, &value);

        if(x <= maxX && y <= maxY){
            matrix[x][y] = max(matrix[x][y], value);
        }
        if(y <= maxX && x <= maxY){
            matrix[y][x] = max(matrix[y][x], value);
        }
    }
}

int calculate(vector<vector<int>> matrix, int maxX, int maxY){
    for (int x =  1; x <= maxX; x++) {
        for (int y = 1; y <= maxY; y++) {
            for (int i = 1; i < y; i++) {
                matrix[x][y] = max(matrix[x][y], matrix[x][i] + matrix[x][y-i]);
            }
            for(int i = 1; i < x; i++){
                matrix[x][y] = max(matrix[x][y], matrix[i][y] + matrix[x-i][y]);
            }
        }
    }
    return matrix[maxX][maxY];   
}

int main() {
    int maxX, maxY, n;
    scanf("%d %d %d", &maxX, &maxY, &n);

    vector<vector<int>> matrix (maxX + 1, vector<int>(maxY + 1, 0));

    parseInput(matrix, n, maxX, maxY);
    
    printf("%d\n", calculate(matrix, maxX, maxY));

    return 0;
}