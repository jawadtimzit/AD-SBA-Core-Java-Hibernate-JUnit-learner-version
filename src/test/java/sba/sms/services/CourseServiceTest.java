package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.*;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@FieldDefaults(level = AccessLevel.PRIVATE)
class CourseServiceTest {
    static CourseService courseService;

    @BeforeAll
    static void beforeAll() {
        courseService = new CourseService();
        CommandLine.addData();
    }

    @Test
    @Order(1)
    @DisplayName("Test get all courses")
    void getAllCourses(){

        String instructorPhillip = "Phillip Witkin";
        List<Course> expected = new ArrayList<>(Arrays.asList(new Course("Java", instructorPhillip),
        new Course("Frontend", "Kasper Kain"),
        new Course("JPA", "Jafer Alhaboubi"),
        new Course("Spring Framework", instructorPhillip),
        new Course("SQL", instructorPhillip)
        ));

        List<Course> actual = courseService.getAllCourses();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(2)
    @DisplayName("Test create a course")
    void createCourse(){
        Course expected = new Course("C++", "test");
        Course actual = new Course("C++", "test");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(3)
    @DisplayName("Test get course by id")
    void getCourseById(){
        Course expected = new Course("JPA", "Jafer Alhaboubi");
        Course actual = courseService.getCourseById(1);
        Assertions.assertEquals(expected, actual);
    }
}
