package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class AddCourseService {

    @RequestMapping(value = "/add_course", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean addCourse(@RequestParam String courseCode, @RequestParam String classCode) {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        Map<String, Course> courseGroup = app.getCourses().get(courseCode);
        if (courseGroup == null)
            return false;

        Course course = courseGroup.get(classCode);
        Student student = null;
        student = app.getLoggedInStudent();
        if (course == null || student == null)
            return false;

        boolean hasConflict = false;

        for (SelectedCourse selectedCourse : new ArrayList<>(student.getSelectedCourses().values())) {
            if (selectedCourse.getCourse().getClassTime().overlaps(course.getClassTime())
                    || selectedCourse.getCourse().getExamTime().overlaps(course.getExamTime()))
                hasConflict = true;
        }

        if (student.getSelectedCourses().containsKey(courseCode) || hasConflict)
            return false;

        if (course.getNumberOfStudents() >= course.getCapacity())
            student.addCourse(course, CourseState.NON_FINALIZED, CourseSelectionType.WAITING_LIST);
        else
            student.addCourse(course, CourseState.NON_FINALIZED, CourseSelectionType.REGISTERED);
        return true;
    }
}
