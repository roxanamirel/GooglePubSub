/*
 * Copyright (c) 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package pubsub.utils;

import java.io.IOException;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.services.pubsub.Pubsub;
import com.google.api.services.pubsub.model.PushConfig;
import com.google.api.services.pubsub.model.Subscription;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.apphosting.api.ApiProxy;

import pubsub.test.PortableConfiguration;

/**
 * Utility class to interact with the Pub/Sub API.
 */
public final class PubsubUtils {

	/**
	 * Prevents instantiation.
	 */
	private PubsubUtils() {
	}

	/**
	 * Returns a topic name for this application.
	 *
	 * @return a topic name.
	 */
	public static String getAppTopicName() {
		return "mytopic";
	}

	/**
	 * Returns a subscription name for this application.
	 *
	 * @return a subscription name.
	 */
	public static String getAppSubscriptionName() {
		return "subscription-" + getProjectId();
	}

	/**
	 * Returns the push endpoint URL.
	 *
	 * @return the push endpoint URL.
	 */
	public static String getAppEndpointUrl() {
		String subscriptionUniqueToken = "P5k6yMTy";

		return "https://" + getProjectId() + ".appspot.com/receive_message" + "?token=" + subscriptionUniqueToken;
	}

	/**
	 * Returns the project ID.
	 *
	 * @return the project ID.
	 */
	public static String getProjectId() {
		AppIdentityService identityService = AppIdentityServiceFactory.getAppIdentityService();

		// The project ID associated to an app engine application is the same
		// as the app ID.
		return identityService.parseFullAppId(ApiProxy.getCurrentEnvironment().getAppId()).getId();
	}
	public static void createSubscription() {
		String fullName = String.format("projects/%s/subscriptions/%s",
                PubsubUtils.getProjectId(),
                PubsubUtils.getAppSubscriptionName());

        try {
        	initializePubSubClient().projects().subscriptions().get(fullName).execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == HttpStatusCodes.STATUS_CODE_NOT_FOUND) {
                // Create the subscription if it doesn't exist
                String fullTopicName = String.format("projects/%s/topics/%s",
                        PubsubUtils.getProjectId(),
                        PubsubUtils.getAppTopicName());
                PushConfig pushConfig = new PushConfig()
                        .setPushEndpoint(PubsubUtils.getAppEndpointUrl());
                Subscription subscription = new Subscription()
                        .setTopic(fullTopicName)
                        .setPushConfig(pushConfig);
                try {
					initializePubSubClient() .projects().subscriptions()
					        .create(fullName, subscription)
					        .execute();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } else {
                System.out.println("Error");
            }
        }
            catch(IOException e){
            	e.printStackTrace();
         }
	}
	
	private static Pubsub initializePubSubClient() {
		try {
			Pubsub pubsub = PortableConfiguration.createPubsubClient();
			return pubsub;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
