import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.notifications.DefaultEmailService;
import com.atlassian.confluence.util.http.HttpResponse;
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
//import com.sun.java.util.jar.pack.Package;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import com.sun.xml.internal.ws.util.QNameMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.*;
import org.apache.pdfbox.pdmodel.graphics.predictor.Sub;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    //@Mock Firebase firebasestella; <-- this makes error


    @Mock private SubscriptionCustomer subscriptionCustomer = new SubscriptionCustomer();
    @Mock private FBClient firebaseClient = new FBClient();
    @InjectMocks Map<String, SubscriptionCustomer> customers = new HashMap<String, SubscriptionCustomer>(); // FIXME: 25/01/2016


    @Mock XportalRemoteAuthRestService.Data data = new XportalRemoteAuthRestService.Data(); //object instance of inner class, bad to mock inner class as they work together
    @Mock TokenGenerator tokenGenerator;

    @Before
    public void setUp() {
       // MockitoAnnotations.initMocks(this); //enable mockito annotations programmatically - alternative to @RunWith

    }


    @Test
    public void authTestWithoutMockingInnerClass() {



        data.url = "https://domain.firebaseio.com";
        Firebase firebase = new Firebase(data.url); //would not work until I created a fake dummy class 'Context' in the package 'android.content' source: http://grokbase.com/t/gg/firebase-talk/151qxkpye7/firebase-problems-with-jvm-client
        // FIXME: 25/01/2016  need to mock context class somehow as current fix is a temporary work around

        data.secret = "not null";
        firebaseClient.authenticate(firebase, data.secret);                  //FIXME: 22/01/2016 //can't pass null as argument so declared data.secret as String..

        LoggerFactory.getLogger("XportalRemoteAuthRestService").info("request.url: " + data.url);

        //customers = firebaseClient.getCustomers(firebase, user.getEmail()); //FIXME: 22/01/2016  //map initialized @ top of class to mock..
                                                                                                   //this breaks the code
        LoggerFactory.getLogger("XportalRemoteAuthRestService").info("customers: " + customers.keySet()); //// FIXME: 25/01/2016  loggers shouldnt really be in the test
        firebaseClient.unAuthenticate(firebase);

        /// FIXME: 25/01/2016 no logic in test classes, should also have multiple tests and shouldnt be so chunky


        }


    @Test public void customerTest()
    {
        assertFalse("Customers empty", !customers.isEmpty());

        assertNotNull("null", customers);
       // PowerMockito.mockStatic(LoggerFactory.class);

        Map<String, Object> payload = new HashMap<String, Object>();
        TokenGenerator tokenGenerator = new TokenGenerator(data.secret);
        String token = tokenGenerator.createToken(payload);

        data.jwt = token;
    }

    @Test public void userTest() throws Exception
    {
        DefaultUser user = new DefaultUser();
        user.setFullName("test user");
        user.setEmail("Test@user.com");
        assertEquals("test user", user.getFullName());
//        AuthenticatedUserThreadLocal.get();
//        AuthenticatedUserThreadLocal.setUser(user);
//        assertTrue(AuthenticatedUserThreadLocal.get() == user);
    }

    @Test public void tokenTest()
    {
//        TokenGenerator tokenGenerator = new TokenGenerator("test token");
//
//        String token = tokenGenerator.createToken(payload);
//        when(tokenGenerator.createToken(payload)).thenReturn("test token");
//        data.jwt = token;
//        when(data.jwt).thenReturn("test token");
//        assertEquals("test token", token);
        Map<String, Object> payload = new HashMap<String, Object>();
        TokenGenerator tokenGenerator = new TokenGenerator("secret");
        when(tokenGenerator.createToken(payload)).thenReturn("hi");




    }

    @Test public void authReturn()
    {
        XportalRemoteAuthRestService xprarService = mock(XportalRemoteAuthRestService.class);
       // Response builder = Response.status(Response.Status.OK);

        Response builder = Response.status(Response.Status.OK).entity(
                "Your schema was succesfully created!").build();
       // TokenGenerator tokenGenerator = new TokenGenerator("secret");
        when(tokenGenerator.createToken(new HashMap<String, Object>())).thenReturn("hi");
        when(xprarService.auth(data)).thenReturn(builder);
        Response response1 = xprarService.auth(data);
        //when(xprarService.auth(data)).thenReturn(response.ok(new Gson().toJson(data)).build());
       // assertEquals(response1.), xprarService.auth(data)); //similar but diff...

        assertTrue("Your schema was succesfully created!"+response1, response1 != builder);
        assertEquals(response1, builder);

    }

    @Test public void authReturnv2()
    {
        XportalRemoteAuthRestService xprarService = mock(XportalRemoteAuthRestService.class);
        when(tokenGenerator.createToken(new HashMap<String, Object>())).thenReturn("hi");
        //PowerMockito.mockStatic(Response.class);

        Response response = xprarService.auth(data);


        int num = 5;

        System.out.println(response.ok(num));
        assertEquals(response.ok(num), response.ok(5) );

    }

    @Test public void authReturnv3()
    {
        XportalRemoteAuthRestService xprarService = mock(XportalRemoteAuthRestService.class);
        when(tokenGenerator.createToken(new HashMap<String, Object>())).thenReturn("hi");





        //Response response = xprarService.auth(data);

       // assertNull(response);
    }

    @Test public void testInitialize()
    {
               XportalRemoteAuthRestService rest = mock(XportalRemoteAuthRestService.class);

               Response.ResponseBuilder builder = Response.status(Response.Status.OK);

               builder = Response.status(Response.Status.OK).entity("Your schema was succesfully created!");

//               when(rest.initialize(DatabaseSchema)).thenReturn(builder.build());
//
//               String result = rest.initialize(DatabaseSchema).getEntity().toString();
//
//               System.out.println("Here: " + result);
//
//               assertEquals("Your schema was succesfully created!", result);
    }

}














//            when(Response.ok()).thenReturn();


            //return Response.ok(new Gson().toJson(d)).build();




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