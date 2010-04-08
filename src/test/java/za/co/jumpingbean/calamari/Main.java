/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mark
 */
public class Main {

        public static void main(String[] args) throws IOException {

            final String baseUri = "http://localhost:9998/";
            final Map<String, String> initParams =
   	                       new HashMap<String, String>();

            initParams.put("com.sun.jersey.config.property.packages",
                   "za.co.jumpingbean.calamari.services");

          System.out.println("Starting grizzly...");
          SelectorThread threadSelector =
  	          GrizzlyWebContainerFactory.create(baseUri, initParams);
          System.out.println(String.format(
            "Jersey app started with WADL available at %sapplication.wadl\n" +
            "Try out %shelloworld\nHit enter to stop it...", baseUri, baseUri));
          System.in.read();
          threadSelector.stopEndpoint();
          System.exit(0);
      }
  }