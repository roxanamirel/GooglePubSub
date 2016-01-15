package pubsub.publisher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.google.api.services.pubsub.Pubsub;
import com.google.api.services.pubsub.model.ListTopicsResponse;
import com.google.api.services.pubsub.model.PublishRequest;
import com.google.api.services.pubsub.model.PublishResponse;
import com.google.api.services.pubsub.model.PubsubMessage;
import com.google.api.services.pubsub.model.Topic;
import com.google.common.collect.ImmutableList;

import pubsub.test.PortableConfiguration;

public class PublisherUtils {

	private static final String PROJECT_NAME = "pubsub-1190";

	public static void createTopic(String topicName) {
		Pubsub pubsub;
		try {
			pubsub = PortableConfiguration.createPubsubClient();
			Topic newTopic = pubsub.projects().topics()
					.create("projects/" + PROJECT_NAME + "/topics/" + topicName, new Topic()).execute();
			System.out.println("Created: " + newTopic.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void publishMessageToTopic(String topicName, String message) {
		Pubsub pubsub;

		PubsubMessage pubsubMessage = new PubsubMessage();
		// You need to base64-encode your message with
		// PubsubMessage#encodeData() method.
		try {
			pubsub = PortableConfiguration.createPubsubClient();
			pubsubMessage.encodeData(message.getBytes("UTF-8"));
			List<PubsubMessage> messages = ImmutableList.of(pubsubMessage);
			PublishRequest publishRequest = new PublishRequest().setMessages(messages);
			PublishResponse publishResponse;
			publishResponse = pubsub.projects().topics()
					.publish("projects/" + PROJECT_NAME + "/topics/" + topicName, publishRequest).execute();
			List<String> messageIds = publishResponse.getMessageIds();
			if (messageIds != null) {
				for (String messageId : messageIds) {
					System.out.println("messageId: " + messageId);
				}
			}

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listTopics() {
		Pubsub pubsub;
		try {
			pubsub = PortableConfiguration.createPubsubClient();
			Pubsub.Projects.Topics.List listMethod = pubsub.projects().topics().list("projects/" + PROJECT_NAME);
			String nextPageToken = null;
			ListTopicsResponse response;
			do {
				if (nextPageToken != null) {
					listMethod.setPageToken(nextPageToken);
				}
				response = listMethod.execute();
				List<Topic> topics = response.getTopics();
				if (topics != null) {
					for (Topic topic : topics) {
						System.out.println("Found topic: " + topic.getName());
					}
				}
				nextPageToken = response.getNextPageToken();
			} while (nextPageToken != null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
