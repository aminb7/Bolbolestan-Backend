package com.IE_CA.CA5.model;

import com.IE_CA.CA5.repository.ConnectionPool;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class Student {
	private final String id;
	private final String name;
	private final String secondName;
	private final String email;
	private final String password;
	private final String birthDate;
	private final String field;
	private final String faculty;
	private final String level;
	private final String status;
	private final String img;

	private Map<String, SelectedCourse> selectedCourses;
	private Map<String, GradedCourse> gradedCourses;

	@JsonCreator
	public Student(@JsonProperty("id") String id, @JsonProperty("name") String name,
	               @JsonProperty("secondName") String secondName, @JsonProperty("email") String email,
				   @JsonProperty("password") String password, @JsonProperty("birthDate") String birthDate,
	               @JsonProperty("field") String field, @JsonProperty("faculty") String faculty,
	               @JsonProperty("level") String level, @JsonProperty("status") String status,
	               @JsonProperty("img") String img) {
		this.id = id;
		this.name = name;
		this.secondName = secondName;
		this.email = email;
		this.password = password;
		this.birthDate = birthDate;
		this.field = field;
		this.faculty = faculty;
		this.level = level;
		this.status = status;
		this.img = img;
		selectedCourses = new HashMap<>();
		gradedCourses = new HashMap<>();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSecondName() {
		return secondName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public String getField() {
		return field;
	}

	public String getFaculty() {
		return faculty;
	}

	public String getLevel() {
		return level;
	}

	public String getStatus() {
		return status;
	}

	public String getImg() {
		return img;
	}

	public void addCourse(Course course, CourseState state, CourseSelectionType courseSelectionType) {
		try {
			Connection con = ConnectionPool.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO SelectedCourses VALUES (?, ?, ?, ?, ?)");
			stmt.setString(1, id);
			stmt.setString(2, course.getCode());
			stmt.setString(3, course.getClassCode());
			stmt.setString(4, state.toString());
			stmt.setString(5, courseSelectionType.toString());
			stmt.addBatch();
			stmt.executeBatch();
			stmt.close();
			con.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void changeCourseSelectionType(String code, CourseSelectionType type) {
		SelectedCourse selectedCourse = selectedCourses.get(code);
		if (selectedCourse != null)
			selectedCourse.setCourseSelectionType(type);
	}

	public void removeCourse(String code) {
		try {
			Connection con = ConnectionPool.getConnection();
			Statement stmt = con.createStatement();
			stmt.executeUpdate("delete from selectedcourses where id = \"" + id + "\" and code = \"" + code + "\"");
			stmt.close();
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Map<String, SelectedCourse> getSelectedCourses() {
		return selectedCourses;
	}

	public int getSelectedUnits() {
		SelectedCourse[] coursesList = selectedCourses.values().toArray(new SelectedCourse[0]);

		int selectedUnits = 0;
		for (SelectedCourse course : coursesList) {
			selectedUnits = selectedUnits + course.getCourse().getUnits();
		}

		return selectedUnits;
	}

	public Map<String, GradedCourse> getGradedCourses() {
		return gradedCourses;
	}

	public void setGradedCourses() {
		try {
			Connection con = ConnectionPool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("select * from gradedcourses where id = \"" + id + "\"");

			while (result.next()) {
				GradedCourse gradedCourse = new GradedCourse(result.getString("code"),
						result.getInt("grade"), result.getInt("term"));

				gradedCourse.setCourse(BolbolestanApplication.getInstance().getCourse(result.getString("code"), result.getString("classCode")));
				this.gradedCourses.put(gradedCourse.getCode(), gradedCourse);
			}
			result.close();
			stmt.close();
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void finalizeCourses() {
		try {
			Connection con = ConnectionPool.getConnection();
			Statement stmt = con.createStatement();
			stmt.executeUpdate("update selectedcourses set courseState = \"FINALIZED\" where id = \""
					+ id + "\"  and courseState = \"NON_FINALIZED\"");
			stmt.close();
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getTotalPassedUnits() {
		int result = 0;

		for (GradedCourse course : new ArrayList<>(gradedCourses.values())) {
			if (course.getGrade() >= 10)
				result += course.getCourse().getUnits();
		}

		return result;
	}

	public float getGPA() {
		float result = 0;
		int unitsSum = 0;

		for (GradedCourse course : new ArrayList<>(gradedCourses.values())) {
			int units = course.getCourse().getUnits();
			result += course.getGrade() * units;
			unitsSum += units;
		}

		return result / unitsSum;
	}

	public void setSelectedCourses() {
		try {
			Connection con = ConnectionPool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("select * from selectedcourses where id = \"" + id + "\"");

			while (result.next()) {
				Statement stmt2 = con.createStatement();
				ResultSet courseResult = stmt2.executeQuery("select * from courses where code = \"" + result.getString("code") + "\"");

				if (courseResult.next()) {
					Statement stmt3 = con.createStatement();
					ResultSet prerequisitesResult = stmt3.executeQuery("select * from prerequisites where code = \"" + result.getString("code") + "\"");

					List<String> prerequisites = new ArrayList<String>();
					while (prerequisitesResult.next()) {
						prerequisites.add(prerequisitesResult.getString("pcode"));
					}
					prerequisitesResult.close();
					stmt3.close();

					Statement stmt4 = con.createStatement();
					ResultSet classDaysResult = stmt4.executeQuery("select * from coursedays where code = \"" + result.getString("code") + "\"");
					List<String> days = new ArrayList<String>();
					while (classDaysResult.next()) {
						days.add(classDaysResult.getString("day"));
					}
					classDaysResult.close();
					stmt4.close();

					ClassTime classTime = new ClassTime(days.toArray(new String[days.size()]), courseResult.getString("classStart") + "-" + courseResult.getString("classEnd"));
					ExamTime examTime = new ExamTime(LocalDateTime.parse(courseResult.getString("examStart")), LocalDateTime.parse(courseResult.getString("examEnd")));
					Course course = new Course(courseResult.getString("code"),
							courseResult.getString("classCode"), courseResult.getString("name"),
							courseResult.getInt("units"), courseResult.getString("type"),
							courseResult.getString("instructor"), courseResult.getInt("capacity"),
							prerequisites.toArray(new String[prerequisites.size()]), classTime, examTime);
					SelectedCourse selectedCourse = new SelectedCourse(course, CourseState.valueOf(result.getString("courseState")),
							CourseSelectionType.valueOf(result.getString("courseSelectionType")));
					courseResult.close();
					stmt2.close();
					selectedCourses.put(course.getCode(), new SelectedCourse(course, selectedCourse.getState(), selectedCourse.getCourseSelectionType()));
				}
			}
			result.close();
			stmt.close();
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		try {
			Connection con = ConnectionPool.getConnection();
			Statement stmt = con.createStatement();
			stmt.executeUpdate("delete from selectedcourses where coursestate = \"NON_FINALIZED\"");
			stmt.close();
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean hasConflicts(Course course) {
		try {
			Connection con = ConnectionPool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("select * from selectedcourses where id = \"" + id + "\"");

			while (result.next()) {
				Course this_course = BolbolestanApplication.getInstance().getCourse(result.getString("code"),
						result.getString("classCode"));

				if (this_course.getClassTime().overlaps(course.getClassTime())
						|| this_course.getExamTime().overlaps(course.getExamTime()))
					return true;
			}
			result.close();
			stmt.close();
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean hasCourse(String courseCode) {
		boolean has = false;
		try {
			Connection con = ConnectionPool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("select * from selectedcourses where id = \"" + id + "\" and code = \"" + courseCode + "\"");
			if (result.next()) has = true;
			result.close();
			stmt.close();
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return has;
	}
}