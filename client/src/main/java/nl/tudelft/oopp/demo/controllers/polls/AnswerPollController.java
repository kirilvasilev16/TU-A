package nl.tudelft.oopp.demo.controllers.polls;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;

import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.polls.AnswerPollCommunication;
import nl.tudelft.oopp.demo.data.Poll;
import nl.tudelft.oopp.demo.data.Room;
import nl.tudelft.oopp.demo.data.User;
import nl.tudelft.oopp.demo.data.helper.AnswerHelper;


public class AnswerPollController {

    @FXML
    private ListView<Button> listLeft;
    @FXML
    private ListView<Button> listRight;
    @FXML
    private Text textQuestion;

    private Poll poll;
    private User user;
    private Room room;

    private String background = "-fx-background-color: LightBlue";
    private String highlightedBackground = "-fx-background-color: Green";
    private List<Button> selected = new ArrayList<>();

    /**
     * Loader for poll view for students to answer.
     * @param poll the poll
     * @param user the user
     * @param room the room
     */
    public void loadData(Poll poll, User user, Room room) {
        this.poll = poll;
        this.user = user;
        this.room = room;

        //Making sure the lists cant be selected.
        listLeft.setSelectionModel(new NoSelectionModel<>());
        listRight.setSelectionModel(new NoSelectionModel<>());

        textQuestion.setText(poll.getText());
        if (poll.getStatus() == Poll.PollStatus.OPEN) {
            loadOpenView();
        } else if (poll.getStatus() == Poll.PollStatus.STATISTICS) {
            loadStatView();
        } else {
            loadClosedView();
        }

    }

    /**
     * Load data into the poll.
     */
    public void loadOpenView() {
        List<String> questions = poll.getOptions();
        if (questions.size() <= 2) {
            fillButtonLists(questions, 342);
        } else if (questions.size() <= 4) {
            fillButtonLists(questions, 168);
        } else if (questions.size() <= 6) {
            fillButtonLists(questions, 110);
        } else if (questions.size() <= 8) {
            fillButtonLists(questions, 81);
        } else if (questions.size() <= 10) {
            fillButtonLists(questions, 63);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Error inserting questions!");
            alert.showAndWait();
        }
    }

    /**
     * Load stat data into the poll.
     */
    public void loadStatView() {
        List<String> questions = poll.getOptions();
        if (questions.size() <= 2) {
            fillStatList(questions, 342);
        } else if (questions.size() <= 4) {
            fillStatList(questions, 168);
        } else if (questions.size() <= 6) {
            fillStatList(questions, 110);
        } else if (questions.size() <= 8) {
            fillStatList(questions, 81);
        } else if (questions.size() <= 10) {
            fillStatList(questions, 63);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Error loading statistics!");
            alert.showAndWait();
        }
    }

    /**
     * Load custom screen for when poll is closed.
     */
    public void loadClosedView() {
        Button button = new Button("The poll is currently closed.");
        button.setPrefHeight(342);
        button.setPrefWidth(230);
        button.setStyle(background);
        button.setDisable(true);
        listLeft.getItems().add(button);
        listRight.getItems().add(button);
    }

    /**
     * Fills the listviews with buttons.
     * @param questions List containing the questions
     * @param height The height that a button can be
     */
    public void fillButtonLists(List<String> questions, int height) {
        for (int i = 0; i < questions.size(); i++) {
            if (i % 2 == 0) {
                listLeft.getItems().add(buttonCreator(questions.get(i), height, false));
            } else {
                listRight.getItems().add(buttonCreator(questions.get(i), height, false));
            }
        }
    }

    /**
     * Filling of the stats list.
     * @param questions The questions that need to be loaded in
     * @param height The height of the questions
     */
    public void fillStatList(List<String> questions, int height) {
        int totalAnswers = AnswerPollCommunication.getTotalAnswerAmount(poll.getId());
        for (int i = 0; i < questions.size(); i++) {
            String answer = questions.get(i) + "\n Chosen by: "
                    + (int) Math.round(AnswerPollCommunication
                    .getAnswerAmount(poll.getId(), questions.get(i))
                    * 100.0 / totalAnswers) + "%";
            if (i % 2 == 0) {
                listLeft.getItems().add(buttonCreator(answer, height, true));
            } else {
                listRight.getItems().add(buttonCreator(answer, height, true));
            }
        }
    }

    /**
     * Creates a button based on height.
     * @param value the questions that the button represents
     * @param height the height that the button can be
     * @return
     */
    public Button buttonCreator(String value, int height, Boolean disable) {
        Button button = new Button(value);
        button.setPrefHeight(height);
        button.setPrefWidth(230);
        button.setStyle(background);
        button.setCursor(Cursor.HAND);
        button.setDisable(disable);
        button.setOnAction(e -> {
            Button button1 = (Button) e.getSource();
            if (selected.contains(button1)) {
                button1.setStyle(background);
                selected.remove(button1);
            } else {
                selected.add((button1));
                button1.setStyle(highlightedBackground);
            }
        });
        return button;
    }

    /**
     * Submit button.
     */
    public void submitButton() {
        if (poll.getStatus() == Poll.PollStatus.OPEN) {
            List<String> answers = formatButtons();
            AnswerHelper answerHelper = new AnswerHelper(user.getId(), poll.getId(), answers);
            AnswerPollCommunication.createAnswer(answerHelper);
            Stage oldStage = (Stage) listLeft.getScene().getWindow();
            oldStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Error, poll is not open!");
            alert.showAndWait();
        }
    }

    /**
     * Format buttons.
     *
     * @return the string
     */
    public List<String> formatButtons() {
        List<String> list = new ArrayList<>();
        for (Button button : selected) {
            list.add(button.getText());
        }
        return list;
    }

}
