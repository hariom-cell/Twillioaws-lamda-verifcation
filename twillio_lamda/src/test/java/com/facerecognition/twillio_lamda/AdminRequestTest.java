package com.facerecognition.twillio_lamda;

import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.Map;

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.Headers;
import com.amazonaws.services.lambda.runtime.Context;
/**
 *
 */
public class AdminRequestTest {

	private TwillioHandlerLamda controller = new TwillioHandlerLamda();
	private Context lambdaContext = new MockLambdaContext();
	static Headers headers = new Headers();

	@Test
	public void GenerateOtp() throws Exception {

		JSONObject object = new JSONObject();
		object.put("countryCode", "91");
		object.put("number", "9817175487");
		object.put("type", "sms");
                
//		final String countryCode = map.get("countryCode");
//		final String mobileNumber = map.get("number");
//		final String type = map.get("type");
//		final String sid = map.get("sid");
//		final String otp = map.get("otp");

		String path = "/generateotp";

		final AwsProxyRequest request = new AwsProxyRequestBuilder(path, "POST")
				.header("Content-Type", "application/json").body(object).build();
		final Map<String, String> response = controller.handleRequest(request, lambdaContext);
		System.out.println("response +++++++++" + response.toString());

	}

	
	@Test
	public void verifyOtp() throws Exception {

		JSONObject object = new JSONObject();
		object.put("countryCode", "91");
		object.put("number", "9817175487");
		object.put("sid", "VAde6721bc591cd8a3b0dddef9eec86fbb");
		object.put("otp", "889099");
		
//		final String countryCode = map.get("countryCode");
//		final String mobileNumber = map.get("number");
//		final String type = map.get("type");
//		final String sid = map.get("sid");
//		final String otp = map.get("otp");

		String path = "/verifyotp";

		final AwsProxyRequest request = new AwsProxyRequestBuilder(path, "POST")
				.header("Content-Type", "application/json").body(object).build();
		final Map<String, String> response = controller.handleRequest(request, lambdaContext);
		System.out.println("response +++++++++" + response.toString());

	}
	
}
