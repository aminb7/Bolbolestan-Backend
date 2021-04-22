package com.IE_CA.CA5.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Course {
	private final String code;
	private final String classCode;
	private final String name;
	private final int units;
	private final String type;
	private final String instructor;
	private final int capacity;
	private final String[] prerequisites;
	private final ClassTime classTime;
	private final ExamTime examTime;

	private int numberOfStudents;
	private ArrayList<Student> waitingList;

	@JsonCreator
	public Course(@JsonProperty("code") String code, @JsonProperty("classCode") String classCode,
	              @JsonProperty("name") String name, @JsonProperty("units") int units,
	              @JsonProperty("type") String type, @JsonProperty("instructor") String instructor,
	              @JsonProperty("capacity") int capacity, @JsonProperty("prerequisites") String[] prerequisites,
	              @JsonProperty("classTime") ClassTime classTime, @JsonProperty("examTime") ExamTime examTime) {
		this.code = code;
		this.classCode = classCode;
		this.name = name;
		this.units = units;
		this.type = type;
		this.instructor = instructor;
		this.capacity = capacity;
		this.prerequisites = prerequisites;
		this.classTime = classTime;
		this.examTime = examTime;
		this.numberOfStudents = 0;
		this.waitingList = new ArrayList<>();
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.code;
	}

	public String getClassCode() {
		return classCode;
	}

	public String getInstructor() {
		return instructor;
	}

	public int getUnits() {
		return this.units;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public int getNumberOfStudents() {return this.numberOfStudents; }

	public ClassTime getClassTime() {
		return classTime;
	}

	public ExamTime getExamTime() {
		return examTime;
	}

	public String[] getPrerequisites() {
		return prerequisites;
	}

	public void incrementNumOfStudents() {
		numberOfStudents += 1;
	}

	public void decrementNumOfStudents() {
		numberOfStudents += 1;
	}

	public String getType() {
		return type;
	}

	public void addToWaitingList(Student student) {
		waitingList.add(student);
	}

	public ArrayList<Student> getWaitingList() {
		return waitingList;
	}

	public void updateWaitingList() {
		while (waitingList.size() != 0 && capacity > numberOfStudents) {
			Student student = waitingList.get(0);
			student.changeCourseSelectionType(code, CourseSelectionType.REGISTERED);
			waitingList.remove(0);
			incrementNumOfStudents();
		}
	}
}
