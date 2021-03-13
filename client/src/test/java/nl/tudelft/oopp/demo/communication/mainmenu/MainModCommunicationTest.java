package nl.tudelft.oopp.demo.communication.mainmenu;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.http.HttpRequest;

import org.junit.jupiter.api.Test;

class MainModCommunicationTest {

    @Test
    void getStudentPassword() {
        assertNotNull(MainModCommunication.getStudentPassword(1));
    }

    @Test
    void getAdminPassword() {
        assertNotNull(MainModCommunication.getAdminPassword(1));
    }

    @Test
    void getQuestions() {
        assertNotNull(MainModCommunication.getQuestions(1));
    }

    @Test
    void getRoom() {
        assertNotNull(MainModCommunication.getRoom(1));
    }

    @Test
    void requestStringData() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/api/v1/rooms/public/" + 1)).build();
        assertNotNull(MainModCommunication.requestStringData(request));
    }
}