
package com.stellanow;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.renderer.v2.RenderMode;

public class XportalRemoteAuthMacro implements Macro
{
	public XportalRemoteAuthMacro()
	{
	}

	public boolean hasBody()
	{
		return false;
	}

	public RenderMode getBodyRenderMode()
	{
		return RenderMode.NO_RENDER;
	}

	@Override
	public BodyType getBodyType()
	{
		return BodyType.NONE;
	}

	@Override
	public OutputType getOutputType()
	{
		return OutputType.BLOCK;
	}


	@Override
	public String execute(Map<String, String> parameters, String body, ConversionContext context) throws MacroExecutionException
	{
		Map<String, Object> contextMap = MacroUtils.defaultVelocityContext(); //the static
		//Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("token", parameters.get("token"));
		contextMap.put("url", parameters.get("url"));

		return VelocityUtils.getRenderedTemplate("vm/stella-firebase-auth.vm", contextMap);
	//return "hello";
	}
}