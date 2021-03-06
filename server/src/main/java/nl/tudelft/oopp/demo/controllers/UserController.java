package nl.tudelft.oopp.demo.controllers;

import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.helpers.StudentHelper;
import nl.tudelft.oopp.demo.entities.users.User;
import nl.tudelft.oopp.demo.services.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type User controller.
 */
@RestController("User")
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Returns a list of all users.
     *
     * @return the list
     */
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    /**
     * Adds a user given a StudentHelper.
     *
     * @param student the student
     * @param request the request
     * @return the long
     */
    @PostMapping("add")
    public long add(@RequestBody StudentHelper student,
                    HttpServletRequest request) {
        StudentHelper user = new StudentHelper(student.getUsername(), request.getRemoteAddr());
        return userService.add(user.createStudent());
    }

    /**
     * Adds questionId to the ids of question user with userId upvoted,
     * if he did not upvote it before.
     *
     * @param userId     the user id
     * @param questionId the question id
     */
    @PostMapping(value = "addUpvotedQuestion")
    public void addUpvotedQuestion(
        @PathParam("userId") Long userId, @PathParam("questionId") Long questionId) {
        userService.addUpvotedQuestion(userId, questionId);
    }

    /**
     * Removes questionId from the ids of questions user with userId upvoted,
     * if user upvoted the question before.
     *
     * @param userId     the user id
     * @param questionId the question id
     */
    @DeleteMapping(value = "removeUpvotedQuestion")
    public void removeUpvotedQuestion(
        @PathParam("userId") Long userId, @PathParam("questionId") Long questionId) {
        userService.removeUpvotedQuestion(userId, questionId);
    }

    /**
     * Gets the set of questions user with userId upvoted.
     *
     * @param userId the user id
     * @return the upvoted question
     */
    @GetMapping(value = "getUpvotedQuestion")
    public HashSet<Question> getUpvotedQuestion(@PathParam("userId") Long userId) {
        return userService.getUpvotedQuestion(userId);
    }

    /**
     * Find all students string.
     *
     * @return the list
     */
    @GetMapping(value = "findAllStudents")
    public List<User> findAllStudents() {
        return userService.findAllStudents();
    }

    /**
     * Returns all elevated users.
     *
     * @return the list
     */
    @GetMapping(value = "findAllElevatedUsers")
    public List<User> findAllElevatedUsers() {
        return userService.findAllElevatedUsers();
    }
}
