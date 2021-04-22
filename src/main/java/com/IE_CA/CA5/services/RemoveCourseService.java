package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.BolbolestanApplication;
import com.IE_CA.CA5.model.CourseState;
import com.IE_CA.CA5.model.Student;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RemoveCourseService {

    @RequestMapping(value = "/remove_course", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeCourse(@RequestParam String courseCode) {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        app.getLoggedInStudent().removeCourse(courseCode);
    }
}
