package nl.tudelft.oopp.demo.communication.mainmenu;

public class MainModCommunication extends MainMenuCommunication {
    private static final String url = "http://localhost:8080/api/v2/";

    /**
     * Request all questions for a chosen room.
     * @param id id of a room
     * @return all questions in json format
     */
    public static String getAllQuestions(long id) {
        String link = url + "questions/exportAll?roomId=";
        return  requestStringData(link + id);
    }

    /**
     * Request top 20 questions for a chosen room.
     * @param id id of a room
     * @return top 20 questions in json format
     */
    public static String getTopQuestions(long id) {
        String link = url + "questions/exportTop?amount=20&roomId=";
        return  requestStringData(link + id);
    }

    /**
     * Request all questions with text answer for a chosen room.
     * @param id id of a room
     * @return answered questions in json format
     */
    public static String getAnsweredQuestions(long id) {
        String link = url + "questions/exportAnswered?roomId=";
        return  requestStringData(link + id);
    }

    /**
     * Changes if the lecture is ongoing or not.
     * @param roomId id of room
     * @param userId id of user
     * @param isOngoing new lecture status
     */
    public static void setOngoingLecture(long roomId, boolean isOngoing, long userId) {
        String link = url + "rooms/setOngoing?";
        link = link + "roomId=" + roomId;
        link = link + "&isOngoing=" + isOngoing;
        link = link + "&userId=" + userId;
        sendEmptyPutRequest(link);
    }

}
