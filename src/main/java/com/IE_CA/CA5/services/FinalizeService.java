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
public class FinalizeService {

    @RequestMapping(value = "/finalize_courses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean finalizeCourses() {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        Student student = null;
        student = app.getLoggedInStudent();
        if (student == null)
            return false;

        boolean passPreconditions = true;
        boolean hasPassed = false;

        for (Map.Entry<String, SelectedCourse> entry : student.getSelectedCourses().entrySet()) {
            if (entry.getValue().getState() == CourseState.FINALIZED)
                continue;

            for (String code : entry.getValue().getCourse().getPrerequisites()) {
                GradedCourse gradedCourse = student.getGradedCourses().get(code);

                if (gradedCourse == null || gradedCourse.getGrade() < 10)
                    passPreconditions = false;
            }

            GradedCourse gradedCourse = student.getGradedCourses().get(entry.getKey());
            if (gradedCourse != null && gradedCourse.getGrade() >= 10)
                hasPassed = true;
        }

        if (!passPreconditions || hasPassed) return false;

        int selectedUnits = student.getSelectedUnits();
        if (selectedUnits < 12 || selectedUnits > 20) return false;

        student.finalizeCourses();
        return true;
    }
}
