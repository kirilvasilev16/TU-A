package nl.tudelft.oopp.demo.entities.helpers;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.oopp.demo.entities.users.Student;

/**
 * Student helper class.
 */
@Data
@NoArgsConstructor
public class StudentHelper {
    private String username;
    private String ip;

    /**
     * Create student.
     *
     * @return the student
     */
    public Student createStudent() {
        return new Student(username, ip);
    }
}