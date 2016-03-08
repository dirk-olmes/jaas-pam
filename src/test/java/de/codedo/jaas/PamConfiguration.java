package de.codedo.jaas;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;

public class PamConfiguration extends Configuration
{
	protected static final String CONFIG_NAME = "pam";
	protected static final String CONFIG_NO_SERVICE_NAME = "pam-no-service";

	@Override
	public AppConfigurationEntry[] getAppConfigurationEntry(String name)
	{
		if (CONFIG_NAME.equals(name))
		{
			return createPamConfig();
		}
		else if (CONFIG_NO_SERVICE_NAME.equals(name))
		{
			return createPamWithoutServiceConfig();
		}
		return null;
	}

	private AppConfigurationEntry[] createPamWithoutServiceConfig()
	{
		return createConfig(null);
	}

	private AppConfigurationEntry[] createPamConfig()
	{
		return createConfig("sshd");
	}

	private AppConfigurationEntry[] createConfig(String service)
	{
		Map<String, String> options = new HashMap<>();
		if (service != null)
		{
			options.put(PamLoginModule.SERVICE_KEY, service);
		}

		String loginModule = PamLoginModule.class.getName();
		AppConfigurationEntry pamEntry = new AppConfigurationEntry(loginModule, LoginModuleControlFlag.REQUIRED, options);

		return new AppConfigurationEntry[] { pamEntry };
	}
}
