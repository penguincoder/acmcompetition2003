#include <fstream>
#include <iostream>
using namespace std;

int main() {
	ifstream infile("doubles.in");
	ofstream outfile ("doubles.out");

	int innum, count;

	infile >> innum;
	while(innum != -1) {
		int size=0;
		int set[15];
		while(innum != 0) {
			set[size++] = innum;
			infile >> innum;
		}

		int count = 0;
		for(int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {
				if( set[x] == set[y] * 2 ) {
					count++;
					break;
				}
			}
		}
		outfile << count << endl;

		infile >> innum;
	}
	outfile.flush();
	outfile.close();
	exit(0);
}