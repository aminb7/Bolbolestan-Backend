package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.BolbolestanApplication;
import com.IE_CA.CA5.model.CourseState;
import com.IE_CA.CA5.model.Student;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResetService {

    @RequestMapping(value = "/reset", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void reset() {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        Student student = app.getLoggedInStudent();
        student.getSelectedCourses().entrySet().removeIf(entries->entries.getValue().getState() != CourseState.FINALIZED);
    }
}
