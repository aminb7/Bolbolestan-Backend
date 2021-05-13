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
        Course course = app.getCourse(courseCode, classCode);
        Student student = app.getLoggedInStudent();
        if (course == null || student == null)
            return false;

        if (student.hasCourse(courseCode) || student.hasConflicts(course))
            return false;

        if (course.getNumberOfStudents() >= course.getCapacity())
            student.addCourse(course, CourseState.NON_FINALIZED, CourseSelectionType.WAITING_LIST);
        else
            student.addCourse(course, CourseState.NON_FINALIZED, CourseSelectionType.REGISTERED);
        return true;
    }
}
