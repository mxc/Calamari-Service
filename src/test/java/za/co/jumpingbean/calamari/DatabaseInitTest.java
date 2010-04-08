package za.co.jumpingbean.calamari;

import com.sun.jersey.api.client.WebResource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.jumpingbean.calamari.dal.DBException;
import za.co.jumpingbean.calamari.model.ChartDataPoint;
import za.co.jumpingbean.calamari.services.DataService;
import za.co.jumpingbean.calamari.services.ServiceException;


/**
 * Unit test for simple App.
 */
public class DatabaseInitTest extends AbstractUnitTest
    
{
    private final Logger logger = LoggerFactory.getLogger(DatabaseInitTest.class);

    @Test
    public void initDB(){
            //First create a database
            logger.debug("------------------ initDB ---------------");
            WebResource resource = client.resource("http://127.0.0.1:9998/admin/initdb");
            String response = resource.accept(MediaType.TEXT_PLAIN).get(String.class);
            logger.info("response = "+response);
            if (response.contains("existing database found, taking no action.") && !response.contains("New database initialised.")){
                logger.debug("database already exists");
                return;
            } else {
                logger.debug("database did not exist");
                if (!response.contains("new database initialised.")) {
                    Assert.assertFalse("web serivice did not create new database", true);
                    return;
                }else return;
            }
    }

    @Test
    public void initDBExists(){
            logger.debug("------------------ initDBExists ---------------");
            WebResource resource = client.resource("http://127.0.0.1:9998/admin/initdb?dropifexists=true");
            String response = resource.accept(MediaType.TEXT_PLAIN).get(String.class);
            logger.info("response = "+response);
            if (!response.contains("old database deleted.")) Assert.assertFalse("web serivice did not drop existing database", true);
            if (!response.contains("new database initialised.")) Assert.assertFalse("web serivice did not create new db when dropping old db", true);
    }

    @Test
    public void importLogFileTest() {
        String enc;
        try {
            enc = URLEncoder.encode("/home/mark/workspace/logs/squid", "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Assert.assertFalse("UTF-8 encoding not supported",true);
            return;
        }
        
        WebResource resource = client.resource("http://127.0.0.1:9998/admin/settings/squidlogfolder?path="+enc);
        String response = resource.accept(MediaType.TEXT_PLAIN).post(String.class);
        logger.debug("response="+response);
        if (!response.contains("config updated")) {
            Assert.assertFalse("log folder was not set! ",true);
            return;
        }
        resource = client.resource("http://127.0.0.1:9998/admin/settings/squidlogfolder");
        response = resource.accept(MediaType.TEXT_PLAIN).get(String.class);
        logger.debug("response="+response);
        if (!response.contains("/home/mark/workspace/logs/squid")) {
            Assert.assertFalse("log folder was not retrieved ",true);
            return;
        }
        resource = client.resource("http://127.0.0.1:9998/admin/importlogfiles");
        response = resource.accept(MediaType.TEXT_PLAIN).get(String.class);
        logger.debug("response="+response);
        if (!response.contains("importing log files....")) {
            Assert.assertFalse("log file import not started ",true);
            return;
        }
        //Wait for import to finish....
        boolean wait = true;
        while(wait){
        resource = client.resource("http://127.0.0.1:9998/admin/importlogfilesstatus");
        response = resource.accept(MediaType.TEXT_PLAIN).get(String.class);
        if (!response.contains("no import in progress")){
            logger.info(response);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {

            }
        }else{
            wait=false;
        }
        }
    }





    @After
    public void shutdownDB() {
        DataService dao = new DataService();
        //dao.shutdownDB();
    }
}
