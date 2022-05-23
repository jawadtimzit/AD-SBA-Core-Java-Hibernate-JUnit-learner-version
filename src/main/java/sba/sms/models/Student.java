package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "student")
@Entity

// create the Student Entity Model
public class Student {
    // data fields, attributes
    @NonNull
    @Id
    @Column(length = 50, name="email")
    String email;

    @NonNull
    @Column(length = 50, nullable = false, name = "name")
    String name;

    @NonNull
    @Column(length = 150, name ="password")
    String password;

    // override to string(exclude collections to avoid infinite loops
    @ToString.Exclude  // exclude Tostring to avoid printing in both sides
    // ManyToMany relationship - cascading will give permission to save in both tables - also delete related fields
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},fetch = FetchType.EAGER)
    // join table name
    @JoinTable(name = "student_courses",
            // join table columns - will contain 2 FK of course and student tables
            joinColumns = @JoinColumn(name = "student_email"),
            inverseJoinColumns = @JoinColumn(name = "courses_id"))
    Set<Course> courses = new LinkedHashSet<>();

    //override equals and hashcode methods (don't use lambok here)
    // override the equal method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return email.equals(student.email) && name.equals(student.name) && password.equals(student.password);
    }

    // override the Hashcode method
    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }

    // helper method to add course to student
    public void addCourse(Course course) {
        courses.add(course);  // took courses and add the course that is passed as parameter
        course.getStudents().add(this); // get student and add course
    }
}
