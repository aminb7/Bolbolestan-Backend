package com.IE_CA.CA5.model;

public class SelectedCourse {
	private Course course;
	private CourseState state;
	private CourseSelectionType courseSelectionType;

	public SelectedCourse(Course course, CourseState state, CourseSelectionType courseSelectionType) {
		this.course = course;
		this.state = state;
		this.courseSelectionType = courseSelectionType;
	}

	public Course getCourse() {
		return course;
	}

	public CourseState getState() {
		return state;
	}

	public CourseSelectionType getCourseSelectionType() {
		return courseSelectionType;
	}

	public void setState(CourseState state) {
		this.state = state;
	}

	public void setCourseSelectionType(CourseSelectionType courseSelectionType) {
		this.courseSelectionType = courseSelectionType;
	}
}
