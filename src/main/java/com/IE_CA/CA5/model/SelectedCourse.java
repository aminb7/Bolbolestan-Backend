package com.IE_CA.CA5.model;

public class SelectedCourse {
	private Course course;
	private CourseState state;

	public SelectedCourse(Course course) {
		this.course = course;
		this.state = CourseState.NON_FINALIZED;
	}

	public Course getCourse() {
		return course;
	}

	public CourseState getState() {
		return state;
	}

	public void setState(CourseState state) {
		this.state = state;
	}
}
