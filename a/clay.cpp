#include <iostream>
#include <fstream>
#include <string>

using namespace std;

int main(int argc, char **argv)
{
	int class_size = 0;
	int i = 0, j = 0;
	int height = 0, width = 0, depth = 0;
	int min = 0, max = 0;
	string *students;
	int *clay;
	ifstream clay_in;
	ofstream clay_out;

	clay_in.open("clay.in");
	clay_out.open("clay.out");

	clay_in >> class_size;
	while (class_size != -1) {
		students = new string[class_size];
		clay = new int[class_size];
		min = 0;
		max = 0;

		for(i = 0; i < class_size; i++) {
			clay_in >> height;
			clay_in >> width;
			clay_in >> depth;
			clay_in >> students[i];
			clay[i] = height * width * depth;
		}

		for(i = 0; i < class_size; i++) {
			if (clay[i] < clay[min]) {
				min = i;
			}
			if (clay[i] > clay[max]) {
				max = i;
			}
		}

		clay_out << students[max] << " took clay from " << students[min] << ".\n";

		clay_in >> class_size;
	}

	clay_in.close();
	clay_out.close();

	return 0;
}
