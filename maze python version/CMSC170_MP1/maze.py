import sys

def main():
    filename = "bigSearch.lay.txt"
    #heuristics = "manhattan"
    heuristics="straightline"
    maze = readmaze(filename)

    print("The Maze:")
    maze.display(spaces=1)
    solve(maze, heuristics)

def readmaze(filename):
    """
        str -> Maze
        loads a .txt file and returns a Maze
    """
    try:
        with open(filename) as fp:
            maze = []    
            row = 0 #row number
            for line in fp:
                currline = []
                col = 0 #column number
                for ch in line:
                    if(ch != '\n'):
                        sq = Square(ch, row, col)
                        currline.append( sq )
                        col += 1
                maze.append(currline)
                row += 1
        return Maze(maze)
    except IOError:
        print('Error: File does not appear to exist')

class Square:
    val = ''
    f = 0
    g = 0
    h = 0
    row = 0
    col = 0
    parent = None
    name = ''
    
    def __init__(self, val, row, col):
        self.val = val
        self.row = row
        self.col = col
        self.name = str(row) + "." + str(col)

    def isinlist(self, givenlist):
        """
            (Square, list of Square) -> bool
            checks if a square is in a list
        """
        for el in givenlist:
            if (self.equals(el)):
                return True
        return False
    
    def equals(self, other):
        if (other.row==self.row) and (other.col==self.col):
                return True
        return False
        
class Maze:
    maze = [[]]
    colsize = 0
    rowsize = 0
    start = None
    goals = []
    numgoals = 0

    def __init__(self, maze):
        self.maze = maze
        self.rowsize = len(maze)
        self.colsize = len(maze[0])
        for arr in maze:
            for i in range(0, self.colsize):
                if( arr[i].val == 'P'):
                    self.start = arr[i]
                    self.start.parent = None
                elif ( arr[i].val == '.'):
                    self.goals.append(arr[i])
        self.numgoals = len(self.goals)

    def display(self, spaces):
        maxspaces = spaces
        for arr in self.maze:
            for square in arr:
                #for proper space formatting
                print square.val.ljust(maxspaces),
            print
        print

    def get(self, row, col):
        return self.maze[row][col]

    def goalsinlist(self, squarelist):
        for goal in self.goals:
            if (goal.isinlist(squarelist)):
                return True
        return False


class Path:
    path = []
    pathcost = 0
    expanded = 0
    frontier = 0
    
    def __init__(self, path, pathcost, expanded, frontier):
        self.path = path
        self.pathcost = pathcost
        self.expanded = expanded
        self.frontier = frontier
       
def findpath(maze, heuristics, start, goal):
    """
        (Maze, str, Square, Square) -> Path
        returns a Path object with the path,
        pathcost, expanded, and frontier details
    """
    closedlist = []
    openlist = []

    currSq = start #current square
    openlist.append(currSq)
    
    while(len(openlist)):
        currSq = mincost(openlist)
        closedlist.append(currSq)
        openlist.remove(currSq)

        if(currSq.equals(goal)):
            break

        neighbors = neighbor(currSq, maze)
        for sq in neighbors:
            if(sq.val != '%' and not sq.isinlist(closedlist)):
                if(sq.isinlist(openlist)):
                    tempG = currSq.parent.g + 1
                    if(sq.g > tempG):
                        sq.g = tempG
                        sq.parent = currSq
                else:
                    sq.parent = currSq
                    sq.g = sq.parent.g + 1
                    openlist.append(sq)

                sq.h = calcHeuristics(heuristics, maze, sq, goal)
                sq.f = sq.h + sq.g
            
    return pathDetails(maze, closedlist, openlist, start, goal)

def pathDetails(maze, closedlist, openlist, start, goal):
    """
        Maze, list, list, int, Square, Square -> Path
        returns a Path object  with the path,
        pathcost, expanded, and frontier details
    """
    path = []
    lenclosedlist = len(closedlist)
    curr = closedlist[lenclosedlist-1]

    if(curr.equals(goal)):
        while(not curr.equals(start)):
            path.append(curr)
            curr = curr.parent
        path.append(curr)
    else:
        print("Goal not found!")

    maxfrontier = len(openlist) + lenclosedlist
    return Path(path, path[0].g, lenclosedlist, maxfrontier)
        

def calcHeuristics(heuristics, maze, square, goal):
    """
        (str, Maze, Square) -> num
        calculates the heuristics based on provided type
    """
    x1 = square.row
    y1 = square.col
    x2 = goal.row
    y2 = goal.col
    dx = abs(x1 - x2)
    dy = abs(y1 - y2)
    if(heuristics=="manhattan"):
        return (dx + dy)
    elif(heuristics=="straightline"):
        return max(dx, dy)
    else:
        print("Incorrect heuristics")
    return 0

def mincost(squares):
    """
        list of Square -> Square
        returns the square with the minimum f cost
    """
    minimum = squares[0]
    for sq in squares:
        if (sq.f < minimum.f):
            minimum = sq
    return minimum

def neighbor(square, maze):
    """
        (Square, Maze) -> list of Square
        returns a list of the neighbors of the given square
    """
    res = []
    row = square.row
    col = square.col
    
    if((row - 1) >= 0): #up
        res.append( maze.get(row-1,col) )
    if((col + 1) < maze.colsize): #right
        res.append( maze.get(row,col+1) )
    if((row + 1) < maze.rowsize): #down
        res.append( maze.get(row+1,col) )  
    if((col - 1) >= 0): #left
        res.append( maze.get(row,col-1) )    
    
    return res

def solve(maze, heuristics):
    """
        (Maze, str) -> void
        finds the path then sets the values of the squares in the path to '.'
        to mean that they were visited
    """
    
    paths = []
    start = maze.start
    goals = maze.goals
    goalnumber = 1
    
    while(len(goals)):
        pathcost = sys.maxsize
        mingoal = goals[0]
        path = None
        
        for goal in goals:
            currpath = findpath(maze, heuristics, start, goal)
            currpathcost = currpath.pathcost

            if(currpathcost < pathcost):
                pathcost = currpathcost
                mingoal = goal
                path = currpath

        start = mingoal
        paths.append(path)
        maze.maze[mingoal.row][mingoal.col].val = str(goalnumber)
        goalnumber += 1
        goals.remove(mingoal)

    #display details
    expanded = 0
    frontier = 0
    
    print "Paths: "
    for path in paths:
        expanded += path.expanded
        frontier += path.frontier
        squares = list(reversed(path.path))

        for sq in squares:
            if(sq.val == ' '):
                sq.val = '.' #equal to maze.maze[sq.row][sq.col].val = '.'
            print sq.name,
        print
    print
    
    print "\nThe Solution:"
    maze.display(len(str(goalnumber)))
    print "Heuristics:", heuristics
    print "Path cost:", pathcost #last set pathcost above is the actual pathcost
    print "Nodes expanded:", expanded
    print "Frontier size:", frontier, "\n"

if __name__ == "__main__":
    main()
