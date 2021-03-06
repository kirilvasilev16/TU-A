package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.helpers.QuestionExportHelper;
import nl.tudelft.oopp.demo.entities.helpers.QuestionHelper;
import nl.tudelft.oopp.demo.entities.helpers.RoomHelper;
import nl.tudelft.oopp.demo.entities.helpers.StudentHelper;

import nl.tudelft.oopp.demo.entities.users.ElevatedUser;
import nl.tudelft.oopp.demo.entities.users.Student;
import nl.tudelft.oopp.demo.entities.users.User;

import nl.tudelft.oopp.demo.exceptions.InvalidIdException;

import nl.tudelft.oopp.demo.repositories.LogEntryRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomConfigRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionsServiceTest {


    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LogEntryRepository logEntryRepository;

    @Autowired
    private RoomConfigRepository roomConfigRepository;


    private UserService userService;

    private QuestionService questionService;

    private RoomService roomService;



    private long id1;
    private long id2;
    private long id3;

    private User user1;
    private User user2;
    private User user3;
    private User bannedUser;
    private ElevatedUser admin;


    private Question question1;
    private Question question2;
    private Question question3;
    private Question question4;
    private Question question5;

    private Room room;



    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        questionService = new QuestionService(questionRepository, userRepository,
                roomRepository, logEntryRepository, userService);
        roomService = new RoomService(roomRepository, userRepository,
                logEntryRepository, roomConfigRepository);

        user1 = new Student("UserName 1", "IP 1");
        user2 = new Student("UserName 2", "IP 2");
        user3 = new Student("UserName 3", "ajnkdsjnasdjnascjnk");




        question1 = new Question("This is the text 1", user1);
        question2 = new Question("This is the text 2", user1);
        question3 = new Question("This is the text 3", user2);
        question4 = new Question("This is the text 4", user3);

        // Make ending date currentDate + a few minutes so no exceptions are thrown.
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 5);
        Date endingDate = calendar.getTime();
        calendar.add(Calendar.MINUTE, -7);
        Date startingDate = calendar.getTime();

        RoomHelper rh = new RoomHelper("title", "admin", false,
            startingDate, endingDate);
        room = roomService.createRoom(rh,"IP 4");
        room.refreshOngoing(); // update this so tests dont fail.

        userRepository.save(user1);
        userRepository.save(user2);

        admin = (ElevatedUser) roomService.join(room.getElevatedPassword(), "admin", "IP 4");

        bannedUser =  roomService.join(room.getNormalPassword(), "banned", "IP 5");
        roomService.banUser(room.getId(), admin.getId(), bannedUser.getId(),
                room.getElevatedPassword());

        question5 = new Question("This is the question of banned user", bannedUser);

        questionService.addQuestion(question1, room.getId());
        questionService.addQuestion(question2, room.getId());
        questionService.addQuestion(question3, room.getId());

        id1 = question1.getId();
        id2 = question2.getId();
        id3 = question3.getId();

    }

    @Test
    void addQuestionInvalidUser() {
        Assertions.assertThrows(InvalidIdException.class, () -> {
            questionService.addQuestion(question4, room.getId());
        });
    }

    @Test
    void addQuestion() {
        userRepository.save(user3);
        Assertions.assertDoesNotThrow(() -> {
            questionService.addQuestion(question4, room.getId());
        });
    }

    @Test
    void getQuestion() {
        assertEquals(questionService.getQuestion(question1.getId()), question1);
    }

    @Test
    void getText() {
        assertEquals("This is the text 1", questionService.getText(id1));
    }

    @Test
    void setTextV1() {
        try {
            questionService.setText(id1, "this question has changed");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals("this question has changed", questionService.getText(id1));
    }

    @Test
    void setTextV2() {
        StudentHelper studentHelper = new StudentHelper("UserName 1", "IP 1");
        QuestionHelper questionHelper = new QuestionHelper("this question has changed",
                studentHelper);

        questionService.setText(question1.getId(), questionHelper);

        assertEquals("this question has changed", questionService.getText(id1));
    }

    @Test
    void getAuthor() {
        assertEquals(user1, questionService.getAuthor(id1));
    }

    @Test
    void upvote() {
        assertEquals(0, questionService.getUpvotes(id1));
        questionService.upvote(id1);
        assertEquals(1, questionService.getUpvotes(id1));
    }

    @Test
    void downvoteStaysZero() {
        assertEquals(0, questionService.getUpvotes(id1));
        questionService.downvote(id1);
        assertEquals(0, questionService.getUpvotes(id1));
    }

    @Test
    void downvoteDecreases() {
        assertEquals(0, questionService.getUpvotes(id1));
        questionService.upvote(id1);
        assertEquals(1, questionService.getUpvotes(id1));
        questionService.downvote(id1);
        assertEquals(0, questionService.getUpvotes(id1));
    }


    @Test
    void getUpvotes() {
        assertEquals(0, questionService.getUpvotes(id1));
    }

    @Test
    void getScore() {
        assertEquals(0, questionService.getScore(id1));
    }

    @Test
    void setScore() {
        questionService.setScore(id1, 10);
        assertEquals(10, questionService.getScore(id1));
    }

    @Test
    void setScoreNegative() {
        questionService.setScore(id1, -1);
        assertEquals(0, questionService.getScore(id1));
    }

    @Test
    void getHighest() {
        questionService.setScore(id3, 2);
        assertEquals(id3, questionService.getHighest(0));
    }

    @Test
    void getDate() {
        assertEquals(question1.getTimeCreated(),
                questionService.getDate(id1));
    }

    @Test
    void getStatus() {
        assertEquals(question1.getStatus(), questionService.getStatus(id1));
    }

    @Test
    void setAnswered() {
        questionService.setAnswered(id1);
        assertEquals(Question.QuestionStatus.ANSWERED, questionService.getStatus(id1));
    }

    @Test
    void userSetAnswered() {
        questionService.userSetAnswered(id1);
        assertEquals(Question.QuestionStatus.ANSWERED, questionService.getStatus(id1));
    }

    @Test
    void userSetAnsweredGreaterThan5() {
        questionService.setScore(id1, 6);
        questionService.userSetAnswered(id1);
        assertEquals(Question.QuestionStatus.OPEN, questionService.getStatus(id1));
    }

    @Test
    void userSetAnsweredGreaterThanX() {
        questionService.setScore(id1, 11);
        questionService.userSetAnswered(id1, 10);
        assertEquals(Question.QuestionStatus.OPEN, questionService.getStatus(id1));

    }

    @Test
    void userSetAnsweredLessThanX() {
        questionService.setScore(id1, 9);
        questionService.userSetAnswered(id1, 10);
        assertEquals(Question.QuestionStatus.ANSWERED, questionService.getStatus(id1));
    }

    @Test
    void setSpam() {
        questionService.setSpam(id1);
        assertEquals(Question.QuestionStatus.SPAM, questionService.getStatus(id1));
    }

    @Test
    void setOpen() {
        questionService.setSpam(id1);
        questionService.setOpen(id1);
        assertEquals(Question.QuestionStatus.OPEN, questionService.getStatus(id1));
    }

    @Test
    void getAnswer() {
        assertEquals(question1.getAnswer(), questionService.getAnswer(id1));
    }

    @Test
    void setAnswerV1() {
        questionService.setAnswer(id1, "Changed the answer");
        assertEquals("Changed the answer", questionService.getAnswer(id1));
    }

    @Test
    void setAnswerV2() {
        StudentHelper studentHelper = new StudentHelper("UserName 1", "IP 1");
        QuestionHelper questionHelper = new QuestionHelper("Changed the answer", studentHelper);
        questionService.setAnswer(question1.getId(), questionHelper);
        assertEquals("Changed the answer", questionService.getAnswer(id1));
    }


    @Test
    void deleteOneQuestion() throws JsonProcessingException {
        boolean contains = roomService.findAllQuestions(room.getId()).contains(question3);
        assertTrue(contains);
        questionService.deleteOneQuestion(room.getId(), id3);
        contains = roomService.findAllQuestions(room.getId()).contains(question3);
        assertFalse(contains);
    }

    @Test
    void deleteAllQuestions() throws JsonProcessingException {
        questionService.deleteAllQuestions(room.getId());
        assertEquals(new HashSet<>(), roomService.findAllQuestions(room.getId()));
    }

    @Test
    void exportError() throws JsonProcessingException {
        Assertions.assertThrows(Exception.class, () -> {
            List<QuestionExportHelper> error =  questionService.export(382911230);

        });
    }

    @Test
    void export() throws JsonProcessingException {
        String expected = "[" + question1.exportToJson() + "]";
        List<QuestionExportHelper> actual = questionService.export(id1);
        assertEquals(expected, actual.toString());
    }

    @Test
    void exportAll() throws JsonProcessingException {
        boolean actual = questionService.exportAll(room.getId()).toString()
                .contains(question1.exportToJson());
        actual = actual && questionService.exportAll(room.getId()).toString()
                .contains(question2.exportToJson());
        actual = actual && questionService.exportAll(room.getId()).toString()
                .contains(question3.exportToJson());
        assertTrue(actual);
    }

    @Test
    void exportTopError() throws JsonProcessingException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            List<QuestionExportHelper> error =  questionService.exportTop(room.getId(), 0);
        });
    }

    @Test
    void exportTop() throws JsonProcessingException {
        questionService.upvote(id1);
        question1.setUpvotes(1);
        List<QuestionExportHelper> actual =  questionService.exportTop(room.getId(), 1);
        String expected = "[" + question1.exportToJson() + "]";
        assertEquals(expected, actual.toString());
    }

    @Test
    void exportAnswered() throws JsonProcessingException {
        assertEquals("[]", questionService.exportAnswered(room.getId()).toString());
        questionService.setAnswer(id1, "This question is answered");
        question1.setAnswer("This question is answered");
        String expected = "[" + question1.exportToJson() + "]";
        List<QuestionExportHelper> actual = questionService.exportAnswered(room.getId());
        assertEquals(expected, actual.toString());
    }

    @Test
    void mapQuestionExport() throws JsonProcessingException {
        String expected = "[" + question1.exportToJson() + "]";
        Set<Question> questions = new HashSet<Question>();
        questions.add(question1);
        List<QuestionExportHelper> actual = questionService.mapQuestionExport(questions);
        assertEquals(expected, actual.toString());
    }

    @Test
    void getBeingAnswered() {
        assertDoesNotThrow(() -> questionService.getBeingAnswered(id1));
    }

    @Test
    void setBeingAnswered() {
        assertFalse(question1.isBeingAnswered());
        assertDoesNotThrow(() -> questionService.setBeingAnswered(id1, true));
        assertTrue(questionService.getBeingAnswered(id1));
    }
}