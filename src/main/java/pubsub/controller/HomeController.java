package pubsub.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pubsub.publisher.PublisherUtils;
import pubsub.utils.ConfigParameters;
import pubsub.utils.PubsubUtils;

/**
 * Handles requests for the application home page.
 */


public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
	        String projectId = ConfigParameters.APPLICATION_NAME;
	        request.setAttribute("project", projectId);

	        PublisherUtils.createTopic("mytopic");
	        PubsubUtils.createSubscription();

	        request.setAttribute("topic", PubsubUtils.getAppTopicName());
	        request.setAttribute("subscription",PubsubUtils.getAppSubscriptionName());
	        request.setAttribute("subscriptionEndpoint",
	                PubsubUtils.getAppEndpointUrl()
	                        .replaceAll("token=[^&]*", "token=REDACTED"));

	        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/main.jsp");
	        try {
	            rd.forward(request, response);
	        } catch (ServletException e) {
	        	e.printStackTrace();
	            
	        }catch (IOException e){
	        	e.printStackTrace();
	        }

	}

}
