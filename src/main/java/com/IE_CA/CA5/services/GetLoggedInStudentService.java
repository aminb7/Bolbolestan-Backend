package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.BolbolestanApplication;
import com.IE_CA.CA5.model.Student;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetLoggedInStudentService {

    @RequestMapping(value = "loggedin_student", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Student getLoggedInStudent() {
        return BolbolestanApplication.getInstance().getLoggedInStudent();
    }
}
