package com.ie.bolbolestan.model;

import com.ie.bolbolestan.repository.BolbolestanRepository;
import com.ie.bolbolestan.repository.ConnectionPool;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Key;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.apache.commons.lang3.time.DateUtils.addMinutes;

public class BolbolestanApplication {
    private static String SECRET_KEY = "bolbolestan";
    private static String ISSUER = "info@bolbolestan.com";
    private static BolbolestanApplication single_instance = null;
    private BolbolestanRepository repository = BolbolestanRepository.getInstance();

    private Map<String, Map<String, Course>> courses;
    private Map<String, Student> students;
    private String searchFilter;
    private String typeSearchFilter;

    private BolbolestanApplication()
    {
        this.courses = new HashMap<>();
        this.students = new HashMap<>();
        this.searchFilter = "";
        this.typeSearchFilter = "all";
    }

    public static BolbolestanApplication getInstance()
    {
        if (single_instance == null)
            single_instance = new BolbolestanApplication();

        return single_instance;
    }

    public boolean studentExists(String email, String password) {
        try {
            Connection con = ConnectionPool.getConnection();
            String passwordHash = BolbolestanRepository.hashPassword(password);
            PreparedStatement stmt = con.prepareStatement("select * from Students where email = ? and password = ?");
            stmt.setString(1, email);
            stmt.setString(2, passwordHash);
            ResultSet result = stmt.executeQuery();

            if (result.next())
                return true;
            result.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentExists(String email) {
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement stmt = con.prepareStatement("select * from Students where email = ?");
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                return true;
            result.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Student getStudent(String email) {

        Student student = null;
        try {
            Connection con1 = ConnectionPool.getConnection();
            Statement stmt1 = con1.createStatement();
            ResultSet result1 = stmt1.executeQuery("USE BolbolestanDatabase");
            con1.close();
            Connection con = ConnectionPool.getConnection();
            PreparedStatement stmt = con.prepareStatement("select * from BolbolestanDatabase.Students where email = ?");
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                student = new Student(result.getString("id"), result.getString("name"),
                        result.getString("secondName"), result.getString("email"), "",
                        result.getString("birthDate"), result.getString("field"), result.getString("faculty"),
                        result.getString("level"), result.getString("status"),
                        result.getString("img"));

            result.close();
            stmt.close();
            con.close();

            if (student != null) {
                student.setGradedCourses();
                student.setSelectedCourses();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public Course getCourse(String code, String classCode) {
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement stmt = con.prepareStatement("select * from Courses where code = ? and classCode = ?");
            stmt.setString(1, code);
            stmt.setString(2, classCode);
            ResultSet courseResult = stmt.executeQuery();

            if (courseResult.next()) {
                PreparedStatement stmt2 = con.prepareStatement("select * from Prerequisites where code = ?");
                stmt2.setString(1, code);
                ResultSet prerequisitesResult = stmt2.executeQuery();

                List<String> prerequisites = new ArrayList<String>();
                while (prerequisitesResult.next()) {
                    prerequisites.add(prerequisitesResult.getString("pcode"));
                }
                prerequisitesResult.close();
                stmt2.close();

                PreparedStatement stmt3 = con.prepareStatement("select * from CourseDays where code = ?");
                stmt3.setString(1, code);
                ResultSet classDaysResult = stmt3.executeQuery();
                List<String> days = new ArrayList<String>();
                while (classDaysResult.next()) {
                    days.add(classDaysResult.getString("day"));
                }
                classDaysResult.close();
                stmt3.close();

                ClassTime classTime = new ClassTime(days.toArray(new String[days.size()]), courseResult.getString("classStart") + "-" + courseResult.getString("classEnd"));
                ExamTime examTime = new ExamTime(LocalDateTime.parse(courseResult.getString("examStart")), LocalDateTime.parse(courseResult.getString("examEnd")));
                Course course = new Course(courseResult.getString("code"),
                        courseResult.getString("classCode"), courseResult.getString("name"),
                        courseResult.getInt("units"), courseResult.getString("type"),
                        courseResult.getString("instructor"), courseResult.getInt("capacity"),
                        prerequisites.toArray(new String[prerequisites.size()]), classTime, examTime);
                courseResult.close();
                stmt2.close();
                con.close();
                return course;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public void setTypeSearchFilter(String typeSearchFilter) {
        this.typeSearchFilter = typeSearchFilter;
    }

    public List<Course> getFilteredCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            Connection con = ConnectionPool.getConnection();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from Courses where name like \"%" + searchFilter + "%\""
                    + " and (\"" + typeSearchFilter + "\" = \"all\" or \"" + typeSearchFilter + "\" = type)");

            while (result.next()) {
                courses.add(this.getCourse(result.getString("code"), result.getString("classCode")));
            }

            result.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

    public void updateWaitingLists() {
        for (Map.Entry<String, Map<String, Course>> entry : this.courses.entrySet()) {
            for (Map.Entry<String, Course> course : entry.getValue().entrySet()) {
                course.getValue().updateWaitingList();
            }
        }
    }

    public String createJWT(String email) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setId(email)
                .setIssuedAt(now)
                .setIssuer(ISSUER)
                .signWith(signatureAlgorithm, signingKey)
                .setExpiration(addDays(now, 1));

        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public boolean isDuplicateStudent(String id, String email) {
        try {
            Connection con = ConnectionPool.getConnection();
            PreparedStatement stmt = con.prepareStatement("select * from Students where id = ? or email = ?");
            stmt.setString(1, id);
            stmt.setString(2, email);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                return true;
            result.close();
            stmt.close();
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void signupStudent(Student student) throws SQLException {
        repository.addStudent(student);
    }

    public boolean changePassword(String email, String password) {
        if (studentExists(email)) {
            try {
                Connection con = ConnectionPool.getConnection();
                PreparedStatement stmt = con.prepareStatement("update Students set password = ? where email = ?");
                stmt.setString(1, BolbolestanRepository.hashPassword(password));
                stmt.setString(2, email);
                stmt.executeUpdate();
                stmt.close();
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public void sendMail(String email, String data) {
        String uri = "http://138.197.181.131:5200/api/send_mail?" + "url=" + data + "&email=" + email;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create(uri))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createForgetURL(String email) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setId(email)
                .setSubject("forget")
                .setIssuedAt(now)
                .setIssuer(ISSUER)
                .signWith(signatureAlgorithm, signingKey)
                .setExpiration(addMinutes(now, 10));

        return builder.compact();
    }
}
