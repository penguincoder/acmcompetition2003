// 2003 ACM Mid-Central Regional Programming Contest
// Problem G: Fax Regions
// by Andy Harrington, Loyola University Chicago
/*
fax.java
Algorithm that shrinks the fax to one with sides O(runs) and calculate
 components conventionally then

Problem:  given fax width and run lengths, calculate the number of
 connected components.

1. Process runs initially
   a.  dropping full rows of same
   b.  if 1+2k full rows of different, drop the first 2k of these rows,
       remembering the previous row and k
2. Look at the increasing sequence of x coordinates where shifts between same
   and different take place, and remap these x coordinates onto
   0,1,2,....newWidth, maintaining the order.
3. Do the conversion to a grid that is at most 1*runs across by 4*runs.
4. Calculate connected components by a conventional recursive routine
5. In each case where 2k rows in a run of differences was removed, add
   k * (number of sideways transitions between black and white across the
   previous row)
   to the number of components, to get the total.

The program prints reduced grids to the screen if they are small enough.

 You may also test the direct algorithm on small datasets by running the
 program with a commandline parameter.  This displays the full grid
 solutions and writes the output to a file smallFax.out as long as the
 widths and heights are no more than 80.
 */


import java.io.*;
import java.util.*;
import java.awt.Point;

class fax {

  static final int MAXWIDTH = 1000000000;
  static final int END_INPUT = -1;
  static final int MAX_RUNS = 1000;
  static final int MAX_DATASETS = 25; // for testing
  static final char EMPTY = '.';
  static final char UNVISITED = '*';
  static final String compLabel =  // for screen display
           "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  static char[][] fillGrid(int h, int w, Point[] changeCoord, int runs) {
    // fill grid of h rows and w columns so first and last rows are empty
    // given the coordinates where sequences of sames and differents end
    int x, y;
    char grid[][] = new char[h][w];
    for (x = 0; x < w; x++)
      grid[h-1][x] = grid[0][x] = EMPTY;  // extra first and last row empty
    boolean matches = true;
    int i = 0;
    for (y = 1; y < h-1; y++)
      for (x = 0; x < w; x++) {
        if (changeCoord[i].x == x && changeCoord[i].y == y) {
          matches = !matches;
          i++;
        }
        grid[y][x] = matches ?
           grid[y-1][x] : (char)(EMPTY + UNVISITED - grid[y-1][x]);
      }
    return grid;
  }

  static int countComps(char[][] grid) {
    // return the numer of components in a grid of EMPTY and UNVISITED
    int h = grid.length - 1;
    int w = grid[0].length;
    int x, y;
    int comps = 0;
    for (y = 1; y < h; y++)
      for (x = 0; x < w; x++) {
        if (grid[y][x] == UNVISITED) {
          fillComp(grid, y, x, compLabel.charAt(comps % compLabel.length()));
          comps++;
        }
      }
    return comps;
  }

  static void fillComp(char[][] grid, int y, int x, char fillChar) {
    // recursively fill the unvisited component including (x, y) with fillChar
    grid[y][x] = fillChar;
    if (grid[y-1][x] == UNVISITED) fillComp(grid, y-1, x, fillChar);
    if (grid[y+1][x] == UNVISITED) fillComp(grid, y+1, x, fillChar);
    if (x > 0 && grid[y][x-1] == UNVISITED) fillComp(grid, y, x-1, fillChar);
    if (x < grid[0].length-1 && grid[y][x+1] == UNVISITED)
       fillComp(grid, y, x+1, fillChar);
  }

  static PrintWriter makeOutput(String name) {
    PrintWriter out = null;
    try {
      out = new PrintWriter(
              new BufferedWriter(
                new FileWriter(name)));
    } catch(Exception e) {
        System.out.println("can't open output");
    }
    return out;
  }

  public static void main(String[] arg) {
    String FILE = "fax";
    ACMIO in = new ACMIO(FILE + ".in");
    PrintWriter out = makeOutput(FILE+".out");

    if (arg.length > 0) {
      smallFax();
      return;
    }
    Point[] changeCoord = new Point[MAX_RUNS];
    int datasets = 0;

    int w = in.intRead();
    while ( w != END_INPUT) { // one dataset per loop
      datasets++;
      int y = 1;  // border at row 0
      int x = 0;
      int totRuns = in.intRead();
      if (totRuns > MAX_RUNS) System.out.println("Error -- runs: " + totRuns +
                                               " > " + MAX_RUNS + " :max runs");
      in.intRead();  // ignore runs per line -- put in for Pascal
      Vector delPairs = new Vector();

      for (int runs = 0; runs < totRuns; runs++) {
        int n = in.intRead();
        if (runs % 2 == 0) { // run of same
          if (n + x >= w) // never add more than one line
           y++;
        }
        else { // run of different
          int dy = (n+x)/w;
          if (dy > 3) { // guarantees all different for 3 full rows
            int killPairs = (dy/2) - 1;
            delPairs.add(new int[] {y, killPairs});
            dy -= 2*killPairs;
          }
          y += dy;
        }
        x = (x + n) % w;
        changeCoord[runs] = new Point(x, y);
      }

      if (x > 0) System.out.println("Error -- ending x is not 0 but " + x);

      // shift x coordinates so all x coordinates 0,1, ... w-1 (with new w)
      Point[] sorted = (Point[])changeCoord.clone();  // shallow clone important
      Arrays.sort(sorted, 0, totRuns,
                  new Comparator() {
                    public int compare(Object p1, Object p2) {
                      return ((Point)p1).x - ((Point)p2).x;
                    }
                    public boolean equals(Object p) {
                           return compare(this, p) == 0;
                    }
                  }
           );
      // testing line
      System.out.println("Dataset " + datasets + ":  Orig width :  " + w);

      int newX = 0, oldX = 0;
      for (int i = 0; i < totRuns; i++) {
        if (sorted[i].x > oldX) {
          newX++;
          oldX = sorted[i].x;
        }
        sorted[i].x = newX;  // also changes element of changeCoord
      }
      w = newX + 1;

      char[][] grid = fillGrid(y+1, w, changeCoord, totRuns);

      int comps = countComps(grid);
      //testing line
      System.out.print("Reduced grid:  " +
                         (y-1) + " X " + w + "  with " + comps + " components");
      for (int i = delPairs.size() - 1; i >= 0; i--) {
        int[] pair = (int[])(delPairs.get(i));
        y = pair[0];
        int changes = 0;
        for (x = 1; x < w; x++)
          if (grid[y][x-1] != grid[y][x])
            changes++;
        comps += (changes+1)*pair[1];
      }
      System.out.println(", final total: " + comps);  // testing line
      printGrid(grid);

      out.println(comps);
      w = in.intRead();
    } // end of dataset
    if (datasets > MAX_DATASETS) System.out.println("Error -- datasets: " +
           datasets + " > " + MAX_DATASETS + " : max runs"); //testing line
    out.close();
  }

  // The rest of the class is only used for testing //////////////////////////

  static void printGrid(char[][] g) {
    System.out.println();
    if (g.length > 80 || g[0].length > 80)
      System.out.println("Skipping big grid");
    else
      for (int r = 1; r < g.length - 1; r++)
        System.out.println(g[r]);
    System.out.println();
  }

// The last method smallFax is just to test small faxes with the obvious
// algorithm:  it is activated if fax is run with a commandline parameter.

  static void smallFax() {
    ACMIO in = new ACMIO("fax.in");
    PrintWriter out = makeOutput("smallFax.out");
    int datasets = 0;

    int w = in.intRead(); // next dataset
    while ( w <= 80) { // stop at big dataset
      datasets++;
      int totRuns = in.intRead();
      in.intRead();  // ignore runs per line -- put in for Pascal
      char[][] grid = new char[82][w]; // assume will be small
      for (int i = 0; i < w; i++)
        grid[0][i] = EMPTY;
      int y = 1;  // border at row 0
      int x = 0;
      boolean matches = true;
      for (int runs = 0; runs < totRuns; runs++) {
        int n = in.intRead();
        for ( ; n > 0 ; n--) {
          grid[y][x] = matches ?
                       grid[y-1][x] : (char)(EMPTY + UNVISITED - grid[y-1][x]);
          if (x == w-1) {
            x = 0;
            y++;
          }
          else x++;
        }
        matches = !matches;
      }
      if (x > 0) System.out.println("Error -- ending x is not 0 but " + x);
      char[][] fullGrid = new char[y+1][];
      System.arraycopy(grid, 0, fullGrid, 0, y);
      fullGrid[y] = fullGrid[0];
      int comps = countComps(fullGrid);

      System.out.println("Small Dataset "+datasets+ ": " +comps+ " components");
      printGrid(fullGrid);

      out.println(comps);
      w = in.intRead();
    } // end of dataset
    out.close();  // omits lines starting with first input width > 80
  } // end smallFax testing alternative
}
