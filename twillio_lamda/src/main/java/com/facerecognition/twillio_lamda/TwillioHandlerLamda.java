package com.facerecognition.twillio_lamda;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.exception.TwilioException;
import com.twilio.rest.verify.v2.Service;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

/**
 * A request handler loading a Spring context and using spring supported Jersey
 * resources.
 */
public class TwillioHandlerLamda implements RequestHandler<AwsProxyRequest, Map<String, String>> {

	private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
	private static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
	private HashMap<String, String> errorResHashMap = new HashMap<>();

	public TwillioHandlerLamda() {
		errorResHashMap.put("status", "400");
		errorResHashMap.put("errorResponse", "Credentials are'nt up to date");
	}

	public Map<String, String> handleRequest(AwsProxyRequest input, Context context) {
		HashMap<String, String> responsemap = null;
		final Boolean contain=input.getPath().contains("generateotp");
		final String method=input.getHttpMethod();
		if (input != null && contain && method== "POST") {

			final String jsonBody = input.getBody();
			if (jsonBody != null) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = null;
				;
				try {
					map = new ObjectMapper().readValue(jsonBody, HashMap.class);
				} catch (IOException e) {
					return errorResHashMap;
				}

				final String countryCode = map.get("countryCode");
				final String mobileNumber = map.get("number");
				final String type = map.get("type");
				if (countryCode != null && mobileNumber != null && type != null) {
					Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

					final String mobNumber = "+".concat(countryCode).concat(mobileNumber);
					final Service service = Service.creator("My First Verify Service").create();
					final String sid = service.getSid();
					final Verification verification = Verification.creator(sid, mobNumber, type).create();
					responsemap = new HashMap<String, String>();
					responsemap.put("id", sid);
					responsemap.put("status", verification.getStatus());
					return responsemap;
				}

			}
			return errorResHashMap;
		}
		final Boolean containVerify=input.getPath().contains("verifyotp");
		final String methodVerify=input.getHttpMethod();
		if (input != null && containVerify && methodVerify.equalsIgnoreCase("POST")) {
			final String jsonBody = input.getBody();
			if (jsonBody != null) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = null;
				try {
					map = new ObjectMapper().readValue(jsonBody, HashMap.class);
				}catch (IOException e) {
				return errorResHashMap;
				}
				final String countryCode = map.get("countryCode");
				final String mobileNumber = map.get("number");
				final String sid = map.get("sid");
				final String otp = map.get("otp");
				if (countryCode != null && mobileNumber != null && otp != null && sid != null) {
					Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
					final String mobNumber = "+".concat(countryCode).concat(mobileNumber);
					try {
						
					VerificationCheck verificationCheck = (VerificationCheck) VerificationCheck.creator(sid, otp)
							.setTo(mobNumber).create();
					responsemap = new HashMap<String, String>();
					responsemap.put("id", sid);
					responsemap.put("status", verificationCheck.getStatus());
					}catch (ApiException e) {
						
						return errorResHashMap;
					}
					
					return responsemap;
				}

			}
		return errorResHashMap;
		}
		errorResHashMap.put("url", "please check your url");
		return errorResHashMap;
	}
}
