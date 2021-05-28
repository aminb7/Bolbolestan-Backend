package com.ie.bolbolestan.services;

import com.ie.bolbolestan.model.BolbolestanApplication;
import com.ie.bolbolestan.model.Course;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetFilteredCoursesService {

    @RequestMapping(value = "filtered_courses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Course> getFilteredCourses() {
        return BolbolestanApplication.getInstance().getFilteredCourses();
    }
}
