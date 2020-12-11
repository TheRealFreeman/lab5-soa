import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


import java.util.ArrayList;
import java.util.List;

public class clientTest {

    List<String> responses = new ArrayList<>();

    @Test
    public void websocketClientTest() throws Exception {
        AsyncHttpClient client = new DefaultAsyncHttpClient();

        WebSocket websocket = client.prepareGet("ws://localhost:50000/twitterWS")
                .execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(new WebSocketListener() {
                    @Override
                    public void onOpen(WebSocket websocket) {
                        System.out.println("Connection opened!");
                    }

                    @Override
                    public void onClose(WebSocket websocket, int code, String reason) {
                        System.out.println("Connection closed!");
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("Unhandled exception: {}" + t.getMessage());
                    }

                    @Override
                    public void onTextFrame(String payload, boolean finalFragment, int rsv) {
                        System.out.println("Received --> " + payload);
                        responses.add(payload);
                    }

                }).build()).get();

        websocket.sendTextFrame("Covid max:2");
        websocket.sendTextFrame("Web engineering max:2");
        Thread.sleep(1000);
        assertEquals(2,responses.size());
        websocket.sendCloseFrame();
        client.close();

    }



}