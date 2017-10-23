package Maze;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Maze {
    List<List <Square>> maze;
    int colSize;
    int rowSize;    
    Square start;
    Square goal;
    int goalCounter;
    List<Square> goals;
    int maxSpace;
    int plusSpace;
    
    Maze(String filename, int space){
        List<List <Square>> row = new ArrayList<>();
        goals = new ArrayList<>();
        try (FileInputStream in = new FileInputStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String line;
            int r = 0;
            while ((line = reader.readLine()) != null){
                List<Square> data = new ArrayList();
                for (int i = 0; i < line.length(); i++){
                    String val = line.charAt(i) + "";
                    Square s = new Square(val, r, i);
                    data.add(s);
                    if (val.equals("P")){
                        start = s;
                        goal = start;
                        goals.add(start);
                    } else if (val.equals(".")) {
                        goals.add(s);
                    }
                }
                row.add(data);
                r++;
            }
            in.close();
            reader.close();
            maze = row;
            rowSize = maze.size();
            colSize = maze.get(0).size();
            goalCounter = 1;
            plusSpace = space;
        } catch (IOException e){
            System.err.print("File does not exist");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    void display(){
        maxSpace = String.valueOf(goals.size()).length() + plusSpace;
        for (List<Square> x: maze){
            for (int i = 0; i < x.size(); i++){
                String str = x.get(i).val;
                int space = maxSpace - str.length();
                System.out.print(str);
                for (int s = 0; s < space; s++){
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
    
    Square get(int row, int col){
        return maze.get(row).get(col);
    }
    
    List<Square> neighbor(Square sq){
        int row = sq.row;
        int col = sq.col;
        int mRow = maze.size();
        int mCol = maze.get(0).size();
        List<Square> l = new ArrayList<>();
        if (row - 1 >= 0){
            l.add(maze.get(row - 1).get(col));
        }
        if (col + 1 < mCol){
            l.add(maze.get(row).get(col + 1));
        }
        if (row + 1 <mRow){
            l.add(maze.get(row + 1).get(col));
        }
         if (col - 1 >= 0){
            l.add(maze.get(row).get(col - 1));
        }
        return l;
    }
    
    void nextGoal(String heuristic){
        start = goal;
        removeGoal(start);
        start.parent = null;
        Square g = null;
        if (!goals.isEmpty()){
            g = goals.get(0);
            goal = g;
            int min = hCost(heuristic, start);
            for(Square s: goals){
                goal = s;
                int cost = hCost(heuristic, start);
                if (cost < min){
                    min = cost;
                    g = s;
                }
            }
        }
        goal = g;
    }
    
    void removeGoal(Square s){
        goals.remove(s);
    }
    
    int manhattanD(int x1,int y1){
        return Math.abs(x1 - goal.row) + Math.abs(y1 - goal.col);
    }
    
    int straightLineD(int x1, int y1){
        return Math.max(Math.abs(x1 - goal.row), Math.abs(y1 - goal.col));
    }
    
    int hCost(String hueristics,Square sq){
        int x1 = sq.row;
        int y1 = sq.col;
        if (hueristics.equals("manhattan")){
            return manhattanD(x1, y1);
        } else if (hueristics.equals("straightLine")){ 
            return straightLineD(x1, y1);
        } else {
            System.err.println("Incorrect heuristic");
            return 0;
        }
    }
}
