import com.stellanow.MockString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sun.xml.bind.v2.schemagen.xmlschema.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

/**
 * Created by Trevor on 14/01/2016.
 */

//MockString and MockStringTest are just two classes I used to test some functions with - wasn't sure if I should delete.


@RunWith(MockitoJUnitRunner.class)
public class MockStringTest {

    MockString ms = mock(MockString.class); //or @Mock MockString ms = new MockString();

    @Before
    public void pretest()
    {
        //System.out.println(ms.testme("testone","testtwo"));
    }

    @Test
    public void teststring() throws Exception
    {

        Mockito.when(ms.testme("","")).thenReturn("threefour"); //mock is null until behavior is assigned
        String result =  ms.testme("","");
        Assert.assertEquals("threefour", result);
        System.out.println(result);



    }
}
