package Maze;
import java.io.*;
import java.util.*;

public class MazeSolver {
    static int frontier = 0;
    
    public static void main(String[] args) {
        String fileName = "others/bigSearch.lay.txt";
        int space = 2;
        String a = "straightLine";
        String b = "manhattan";
        String heuristic = b;
        Maze x = new Maze(fileName, space);
        System.out.println("The Maze:");
        x.display();
        solve(x, heuristic);
    }
    
    public static int solveMaze(Maze maze, String hueristics){
        List<Square> closedList = new ArrayList<>();
        List<Square> openList = new ArrayList<>();
        Square current = maze.start;
        current.g = 0;
        current.h = maze.hCost(hueristics, current);
        current.f = current.g + current.h;
        openList.add(current);
        while(!closedList.contains(maze.goal) && !openList.isEmpty()){
            current = minCost(openList); 
            closedList.add(current);
            openList.remove(current);
            List<Square> neighbor = maze.neighbor(current);
            for (Square x: neighbor){
                if (!x.val.equals("%") && !closedList.contains(x)){
                    if (openList.contains(x)){
                        int tempG = current.g + 1;
                        if (x.g > tempG){
                            x.g = tempG;
                            x.parent = current;
                            x.f = (x.g + x.h);
                        }
                    } else {
                        x.g = current.g + 1;
                        x.parent = current;
                        x.h = maze.hCost(hueristics, x);
                        x.f = (x.g + x.h);
                        openList.add(x);
                    }
                }
            }
        }
        if (closedList.contains(maze.goal)){
            frontier += closedList.size() + openList.size();
            return closedList.size();
        } else {
            System.out.println("Failed to find the target square at " + maze.goal.row + "," + maze.goal.col);
            return 0;
        }
    }
   
    public static void solve(Maze m, String heuristic){
        int nodesExpanded = 0;
        int pathCost = 0;
        m.nextGoal(heuristic);
        System.out.println("");
        System.out.println("Path Coordinates");
        while(m.goal != null){
            nodesExpanded += solveMaze(m, heuristic);
            pathCost += trace(m);
            m.nextGoal(heuristic);
        }
        System.out.println("");
        m.display();
        System.out.println("");
        System.out.println("Heuristics: " + heuristic);
        System.out.println("Nodes Expanded: " + nodesExpanded);
        System.out.println("Path Cost: " + pathCost);
        System.out.println("Frontier Size: " +frontier );
        
    }
    
    public static int trace(Maze m){
        Square current = m.goal;
        int pathCost = m.goal.g;
        List<Square> goalsPassed = new ArrayList<>();
        List<Square> path = new ArrayList<>();
        while(current.parent != null){
            if (current.val.equals(".")) {
                goalsPassed.add(current);
                m.removeGoal(current);
            } else if (current.val.equals(" ")){
                current.val = "-";
            }
            path.add(current);
            current = current.parent;
        }
        for (int i = goalsPassed.size() - 1; i >= 0; i--){
            goalsPassed.get(i).val = m.goalCounter++ + "";
        }
        for (int i = path.size() - 1; i >= 0; i--){
            Square s = path.get(i);
            System.out.print(s.row + "," + s.col + " ");
        }
        System.out.println("");
        return pathCost;
    }
    
    public static Square minCost(List<Square> l){
        Square min = l.get(0);
        for (Square sq: l){
            if (sq.f < min.f){
                min = sq;
            }
        }
        return min;
    }
}
