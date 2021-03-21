package com.example.CA3;

import com.example.CA3.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "Courses", value = "/courses")
public class CoursesServlet extends HttpServlet {
	private static String message;

	public static String getMessage() {
		return message;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.getRequestDispatcher("/courses.jsp").forward(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		BolbolestanApplication app = BolbolestanApplication.getInstance();
		if (app.getLoggedInStudentId().equals("")) {
			response.sendRedirect("/login");
			return;
		}

		String action = request.getParameter("action");

		switch (action) {
			case "remove" -> {
				String courseCode = request.getParameter("course_code");
				app.getLoggedInStudent().removeCourse(courseCode);
			}
			case "add" -> {
				String courseCode = request.getParameter("course_code");
				String classCode = request.getParameter("class_code");
				Map<String, Course> courseGroup = app.getCourses().get(courseCode);

				if (courseGroup == null) {
					request.getRequestDispatcher("/404.jsp").forward(request, response);
					break;
				}

				Course course = courseGroup.get(classCode);
				Student student = app.getLoggedInStudent();

				if (course == null || student == null) {
					request.getRequestDispatcher("/404.jsp").forward(request, response);
					break;
				}

				boolean hasPreconditions = true;

				for (String code : course.getPrerequisites()) {
					GradedCourse gradedCourse = student.getGradedCourses().get(code);

					if (gradedCourse == null || gradedCourse.getGrade() < 10)
						hasPreconditions = false;
				}

				boolean hasConflict = false;

				for (SelectedCourse selectedCourse : new ArrayList<>(student.getSelectedCourses().values())) {
					if (selectedCourse.getCourse().getClassTime().overlaps(course.getClassTime())
							|| selectedCourse.getCourse().getExamTime().overlaps(course.getExamTime()))
						hasConflict = true;
				}

				if (hasPreconditions && !hasConflict) {
					student.addCourse(course);
				}
				else {
					if (!hasPreconditions)
						message = "You have not passed preconditions.";
					else
						message = "Your selected courses has conflict.";

					request.getRequestDispatcher("/submit_failed.jsp").forward(request, response);
				}
			}
			case "submit" -> {
				Student student = app.getLoggedInStudent();
				int selectedUnits = student.getSelectedUnits();
				boolean hasCapacity = true;

				for (Map.Entry<String, SelectedCourse> entry : student.getSelectedCourses().entrySet()) {
					if (entry.getValue().getCourse().getCapacity() <= entry.getValue().getCourse().getNumberOfStudents())
						hasCapacity = false;
				}

				if (selectedUnits < 12 || selectedUnits > 20) {
					message = "Invalid sum of units.";
					request.getRequestDispatcher("/submit_failed.jsp").forward(request, response);
				}
				else if (!hasCapacity){
					message = "Class is full.";
					request.getRequestDispatcher("/submit_failed.jsp").forward(request, response);
				}
				else {
					student.finalizeCourses();
				}
			}
			case "reset" -> {
				Student student = app.getLoggedInStudent();
				for (Map.Entry<String, SelectedCourse> entry : student.getSelectedCourses().entrySet()) {
					if (entry.getValue().getState() != CourseState.FINALIZED)
						student.removeCourse(entry.getKey());
				}
			}
			case "search" -> {
				app.setSearchFilter(request.getParameter("search"));
				response.sendRedirect("/courses");
			}
			case "clear" -> {
				app.setSearchFilter(request.getParameter(""));
				response.sendRedirect("/courses");
			}
		}

		response.sendRedirect("/courses");
	}
}