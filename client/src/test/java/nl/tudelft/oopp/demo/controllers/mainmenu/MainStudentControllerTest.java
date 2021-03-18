package nl.tudelft.oopp.demo.controllers.mainmenu;

import org.junit.jupiter.api.Test;

class MainStudentControllerTest {

    @Test
    void loadData() {
        /* Manual test plan to be carried out when testing if everything works correctly.
        Test plan for testing if loading data from server works:
        1. Launch the client application.
        2. Join a room.
        3. The questions for the chosen room should appear as a list in the centre of the window.
        4. There should be a "0:00" timer and the tooFast and tooSlow buttons should be enabled.
         */
    }

    @Test
    void buttonFastClicked() {
        /* Manual test plan to be carried out when testing if everything works correctly.
        Test plan for testing if buttonTooFast works:
        1. Launch the client application.
        2. Join a room.
        3. Upon pressing the button, both tooSlow and tooFast buttons should get disabled.
        4. A 5-minute countdown should initiate.
        5. A request to the server should be send to increase tooFast counter prior the countdown.
        6. After the countdown is over, both tooSlow and tooFast buttons should get enabled.
        7. A request to the server should be send to decrease tooFast counter after the countdown.
         */
    }

    @Test
    void buttonSlowClicked() {
        /* Manual test plan to be carried out when testing if everything works correctly.
        Test plan for testing if buttonTooSlow works:
        1. Launch the client application.
        2. Join a room.
        3. Upon pressing the button, both tooSlow and tooFast buttons should get disabled.
        4. A 5-minute countdown should initiate.
        5. A request to the server should be send to increase tooSlow counter prior the countdown.
        6. After the countdown is over, both tooSlow and tooFast buttons should get enabled.
        7. A request to the server should be send to decrease tooSlow counter after the countdown.
         */
    }

    @Test
    void buttonSendClicked() {
        /* Manual test plan to be carried out when testing if everything works correctly.
        Test plan for testing if sending questions to server works:
        1. Launch the client application.
        2. Join a room.
        3. Type your question in the textBox and press button Send when ready.
        4. A request to the server should be send to add a new question.
        5. After a few seconds, your question should appear in the questionList.
         */
    }

    /**
     * See {@link #loadData()}.
     */
    @Test
    void fetchData() {
        // Since this method cannot be tested on it's own, follow the linked test plan.
    }

    /**
     * See {@link #loadData()}.
     */
    @Test
    void populateListView() {
        // Since this method cannot be tested on it's own, follow the linked test plan.
    }

    /**
     * See {@link #loadData()}.
     */
    @Test
    void setCountdown() {
        // Since this method cannot be tested on it's own, follow the linked test plan.
    }
}