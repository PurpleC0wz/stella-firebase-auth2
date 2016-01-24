import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.velocity.ContextUtils;
import com.atlassian.plugin.ModuleDescriptor;
import com.atlassian.plugin.webresource.UrlMode;
import com.atlassian.plugin.webresource.WebResourceFilter;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.user.util.Assert;
import com.atlassian.util.concurrent.NotNull;
import com.atlassian.velocity.VelocityManager;
import com.google.common.base.Supplier;
import com.stellanow.XportalRemoteAuthMacro;
import com.stellanow.MockString;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.anakia.AnakiaTask;
import org.aspectj.weaver.tools.UnsupportedPointcutPrimitiveException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import com.stellanow.XportalRemoteAuthMacro;

import org.apache.log4j.spi.LoggerFactory;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import com.atlassian.config.util.BootstrapUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by Trevor on 13/01/2016.
 */
@RunWith(PowerMockRunner.class)
//@RunWith(MockitoJUnitRunner.class)

@PrepareForTest(MacroUtils.class) //this is used for classes that need to be byte-code manipulated aka mocking final/static/private methods
public class XportalRemoteAuthMacroTest {



    @Mock ConversionContext conversionContext;

    @Mock VelocityManager vm;

    @Mock MacroUtils mu;

    @InjectMocks  Map<String, Object> contextMap = new HashMap<String, Object>();

    @Before
    public void setUp()
    {


    }

    @Test
    public void powerMacroTest() throws Exception
    {
        XportalRemoteAuthMacro xpraMacro = new XportalRemoteAuthMacro();

        PowerMockito.mockStatic(MacroUtils.class);
       // PowerMockito.mockStatic(VelocityManager.class);
        //PowerMockito.mockStatic(XportalRemoteAuthMacro.class);


        contextMap.put("token", "token");
        contextMap.put("url", "url");


        String output = vm.getBody("vm","stella-firebase-auth.vm", new HashMap<String, Object>());

        PowerMockito.when(VelocityUtils.getRenderedTemplate("vm/stella-firebase-auth.vm", contextMap)).thenReturn(output);

        //Map<String,Object> mockTest = new HashMap<String, Object>();
        //PowerMockito.when(MacroUtils.defaultVelocityContext()).thenReturn(mockTest);                // ContextUtils.toMap(null));
        //MacroUtils.defaultVelocityContext();
        //PowerMockito.verifyStatic();

        String yo = xpraMacro.execute(new HashMap(), "", conversionContext);

        PowerMockito.when(xpraMacro.execute(new HashMap(), "", conversionContext)).thenReturn(output);


        assertTrue("error"+ yo, yo.contains("hello"));

      //  assertTrue("second error" + output, output.contains("hello"));

        //System.out.println(xpraMacro.execute(new HashMap(), "", conversionContext));
        //String result = xpraMacro.execute(new HashMap(),"", conversionContext);
      //  assertEquals(output, result);

    }

    @Test
    public void freshTest()
    {
        PowerMockito.mockStatic(MacroUtils.class);
        {

            PowerMockito.when(MacroUtils.defaultVelocityContext()).thenReturn(new HashMap<String, Object>());
            MacroUtils.defaultVelocityContext();
            PowerMockito.verifyStatic();


        }
    }

    /*
    @Test
    public void nulled() throws Exception
    {
        Mockito.when((xpraMacro.execute(new HashMap(), "", conversionContext)) == null).thenThrow(new NullArgumentException("url/token null"));
    }
*/




  //Always remember that you should mock the objects that your classes come in contact with, because you are interested in the class itself and nothing else.
}
