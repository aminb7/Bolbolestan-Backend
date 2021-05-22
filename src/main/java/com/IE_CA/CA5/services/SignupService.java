package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.BolbolestanApplication;
import com.IE_CA.CA5.model.Student;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class SignupService {

    @RequestMapping(value = "/signup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean signupStudent(@RequestParam String id, @RequestParam String name, @RequestParam String secondName,
                                 @RequestParam String email, @RequestParam String password, @RequestParam String birthDate,
                                 @RequestParam String field, @RequestParam String faculty, @RequestParam String level) throws SQLException {
        System.out.println("Signup Service");
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        if (app.isDuplicateStudent(id, email))
            return false;
        else {
            String defaultStudentStatus = "مشغول به تحصیل";
            String defaultStudentImg = "http://138.197.181.131:5200/img/brynn_larson.jpg";
            app.signupStudent(new Student(id, name, secondName, email, password, birthDate, field, faculty, level,
                    defaultStudentStatus, defaultStudentImg));
            return true;
        }
    }
}
