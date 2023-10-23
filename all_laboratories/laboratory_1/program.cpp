#include <iostream>

// The minimal Thread index is 11 in Java.
#define MIN_INDEX 11;

int main() {
	int input_Thread_ID;
	int index = MIN_INDEX;

	while (std::cin >> input_Thread_ID) {
		if (input_Thread_ID != index) {
			std::cout << input_Thread_ID;

			break;
		}
		else {
			index++;
		}
	}

	return 0;
}