package com.kh388;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class StudentManagementSystemApplicationTests {

	@Test
	void itShouldAddTwoNumbers() {

		Calculator underTest = new Calculator();

		// given
		int numberOne = 20;
		int numberTwo = 30;

		// when
		int result = underTest.add(numberOne, numberTwo);

		// then
		int expected = 50;
		assertThat(result).isEqualTo(expected);

	}

	class Calculator {
		int add(int a, int b) {
			return a + b;
		}
	}


}
