#include <fstream>
#include <iostream>
using namespace std;
#define UNCOV  4
#define FLG    5
#define COV    6
#define MINE   9


int board[40][40], flags[40][40], visited[40][40];

int getInfo(int rows, int cols, int x, int y, int type) {
   int count = 0;
   if( x-1 >= 0 ){
	 if( y-1 >= 0 && board[x-1][y-1] == type) { count++; } 
	 if( y+1 < rows && board[x-1][y+1] == type) {count++; }
     if(board[x-1][y] == type) { count++; }
   }
   if( x+1 < cols ){
     if( y-1 >= 0 && board[x+1][y-1] == type) { count++; } 
	 if( y+1 < rows && board[x+1][y+1] == type) {count++; } 
     if(board[x+1][y] == type) { count++; }
   }
   
   if( y-1 >= 0 ){
     if(board[x][y-1] == type) { count++; }
   }
   if( y+1 < rows ){
     if(board[x][y+1] == type) { count++; }
   }
   

   return count;
}

int checkSquares(int rows, int cols) {
    int count=0;
    for(int i=0; i<rows; i++) {
      for(int j=0; j<cols; j++) {
        if(flags[i][j] == COV) { count++; }
      }
    }
  return count;
}

int checkPath(int rows, int cols, int x, int y) {
    visited[x][y] = true;
	//for each uncovered square
	int f = getInfo(rows, cols, x, y, FLG);
	int c = getInfo(rows, cols, x, y, COV);
	int m = getInfo(rows, cols, x, y, MINE);
	if( f == m ) {
		//uncover adjecent cells
						if( x-1 >= 0 )
					if(flags[x-1][y] != FLG ) { flags[x-1][y] = UNCOV; }
				if( x+1 < cols )
					if(flags[x+1][y] != FLG) { flags[x+1][y]= UNCOV; }
				if( y-1 >= 0 )
					if(flags[x][y-1] != FLG) { flags[x][y-1] = UNCOV; }
				if( y+1 < rows )
					if(flags[x][y+1] != FLG) { flags[x][y+1] = UNCOV;}
	}
	if( f + c == m) {
		//flag all covered cells
					if( x-1 >= 0 )
				if(flags[x-1][y] != UNCOV ) { flags[x-1][y] = FLG; }
			if( x+1 < cols )
				if(flags[x+1][y] != UNCOV) { flags[x+1][y]= FLG; }
			if( y-1 >= 0 )
				if(flags[x][y-1] != UNCOV) { flags[x][y-1] = FLG; }
			if( y+1 < rows )
				if(flags[x][y+1] != UNCOV) { flags[x][y+1] = FLG;}
	}
	for(int i=0; i<rows; i++) {
		for(int j=0; j<cols; j++) {
			if( flags[i][j] == UNCOV && visited[i][j] == false ) {
				//recursion
				return checkPath(rows, cols, i, j);
			}
		}
	}
	return checkSquares(rows,cols);
	return 1;
}






int main() {
     ifstream in("mines.in");
     ofstream out("mines.out");
     char temp;

     while(!in.eof()){
       int rows, cols;
       in >> rows;
       in >> cols;
       if( rows == 0 || cols == 0) { exit(0); }
	   cout << rows << " -- " << cols << endl;
       for(int i=0; i<rows; i++) {
         for(int j=0; j<cols; j++) {
			in >> temp;
			int a;
			switch(temp) {
				case '.':
				a=0;
				break;
				case 'M':
				a=MINE;
				break;
				default:
				a = (int) temp;
			}
			board[i][j] = a;
			}
       }//end nested for
	   
	   //debug
       for(int i=0; i<rows; i++) {
         for(int j=0; j<cols; j++) {
			 cout << getInfo(rows, cols, i, j, MINE);
		 }
		 cout << endl;
	   }
       
	   int minsqur = rows * cols;
       for(int i=0; i<rows; i++) {
         for(int j=0; j<cols; j++) {
		   //array we need
		   for(int a=0; a< rows; a++) {
			   for(int b=0; b<cols; b++) {
				   flags[a][b] = COV;
			   }
		   }
		   //another array we need
		   for(int a=0; a< rows; a++) {
			   for(int b=0; b<cols; b++) {
				   visited[a][b] = false;
			   }
		   }
		   //actual recursive call
		   if(board[i][j] != MINE) {
		     int c = checkPath(rows, cols, i, j);
		     if( c < minsqur ) { minsqur = c; }
		   }
	     }
       }
	   out << minsqur << endl;  

     }//end while(!eof)
}//end main
