package nl.tudelft.oopp.demo.controllers.startscreen;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javafx.fxml.FXML;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import nl.tudelft.oopp.demo.communication.startscreen.StartCommunication;
import nl.tudelft.oopp.demo.data.Room;

public class CreateController {

    @FXML
    private TextField inputRoomName;
    @FXML
    private DatePicker inputDate;
    @FXML
    private TextField inputTime;

    /**
     * Checks what type of room is being created and calls correct method.
     */
    @FXML
    public void createButton() {
        String name = inputRoomName.getText();
        Room room = null;
        if (name.equals("")) {
            name = "Room";
        }
        if (inputDate.getValue() == null) {
            room = StartCommunication.createRoom(name);
        } else {
            String date = inputDate.toString();
            String time = inputTime.getText();
            //Since Date is a dick, this is how you make it :)
            Calendar fullDate = new GregorianCalendar(Integer.parseInt(
                    (date.substring(0, 3))) + 1900,
                    Integer.parseInt(date.substring(5, 6)),
                    Integer.parseInt(date.substring(7, 8)),
                    Integer.parseInt(time.substring(0, 1)),
                    Integer.parseInt(time.substring(2, 3)));
            Date actualDate = fullDate.getTime();
            room = StartCommunication.createScheduledRoom(name, actualDate);
        }
    }

}
