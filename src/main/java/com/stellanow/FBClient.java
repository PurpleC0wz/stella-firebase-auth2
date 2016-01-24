package com.stellanow;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.security.token.TokenGenerator;
import com.firebase.security.token.TokenOptions;

public class FBClient {
	
	private static Logger logger = Logger.getLogger("com.stellanow.FBClient");
	
//	static {
//		logger.setLevel(Level.DEBUG);
//	}
	
	public FBClient() {
	}

				public Entry<String, SubscriptionCustomer> checkUser(Map<String, SubscriptionCustomer> customers, String customerEmail)
				{
					logger.info("checkUser: "+customerEmail);
					if(null != customers && customers.size() > 0)
					{
						for(Map.Entry<String, SubscriptionCustomer> entry : customers.entrySet())
						{
							if((customerEmail).equals(entry.getValue().getEmail()))
							{
								logger.info("checkUser exist: "+customerEmail);
								return entry;
							}
			}
		}
		return null;
	}

	public Map<String, SubscriptionCustomer> getCustomers(Firebase firebase, String email)
	{
		logger.info("getCustomers()");
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Object>> data = (Map<String, Map<String, Object>>) getDataFromFB(firebase.child("users"), email);
		Map<String, SubscriptionCustomer> customers = new HashMap<String, SubscriptionCustomer>();
		if(null != data)
		{
			for (Map.Entry<String, Map<String, Object>> entry : data.entrySet())
			{
				
				logger.info("User Key: " + entry.getKey() + " Value: " + entry.getValue().get("basic"));
				
				SubscriptionCustomer customer = (SubscriptionCustomer) convertObject(new SubscriptionCustomer(), entry.getValue().get("basic"));
				if(customer != null){						
					logger.info("Object convertor: "+customer.getEmail());
					customers.put(entry.getKey(), customer);
//				@SuppressWarnings("unchecked")
//				Map<String, Map<String, Object>> m1 = (Map<String, Map<String,Object>>)customer.getAccess(); 
//				for (Map.Entry<String, Map<String, Object>> entry1 : m1.entrySet()) {
//					logger.info("Key: " + entry1.getKey() + " Value: " + entry1.getValue());
//				}
				}
				// userAvailable = true;
			}
		}
		return customers;
	}
	
	public Object getDataFromFB(Firebase firebase, String email)
	{
		logger.info("getDataFromFB()");
		final CountDownLatch done = new CountDownLatch(1);
		final Map<String, Object> data = new HashMap<String, Object>();
		//firebase.orderByPriority().equalTo(email);
		firebase.orderByPriority().equalTo(email).addValueEventListener(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot snapshot)
			{
				if (snapshot != null)
				{
					data.put("object", snapshot.getValue());
				} else {
					data.put("object", null);
				}
				done.countDown();
			}		

			@Override
			public void onCancelled(FirebaseError firebaseError)
			{
				logger.info("Error: The read failed: " + firebaseError.getMessage());
				done.countDown();
			}
		});
		try {
			logger.info("get data waiting....");
			done.await(5, TimeUnit.SECONDS);
		}
		catch (InterruptedException e)
		{
			logger.info("Interruption Error: "+e.getMessage());
			unAuthenticate(firebase);
		} finally
		{
			logger.info("++++++++++++++++++++++++++++++++get data done!");
		}
		return data.get("object");
	}
	
	public Object convertObject(Object object, Object data){
		logger.info("convertObject()");
		ObjectMapper mapper = new ObjectMapper();
		try {
			byte[] json = mapper.writeValueAsBytes(data);
			return mapper.readValue(json, object.getClass());
		}
		catch (Exception e)
		{
			logger.info("Error: Unable to convert data: "+e.getMessage());
		}
		return null;
	}
	
	public String tokenGenerator(String firebaseSecret) //method not called initially so mock..?
	{
		logger.info("tokenGenerator()");
		Map<String, Object> authPayload = new HashMap<String, Object>();
		authPayload.put("uid", "123");
		authPayload.put("some", "pass");
		authPayload.put("data", "pass");

		TokenOptions tokenOptions = new TokenOptions();
		tokenOptions.setAdmin(true);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		Date currentTimePlusOneMinute = cal.getTime();
		tokenOptions.setExpires(currentTimePlusOneMinute);

		TokenGenerator tokenGenerator = new TokenGenerator(firebaseSecret);
		//tokenGenerator.
		return tokenGenerator.createToken(authPayload, tokenOptions);
	}
	
	public void authenticate(Firebase firebase, String token)
	{
		logger.info("authenticate()");
		//String token = "";
		firebase.authWithCustomToken(token, new Firebase.AuthResultHandler()
		{

			public void onAuthenticated(AuthData authData)
			{
				// Authenticated successfully with payload authData
				logger.info("Authenticated successfully with payload authData");
			}


			public void onAuthenticationError(FirebaseError firebaseError)
			{
				// Authenticated failed with error firebaseError
				logger.info("Error: Authenticated failed with error firebaseError");
			}
		});
	}
	
	public void unAuthenticate(Firebase firebase)
	{
		logger.info("unAuthenticate()");
		firebase.unauth();
		//Firebase.goOffline();
		logger.info("Unauthenticated!");
	}
}
