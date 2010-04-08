/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.jumpingbean.calamari.model.ChartDataPoint;
import za.co.jumpingbean.calamari.model.SquidLogRecord;
import za.co.jumpingbean.calamari.services.DataService;
import za.co.jumpingbean.calamari.services.ServiceException;

/**
 *
 * @author mark
 */
public class DataTest extends AbstractUnitTest{

    private final Logger logger = LoggerFactory.getLogger(DataTest.class);


    @Test
    public void getTopSitesByRequest() throws ServiceException{
        logger.debug("------------------ Get Top Sites By Hits ---------------");
        WebResource resource = client.resource("http://127.0.0.1:9998/dataservice/topsitesbyhits/20100331/20100331/10");
        GenericType<Collection<ChartDataPoint>> genericType = new GenericType<Collection<ChartDataPoint>>() {};
        Collection<ChartDataPoint> response = resource.accept(MediaType.TEXT_XML).get(genericType);
        if (response==null || response.size()==0){
            Assert.assertFalse("top sites by hits list is empty or null", true);
        }
        for (ChartDataPoint tmpData: response){
            logger.debug(tmpData.getName()+" = " + tmpData.getHits() +","+ tmpData.getBytes());
       }
    }
//
    @Test
    public void getTopSitesBySize() throws ServiceException{
        logger.debug("------------------ Get Top Sites By Size ---------------");
        WebResource resource = client.resource("http://127.0.0.1:9998/dataservice/topsitesbysize/20100331/20100331/10");
        GenericType<Collection<ChartDataPoint>> genericType = new GenericType<Collection<ChartDataPoint>>() {};
        Collection<ChartDataPoint> response = resource.accept(MediaType.TEXT_XML).get(genericType);
        if (response==null || response.size()==0){
            Assert.assertFalse("top sites by size list is empty or null", true);
        }
        for (ChartDataPoint tmpData: response){
            logger.debug(tmpData.getName()+" = " + tmpData.getBytes() +","+ tmpData.getHits());
       }
    }

    @Test
    public void getTopUsersBySize() throws ServiceException{
        logger.debug("------------------ Get Top Users By Size ---------------");
        WebResource resource = client.resource("http://127.0.0.1:9998/dataservice/topusersbysize/20100331/20100331/10");
        GenericType<Collection<ChartDataPoint>> genericType = new GenericType<Collection<ChartDataPoint>>() {};
        Collection<ChartDataPoint> response = resource.accept(MediaType.TEXT_XML).get(genericType);
        if (response==null || response.size()==0){
            Assert.assertFalse("top users by size list is empty or null", true);
        }
        for (ChartDataPoint tmpData: response){
           logger.debug(tmpData.getName()+" = " + tmpData.getBytes() +","+ tmpData.getHits());
       }
    }

    @Test
    public void getTopUsersByHits() throws ServiceException{
        logger.debug("------------------ Get Top Users By Hits ---------------");
        WebResource resource = client.resource("http://127.0.0.1:9998/dataservice/topusersbyhits/20100331/20100331/10");
        GenericType<Collection<ChartDataPoint>> genericType = new GenericType<Collection<ChartDataPoint>>() {};
        Collection<ChartDataPoint> response = resource.accept(MediaType.TEXT_XML).get(genericType);
        if (response==null || response.size()==0){
            Assert.assertFalse("top users by size list is empty or null", true);
        }
        for (ChartDataPoint tmpData: response){
            logger.debug(tmpData.getName()+" = " + tmpData.getHits()  +","+ tmpData.getBytes());
        }
    }

    @Test
    public void getUserDetails() throws ServiceException{
        logger.debug("------------------ Get Users Details ---------------");
        WebResource resource = client.resource("http://127.0.0.1:9998/dataservice/userdetails/20100331/20100331/joseph");
        GenericType<Collection<SquidLogRecord>> genericType = new GenericType<Collection<SquidLogRecord>>() {};
        Collection<SquidLogRecord> response = resource.accept(MediaType.TEXT_XML).get(genericType);
        if (response==null || response.size()==0){
            Assert.assertFalse("user details list is empty or null", true);
        }
        int counter = 0;
        for (SquidLogRecord tmpData: response){
            counter++;
            logger.debug(tmpData.getRfc931()+" = " + tmpData.getAccessDate().toString()  +","+ tmpData.getDomain());
            if (counter>10) break;
        }
    }
//
    @Test
    public void getDomainDetails() throws ServiceException, UnsupportedEncodingException{
        logger.debug("------------------ Get Domain Details ---------------");
        String enc = URLEncoder.encode("google.com","UTF-8");
        WebResource resource = client.resource("http://127.0.0.1:9998/dataservice/domaindetails/20100331/20100331/"+enc);
        GenericType<Collection<SquidLogRecord>> genericType = new GenericType<Collection<SquidLogRecord>>() {};
        Collection<SquidLogRecord> response = resource.accept(MediaType.TEXT_XML).get(genericType);
        if (response==null || response.size()==0){
            Assert.assertFalse("user details list is empty or null", true);
        }
        int counter = 0;
        for (SquidLogRecord tmpData: response){
            counter++;
            logger.debug(tmpData.getDomain()+" = " + tmpData.getAccessDate().toString()  +","+ tmpData.getRfc931());
            if (counter>10) break;
        }
    }
//
//
    @Test
    public void getContentTypeData() throws ServiceException{
        logger.debug("------------------ Get Content Type Data ---------------");
        WebResource resource = client.resource("http://127.0.0.1:9998/dataservice/contenttype/20100331/20100331/10");
        GenericType<Collection<ChartDataPoint>> genericType = new GenericType<Collection<ChartDataPoint>>() {};
        Collection<ChartDataPoint> response = resource.accept(MediaType.TEXT_XML).get(genericType);
        if (response==null || response.size()==0){
            Assert.assertFalse("user details list is empty or null", true);
        }
        for (ChartDataPoint tmpData: response){
            logger.debug(tmpData.getName()+" = " + tmpData.getBytes()  +","+ tmpData.getHits());
        }
    }
//
//    @After
//    public void shutdownDB() {
//        DataService dao = new DataService();
//        //dao.shutdownDB();
//    }
}
