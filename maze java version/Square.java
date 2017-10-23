package Maze;

public class Square {
    String val;
    int f, g, h, row, col;
    Square parent;
    Square(String val, int row, int col){
        this.val = val;
        this.row = row;
        this.col = col;
    }
}
