package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.BolbolestanApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginService {

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean loginStudent(@RequestParam String studentId) {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        if (app.studentExists(studentId)) {
            app.setLoggedInStudentId(studentId);
            return true;
        }
        else
            return false;
    }
}
