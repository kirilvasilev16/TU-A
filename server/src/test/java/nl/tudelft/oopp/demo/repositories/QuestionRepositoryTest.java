package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.users.Student;
import nl.tudelft.oopp.demo.entities.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private UserRepository repositoryU;

    private long id1;
    private long id2;
    private long id3;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;
    private Question question3;


    @BeforeEach
    void setUp() {

        user1 = new Student("UserName 1", "IP 1");
        user2 = new Student("UserName 2", "IP 2");

        question1 = new Question("This is the text 1", user1);
        question2 = new Question("This is the text 2", user1);
        question3 = new Question("This is the text 3", user2);


        repositoryU.save(user1);
        repositoryU.save(user2);

        repository.save(question1);
        repository.save(question2);
        repository.save(question3);

        id1 = question1.getId();
        id2 = question2.getId();
        id3 = question3.getId();
    }



    @Test
    void getText() {
        assertEquals("This is the text 1", repository.getText(id1));
    }

    @Test
    void setText() {
        repository.setText(id1, "this question has changed");
        assertEquals("this question has changed", repository.getText(id1));
    }

    @Test
    void getAuthor() {
        assertEquals(user1, repository.getAuthor(id1));

    }

    @Test
    void upvote() {
        assertEquals(repository.getUpvotes(id1), question1.getUpvotes());
        repository.upvote(id1);
        assertEquals(repository.getUpvotes(id1), question1.getUpvotes() + 1);

    }

    @Test
    void downvote() {
        assertEquals(repository.getUpvotes(id1), question1.getUpvotes());
        repository.downvote(id1);
        assertEquals(repository.getUpvotes(id1), question1.getUpvotes() - 1);
    }

    @Test
    void getUpvotes() {
        assertEquals(repository.getUpvotes(id1), question1.getUpvotes());
    }

    @Test
    void getScore() {
        assertEquals(repository.getScore(id1), question1.getScore());

    }

    @Test
    void setScore() {
        assertEquals(repository.getScore(id1), question1.getScore());
        repository.setScore(id1, 100);
        assertEquals(repository.getScore(id1), 100);
    }

    @Test
    void getHighestScore() {

        repository.setScore(id1, 100);
        repository.setScore(id2, 500);
        repository.setScore(id3, 300);

        List highestScore = new LinkedList();
        highestScore.add(id2);
        highestScore.add(id3);
        highestScore.add(id1);

        assertEquals(highestScore, repository.getHighestScore());

    }

    @Test
    void getTime() {
        assertEquals(question1.getTimeCreated(), repository.getDate(id1));
    }

    @Test
    void getStatus() {
        assertEquals(question1.getStatus(), repository.getStatus(id1));
    }

    @Test
    void isAnswered() {
        repository.setAnswered(id1);
        question1.setStatus(Question.QuestionStatus.ANSWERED);
        assertEquals(repository.getStatus(id1), question1.getStatus());
    }

    @Test
    void isSpam() {
        repository.setSpam(id1);
        question1.setStatus(Question.QuestionStatus.SPAM);
        assertEquals(repository.getStatus(id1), question1.getStatus());
    }

    @Test
    void isOpen() {
        repository.setOpen(id1);
        question1.setStatus(Question.QuestionStatus.OPEN);
        assertEquals(repository.getStatus(id1), question1.getStatus());
    }

    @Test
    void getAnswer() {
        assertEquals(question1.getAnswer(), repository.getAnswer(id1));
    }

    @Test
    void setAnswer() {
        assertEquals(question1.getAnswer(), repository.getAnswer(id1));
        repository.setAnswer(id1, "Some New Answer");
        assertEquals(repository.getAnswer(id1), "Some New Answer");
    }

    @Test
    void setBeingAnswered() {
        assertEquals(question1.isBeingAnswered(), repository.getBeingAnswered(id1));
        repository.setBeingAnswered(id1, true);
        assertEquals(true, repository.getBeingAnswered(id1));
    }

    @Test
    void getBeingAnswered() {
        assertEquals(question1.isBeingAnswered(), repository.getBeingAnswered(id1));
    }
}