package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.BolbolestanApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class RemoveCourseService {

    @RequestMapping(value = "/remove_course", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeCourse(@RequestParam String courseCode, @RequestAttribute("id") String email) throws SQLException {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        app.getStudent(email).removeCourse(courseCode);
    }
}
