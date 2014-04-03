package demo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import sparklr.common.AbstractClientCredentialsProviderTests;

/**
 * @author Dave Syer
 */
@SpringApplicationConfiguration(classes=Application.class)
public class ClientCredentialsProviderTests extends AbstractClientCredentialsProviderTests {


	@Test
	@OAuth2ContextConfiguration(ClientCredentials.class)
	public void testPostForToken() throws Exception {
		OAuth2AccessToken token = context.getAccessToken();
		assertNull(token.getRefreshToken());
		assertNotNull("Wrong token value: " + token, JwtHelper.decode(token.getValue()));
	}

}
