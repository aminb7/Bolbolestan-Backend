package com.ie.bolbolestan.services;

import com.ie.bolbolestan.model.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FinalizeService {

    @RequestMapping(value = "/finalize_courses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean finalizeCourses(@RequestAttribute("id") String email) {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        Student student = app.getStudent(email);
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
