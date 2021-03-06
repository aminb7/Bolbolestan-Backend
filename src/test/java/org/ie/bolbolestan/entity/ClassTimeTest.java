package org.ie.bolbolestan.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ClassTimeTest {

	@Test
	public void overlapsShouldReturnFalseWhenTwoClassTimesHaveTwoSameDaysAndNotOverlappingTimes() {
		final String days[] = {"Sunday", "Tuesday"};
		ClassTime first = new ClassTime(days, "16-17:30");
		ClassTime second = new ClassTime(days, "14-15:30");

		assertFalse(first.overlaps(second));
	}

	@Test
	public void overlapsShouoldReturnTrueWhenOneClassHasOnlyOneDayAndClassTimesHaveOneSameDaysAndSameHours() {
		final String firstDays[] = {"Sunday", "Tuesday"};
		final String secondDays[] = {"Sunday"};
		ClassTime first = new ClassTime(firstDays, "16-17:30");
		ClassTime second = new ClassTime(secondDays, "16-17:30");
		assertTrue(first.overlaps(second));
	}

	@Test
	public void overlapsShouldReturnFalseWhenTwoClassTimesHaveOneSameDayButDifferentHours() {
		final String days[] = {"Sunday"};
		ClassTime first = new ClassTime(days, "16-17:30");
		ClassTime second = new ClassTime(days, "14-15:30");
		assertFalse(first.overlaps(second));
	}

	@ParameterizedTest
	@CsvSource({"16-17:30,16-17:30", "16-17:30,17-17:30", "16-17:30,15-17:30", "16-17:30,15-17", "16-17:30,15-18:30",
			"16-17:30,17-18:30", "16-17:30,16-18:30", "16-17:30,16-17"})
	public void overlapsShouoldReturnTrueWhenTwoClassTimesHaveOverlapInOneDayAndClassHours(String firstTime,
	                                                                                       String secondTime) {
		final String firstDays[] = {"Sunday", "Tuesday"};
		final String secondDays[] = {"Sunday", "Wednesday"};
		ClassTime first = new ClassTime(firstDays, firstTime);
		ClassTime second = new ClassTime(secondDays, secondTime);
		assertTrue(first.overlaps(second));
	}

	@ParameterizedTest
	@CsvSource({"16-17:30,14-15:30", "16-17:30,17:30-18"})
	public void overlapsShouldReturnFalseWhenTwoClassTimesHaveNoOverlaps(String firstTime, String secondTime) {
		final String firstDays[] = {"Sunday", "Tuesday"};
		final String secondDays[] = {"Monday", "Wednesday"};
		ClassTime first = new ClassTime(firstDays, firstTime);
		ClassTime second = new ClassTime(secondDays, secondTime);
		assertFalse(first.overlaps(second));
	}
}