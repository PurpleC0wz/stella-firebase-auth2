
package com.stellanow;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;

import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.user.User;
import com.firebase.client.Firebase;
import com.firebase.security.token.TokenGenerator;
import com.google.gson.Gson;

@Path("/auth-jwt")
public class XportalRemoteAuthRestService {
	
	private FBClient firebaseClient;
	
	public XportalRemoteAuthRestService(FBClient firebaseClient)
	{
		this.firebaseClient = firebaseClient;
	}

	@POST
	@Consumes(
	{ MediaType.APPLICATION_JSON })
	@Produces(
	{ MediaType.APPLICATION_JSON })
	@Path("/auth")
	public Response auth(final Data request)
	{
		User user = AuthenticatedUserThreadLocal.get();
		Firebase firebase = new Firebase(request.url);	
		firebaseClient.authenticate(firebase, request.secret);
		LoggerFactory.getLogger("XportalRemoteAuthRestService").info("request.url: "+request.url);
		Map<String, SubscriptionCustomer> customers = firebaseClient.getCustomers(firebase, user.getEmail());		
		LoggerFactory.getLogger("XportalRemoteAuthRestService").info("customers: "+customers.keySet());
		
		//Entry<String, SubscriptionCustomer> customer = firebaseClient.checkUser(customers, user.getEmail());
		firebaseClient.unAuthenticate(firebase);
		if(customers != null && !customers.isEmpty()){			
			Entry<String, SubscriptionCustomer> customer = customers.entrySet().iterator().next();
			LoggerFactory.getLogger("XportalRemoteAuthRestService").info("customer: "+customer.getValue().getEmail());
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("uid", customer.getKey());
			payload.put("email", user.getEmail());
			payload.put("name", user.getFullName());
			TokenGenerator tokenGenerator = new TokenGenerator(request.secret);
			String token = tokenGenerator.createToken(payload);
			Data d = new Data();
			d.name = user.getFullName();
			d.email = user.getEmail();
			d.jwt = token;
			
			return Response.ok(new Gson().toJson(d)).build();
		}
		
		return null;
	}

	public static class Data
	{
		public String email, jwt, name, url, secret;

		@Override
		public String toString()
		{
			return "name=" + name + ", email=" + email + ", jwt=" + jwt + ", url=" + url + ", secret=" + secret;
		}
	}
}
