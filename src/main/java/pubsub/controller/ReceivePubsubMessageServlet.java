package pubsub.controller;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.pubsub.model.PubsubMessage;

public class ReceivePubsubMessageServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ServletInputStream reader = req.getInputStream();
        // Parse the JSON message to the POJO model class.
        JsonParser parser =
                JacksonFactory.getDefaultInstance().createJsonParser(reader);
        parser.skipToKey("message");
        PubsubMessage message = parser.parseAndClose(PubsubMessage.class);
        // Base64-decode the data and work with it.
        String data = new String(message.decodeData(), "UTF-8");
        // Work with your message
        System.out.println(data);
        // Respond with a 20X to acknowledge receipt of the message.
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.getWriter().close();
    }
	

}
