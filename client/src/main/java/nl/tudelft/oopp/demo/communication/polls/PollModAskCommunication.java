package nl.tudelft.oopp.demo.communication.polls;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import nl.tudelft.oopp.demo.data.Poll;
import nl.tudelft.oopp.demo.data.Room;
import nl.tudelft.oopp.demo.data.helper.PollHelper;


public class PollModAskCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();
    private static Gson gson = new Gson();



    /**
     * Checks if a poll is already created, and if it isn't creates that poll.
     * Otherwise updates the information of the poll.
     *
     * @param pollHelper the poll that is added in the backend.
     */
    public static void createPoll(PollHelper pollHelper) {
        //Add checker for if poll already created, update information instead.
        String url = "http://localhost:8080/api/v1/polls/createPoll?";

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(pollHelper)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Sets the status of the poll to the given one in the parameters.
     *
     * @param poll the poll to change.
     * @param status The new status.
     */
    public static void setStatus(Poll poll, Poll.PollStatus status) {
        String url = "http://localhost:8080/api/v1/polls/status?";

        url = url + "&pollId=" + poll.getId();
        url = url + "&status=" + status.toString();

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

    }
}