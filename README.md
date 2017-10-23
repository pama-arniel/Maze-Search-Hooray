## Maze-Search-Hooray
> An A* search implementation

1. HERE IS THE MAIN FUNCTION OF THE PYTHON A* SEARCH IMPLEMENTATION

'''
def main():
    filename = "bigSearch.lay.txt"
    #heuristics = "manhattan"
    heuristics="straightline"
    maze = readmaze(filename)

    print("The Maze:")
    maze.display(spaces=1)
    solve(maze, heuristics)
'''

2. TO RUN THE PROGRAM, SIMPLY ALTER THE THE "filename" and
"heuristics" value:

Possible filename values: "bigMaze.lay.txt", "smallSearch.lay.txt", etc.

Possible heuristics value: "straightline" and "manhattan" ONLY
NOTE: typing other string values would throw an Exception

3. Here is why:

'''
def calcHeuristics(heuristics, maze, square, goal):
    .
    .
    .
    if(heuristics=="manhattan"):
        return (dx + dy)
    elif(heuristics=="straightline"):
        return max(dx, dy)
    else:
        raise Exception("Incorrect heuristics")
    return 0
'''

4. TO RUN, YOU CAN ALSO SIMPLY COMMENT OUT ONE OF THE TWO:
'''
    #heuristics = "manhattan"
    heuristics="straightline"
'''

5. Make sure the file exists.
