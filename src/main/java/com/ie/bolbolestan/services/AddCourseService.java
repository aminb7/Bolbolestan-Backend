package com.ie.bolbolestan.services;

import com.ie.bolbolestan.model.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AddCourseService {

    @RequestMapping(value = "/add_course", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean addCourse(@RequestParam String courseCode, @RequestParam String classCode, @RequestAttribute("id") String email) {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        Course course = app.getCourse(courseCode, classCode);
        Student student = app.getStudent(email);
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
