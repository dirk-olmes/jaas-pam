// $Id$

package de.codedo.jaas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.junit.BeforeClass;
import org.junit.Test;

public class PamLoginModuleTest extends Object
{
	@Test(expected = LoginException.class)
	public void missingServiceNameShouldThrow() throws Exception
	{
		CallbackHandler handler = new UsernamePasswordCallbackHandler("xxx", "xxx");
		LoginContext context = new LoginContext("pam-no-service", handler);
		context.login();
	}

	@Test(expected = LoginException.class)
	public void nonexistingUserShouldFail() throws Exception
	{
		CallbackHandler handler = new UsernamePasswordCallbackHandler("nonexistent", "xxx");
		LoginContext context = new LoginContext(PamConfiguration.CONFIG_NAME, handler);
		context.login();
	}

	@Test(expected = LoginException.class)
	public void existingUserWithWrongPasswordShouldFail() throws Exception
	{
		String user = getUser();

		CallbackHandler handler = new UsernamePasswordCallbackHandler(user, "xxx");
		LoginContext context = new LoginContext(PamConfiguration.CONFIG_NAME, handler);
		context.login();
	}

	@Test
	public void existingUserWithCorrectPasswordShouldSucceed() throws Exception
	{
		String user = getUser();
		String password = getPassword();

		CallbackHandler handler = new UsernamePasswordCallbackHandler(user, password);
		LoginContext context = new LoginContext(PamConfiguration.CONFIG_NAME, handler);
		context.login();

		boolean pamPrincipalFound = false;
		Subject subject = context.getSubject();
		for (Principal principal : subject.getPrincipals())
		{
			if (principal instanceof PamPrincipal)
			{
				pamPrincipalFound = true;
			}
		}
		assertTrue(pamPrincipalFound);

		context.logout();
	}

	private String getUser()
	{
		String user = System.getProperty("validUser");
		assertNotNull("validUser system property is not set", user);
		return user;
	}

	private String getPassword()
	{
		String password = System.getProperty("validPassword");
		assertNotNull("validPassword system property is not set", password);
		return password;
	}

	@BeforeClass
	public static void initLoginModules()
	{
		Configuration.setConfiguration(new PamConfiguration());
	}

}
