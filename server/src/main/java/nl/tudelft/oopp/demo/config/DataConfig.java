package nl.tudelft.oopp.demo.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Quote;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.PollRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.QuoteRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    CommandLineRunner commandLineRunner(QuoteRepository quoteRepository,
                                        UserRepository userRepository,
                                        RoomRepository roomRepository,
                                        QuestionRepository questionRepository,
                                        PollRepository pollRepository) {
        return args -> {
            Quote quote1 = new Quote(
                    1,
                    "A clever person solves a problem. A wise person avoids it.",
                    "Albert Einstein"
            );
            Quote quote2 = new Quote(
                    2,
                    "The computer was born to solve problems that did not exist before.",
                    "Bill Gates"
            );
            Quote quote3 = new Quote(
                    3,
                    "Tell me and I forget.  Teach me and I remember.  Involve me and I learn.",
                    "Benjamin Franklin"
            );
            quoteRepository.saveAll(List.of(quote1, quote2, quote3));

            User user1 = new User("Admin", "ip", User.Type.ADMIN);
            User user2 = new User("Mod", "ip", User.Type.MODERATOR);
            User user3 = new User("Mod2", "ip", User.Type.MODERATOR);
            User user4 = new User("Student", "ip", User.Type.STUDENT);
            userRepository.saveAll(List.of(user1, user2, user3, user4));

            Poll p1 = new Poll("Poll title", "Poll text", new ArrayList<>(),
                    List.of("Correct answer"));
            pollRepository.saveAll(List.of(p1));

            Question q1 = new Question("Question title 1", "Question text 1", user1);
            Question q2 = new Question("Question title 2", "Question text 2", user2);
            Question q3 = new Question("Question title 3 ", "Question text 3", user2);
            Question q4 = new Question("Question title 4", "Question text 4", user4);
            questionRepository.saveAll(List.of(q1, q2, q3, q4));

            Room r1 = new Room("Room Title", user1);

            r1.setQuestions(Stream.of(q1, q2, q3, q4)
                    .collect(Collectors.toSet())
            );
            r1.setPolls(Stream.of(p1)
                    .collect(Collectors.toSet())
            );
            roomRepository.save(r1);
        };
    }

}
