/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mark
 */
public abstract class AbstractUnitTest {

    private static SelectorThread threadSelector;
    protected static Client client;
    private static final Logger logger = LoggerFactory.getLogger(DataTest.class);

    @BeforeClass
    public static void setupGrizzly() throws IOException{
      final String baseUri = "http://127.0.0.1:9998/";
      final Map<String, String> initParams = new HashMap<String, String>();
      initParams.put("com.sun.jersey.config.property.packages","za.co.jumpingbean.calamari.services");
      logger.debug("starting grizzly on port 9998 ...");
      threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
      client = Client.create();
    }


@AfterClass
public static void stopGrizzly(){
    logger.debug("stopping grizzly...");
    threadSelector.stopEndpoint();
}

}