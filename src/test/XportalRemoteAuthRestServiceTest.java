import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.mail.Email;
import com.atlassian.user.User;
import com.atlassian.user.impl.DefaultUser;
import com.firebase.client.Firebase;
import com.firebase.client.Logger;
import com.firebase.security.token.TokenGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stellanow.FBClient;
import com.stellanow.SubscriptionCustomer;
import com.stellanow.XportalRemoteAuthRestService;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.apache.log4j.*;
import org.apache.pdfbox.pdmodel.graphics.predictor.Sub;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.HashMap;
import java.util.Map;


import static org.junit.Assert.*;
import com.firebase.client.*;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.LoggerFactory;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
/**
 * Created by Trevor on 13/01/2016.
 */

@RunWith(PowerMockRunner.class)
//@PrepareForTest(XportalRemoteAuthRestService.class)
public class XportalRemoteAuthRestServiceTest {

    //FBClient firebaseClient = mock(FBClient.class);
    //SubscriptionCustomer sc = mock(SubscriptionCustomer.class);
    //@Mock FBClient firebaseClient = new FBClient();



    @InjectMocks private FBClient firebaseClient = new FBClient();
    @Mock private SubscriptionCustomer subscriptionCustomer = new SubscriptionCustomer();

   // @Mock Map<String, SubscriptionCustomer> customers = new HashMap<String, SubscriptionCustomer>(); //
    @InjectMocks Map<String, SubscriptionCustomer> customers = new HashMap<String, SubscriptionCustomer>(); // FIXME: 25/01/2016

    //@Mock Firebase firebasestella; <-- this makes error

/*
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); //enable mockito annotations programmatically - alternative to @RunWith
    }
*/


    /*@Test
    public void authTest() throws Exception {


        //XportalRemoteAuthRestService xprarService = new XportalRemoteAuthRestService(firebaseClient); //require fb client param <--NPE

        XportalRemoteAuthRestService xprarService = mock(XportalRemoteAuthRestService.class);

        //PowerMockito.mockStatic(ResponseBuilder.class); //ResponseBuilder potentially superfluous
        //ResponseBuilder responseBuilder = mock(ResponseBuilder.class); //Powermock as classes methods are static
        PowerMockito.mockStatic(Response.class);
        Response response = mock(Response.class);                      //Same deal

        PowerMockito.mockStatic(XportalRemoteAuthRestService.Data.class);

        XportalRemoteAuthRestService.Data data = mock(XportalRemoteAuthRestService.Data.class); //mocking Data class which is within XportalRemoteAuthRestService

        //PowerMockito.when(xprarService.auth(data)).thenReturn();

        xprarService.auth(data);
        System.out.println(xprarService.auth(data));

        System.out.println(when(xprarService.auth(data)).thenReturn(response));
        System.out.println(when(xprarService.auth(any(XportalRemoteAuthRestService.Data.class))).thenReturn(response));
        System.out.println((xprarService.auth(any(XportalRemoteAuthRestService.Data.class))));

        User user = AuthenticatedUserThreadLocal.get();


    }
*/
    @Test
    public void authTestWithoutMockingInnerClass() {
        XportalRemoteAuthRestService xprarService = mock(XportalRemoteAuthRestService.class);
        PowerMockito.mockStatic(Response.class);
        Response response = mock(Response.class);
        User user = AuthenticatedUserThreadLocal.get();

        XportalRemoteAuthRestService.Data data = new XportalRemoteAuthRestService.Data(); //object instance of inner class, bad to mock inner class as they work together

        data.url = "https://domain.firebaseio.com";

        Firebase firebase = new Firebase(data.url); //would not work until I created a fake dummy class 'Context' in the package 'android.content' source: http://grokbase.com/t/gg/firebase-talk/151qxkpye7/firebase-problems-with-jvm-client
        //doNothing().when(firebaseClient).authenticate(firebase, ""); //seems bad to do but unsure how to test otherwise //// FIXME: 25/01/2016  need to mock context class somehow as current fix is a temporary work around
        //verify(firebaseClient).authenticate(firebase, data.secret);

        data.secret = "not null";
        firebaseClient.authenticate(firebase, data.secret);                  //FIXME: 22/01/2016 //can't pass null as argument so declared data.secret as String..

        LoggerFactory.getLogger("XportalRemoteAuthRestService").info("request.url: " + data.url);

        //customers = firebaseClient.getCustomers(firebase, user.getEmail()); //FIXME: 22/01/2016  //map initialized @ top of class to mock..
                                                                                                   //this breaks the code

        LoggerFactory.getLogger("XportalRemoteAuthRestService").info("customers: " + customers.keySet()); //// FIXME: 25/01/2016  loggers shouldnt really be in the test
        firebaseClient.unAuthenticate(firebase);

        if (customers != null && !customers.isEmpty()) { /// FIXME: 25/01/2016 no logic in test classes, should also have multiple tests and shouldnt be so chunky
            Map.Entry<String, SubscriptionCustomer> customer = customers.entrySet().iterator().next();
            LoggerFactory.getLogger("XportalRemoteAuthRestService").info("customer: " + customer.getValue().getEmail());
            Map<String, Object> payload = new HashMap<String, Object>();
            payload.put("uid", customer.getKey());
            payload.put("email", user.getEmail());
            payload.put("name", user.getFullName());

            TokenGenerator tokenGenerator = new TokenGenerator(data.secret);
            String token = tokenGenerator.createToken(payload);

            data.name = user.getFullName();
            data.email = user.getFullName();
            data.jwt = token;
            when(xprarService.auth(data)).thenReturn(response.ok(new Gson().toJson(data)).build());
        }
    }

    @Test public void smallTest()
    {

    }
}



//            when(Response.ok()).thenReturn();


            //return Response.ok(new Gson().toJson(d)).build();





















