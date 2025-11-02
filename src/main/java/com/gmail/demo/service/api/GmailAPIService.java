package com.gmail.demo.service.api;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

@Service
public class GmailAPIService {

	@Autowired
	private OAuth2AuthorizedClientManager authorizedClientManager;

	public OAuth2AccessToken getValidAccessToken() {

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.isAuthenticated()) {
				Object principal = authentication.getPrincipal();
				if (principal instanceof UserDetails) {
					System.out.println("((UserDetails) principal).getUsername()  ::::: "
							+ ((UserDetails) principal).getUsername());
				} else {
					System.out.println("  principal.toString() ::::: " + principal.toString());
				}
			}
			OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("google")
//					.principal(authentication)
					.principal("snehademo27@gmail.com").build();

			OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(authorizeRequest);

			if (authorizedClient == null) {
				throw new IllegalStateException("Authorization failed!");
			}

			return authorizedClient.getAccessToken();
		} catch (Exception e) {
			System.err.println("exception occured in getValid AccessToken");
//			e.printStackTrace();
			return null;
		}
	}

	public Gmail getGmailService(String accessToken, Instant expiresAt) throws Exception {

		AccessToken token = new AccessToken(accessToken, Date.from(expiresAt));

		GoogleCredentials credentials = GoogleCredentials.create(token)
				.createScoped(Collections.singletonList("https://mail.google.com/"));

		HttpCredentialsAdapter requestInitializer = new HttpCredentialsAdapter(credentials);

		return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(),
				requestInitializer).setApplicationName("GmailCloneApp").build();
	}

	public List<String> getSnoozedMsgIds() throws Exception {

		OAuth2AccessToken token = getValidAccessToken();

		System.err.println(" token ::::::::::::::::::  " + token);

		if (token != null) {

			System.out.println(" token.getTokenValue() :::::::::  " + token.getTokenValue());

			/// param 1 is accessToken and param 2 is accessToken expiresAt time
			Gmail service = getGmailService(token.getTokenValue(), token.getExpiresAt());

			ListMessagesResponse response = service.users().messages().list("me").setQ("in:snoozed").execute();

			List<String> msgIds = new ArrayList<String>();
			List<Message> messages = response.getMessages();
			if (messages != null && !messages.isEmpty()) {
				for (Message msg : messages) {

					String GmailMsgId = msg.getId();

					Message fullMessage = service.users().messages().get("me", GmailMsgId).setFormat("full").execute();
					List<MessagePartHeader> headers = fullMessage.getPayload().getHeaders();
					for (MessagePartHeader header : headers) {
						if ("Message-ID".equalsIgnoreCase(header.getName())) {
							msgIds.add(header.getValue());
						}
					}
				}
			}
			return msgIds;
		}
		return new ArrayList<String>();

	}

}

