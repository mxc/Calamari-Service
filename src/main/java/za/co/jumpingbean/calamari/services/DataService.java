/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.services;

import za.co.jumpingbean.calamari.support.StartDateTimeParam;
import za.co.jumpingbean.calamari.support.EndDateTimeParam;
import za.co.jumpingbean.calamari.dal.DBException;
import za.co.jumpingbean.calamari.dal.JDBCHelper;
import java.sql.Timestamp;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.jumpingbean.calamari.dal.RecordDetailResultSetHandler;
import za.co.jumpingbean.calamari.dal.ChartDataPointResultSetHandler;
import za.co.jumpingbean.calamari.model.ChartDataPoint;
import za.co.jumpingbean.calamari.model.SquidLogRecord;


/**
 *
 * @author mark
 */
@Path("/dataservice")
@Produces({MediaType.TEXT_XML})
public class DataService {

    private final JDBCHelper helper = JDBCHelper.getJDBCHelper();
    private final Logger logger = LoggerFactory.getLogger(DataService.class);


    @GET
    @Path("/topsitesbyhits/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{count: [0-9]*}")
    public List<ChartDataPoint> getTopSitesByHits(@PathParam("count")int topx,@PathParam("startDate")StartDateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
      try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql = String.format("Select domain,count(*) as hits,sum(bytes) as size from squidlog where accessDate between '%s' and '%s' group by domain order by  count(*) desc fetch first %d rows only", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(),topx);
            return helper.getResultSet(sql, new ChartDataPointResultSetHandler("domain"));
        } catch (DBException ex) {
            logger.error("there was an error getting top sites by request "+ex.getMessage());
            throw new ServiceException("there was an error getting top sites by request "+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/topsitesbysize/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{count: [0-9]*}")
    public List<ChartDataPoint> getTopSitesBySize(@PathParam("count")int topx,@PathParam("startDate")StartDateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql = String.format("Select domain,count(*) as hits,sum(bytes) as size from squidlog where accessDate between '%s' and '%s' group by domain order by  sum(bytes) desc fetch first %d rows only", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), topx);
            return helper.getResultSet(sql, new ChartDataPointResultSetHandler("domain"));
        } catch (DBException ex) {
            logger.error("there was an error getting top sites by size "+ex.getMessage());
            throw new ServiceException("there was an error getting top sites by size "+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/contenttypeDetails/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{count: [0-9]*}")
    public List<ChartDataPoint> getContentType(@PathParam("count")int topx,@PathParam("startDate")StartDateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql = String.format("Select contentType,count(*) as hits,sum(bytes) as size from squidlog where accessDate between '%s' and '%s' group by contenttype order by  sum(bytes) desc fetch first %d rows only", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), topx);
            return helper.getResultSet(sql, new ChartDataPointResultSetHandler("contenttype"));
        } catch (DBException ex) {
            logger.error("there was an error getting content type data "+ex.getMessage());
            throw new ServiceException("there was an error getting content type data "+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/topusersbyhits/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{count: [0-9]*}")
    public List<ChartDataPoint> getTopUsersByHits (@PathParam("count")int topx,@PathParam("startDate")StartDateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql = String.format("Select rfc931,count(*) as hits,sum(bytes) as size from squidlog where accessDate between '%s' and '%s' and rfc931 not like '-' group by rfc931 order by  count(*) desc fetch first %d rows only", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), topx);
            return helper.getResultSet(sql, new ChartDataPointResultSetHandler("rfc931"));
        } catch (DBException ex) {
            logger.error("there was an error getting top users by hits "+ex.getMessage());
            throw new ServiceException("there was an error getting top users by hits "+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/topusersbysize/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{count: [0-9]*}")
    public List<ChartDataPoint> getTopUsersBySize(@PathParam("count")int topx,@PathParam("startDate")StartDateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql = String.format("Select rfc931,count(*) as hits,sum(bytes) as size from squidlog where accessDate between '%s' and '%s' and rfc931 not like '-' group by rfc931 order by  sum(bytes) desc fetch first %d rows only", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), topx);
            return helper.getResultSet(sql, new ChartDataPointResultSetHandler("rfc931"));
        } catch (DBException ex) {
            logger.error("there was an error getting top users by size "+ex.getMessage());
            throw new ServiceException("there was an error getting top  users by size "+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/userdetails/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{username: [a-z][A-z]*}")
    public List<SquidLogRecord> getDetailsForUser(@PathParam("username")String user,@PathParam("startDate")StartDateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql = String.format("Select * from squidlog where accessDate between '%s' and '%s' and rfc931 like '%s' order by  accessDate desc", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), user);
            return helper.getResultSet(sql, new RecordDetailResultSetHandler());
        } catch (DBException ex) {
            logger.error("there was an error getting top sites by request"+ex.getMessage());
            throw new ServiceException("there was an error getting details for users "+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/domaindetails/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{domain}")
    public List<SquidLogRecord> getDetailsForDomain(@PathParam("domain")String domain,@PathParam("startDate")StartDateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql = String.format("Select * from squidlog where accessDate between '%s' and '%s' and domain like '%%%s%%' order by  accessDate desc", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), domain);
            return helper.getResultSet(sql, new RecordDetailResultSetHandler());
        } catch (DBException ex) {
            logger.error("there was an error getting details for domain "+ex.getMessage());
            throw new ServiceException("there was an error getting top sites by request"+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/contenttypedetails/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{contenttype: [a-z][A-z]*}")
    public List<SquidLogRecord> getDetailsForCotentType(@PathParam("contenttype")String contentType,@PathParam("startDate")StartDateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql = String.format("Select * from squidlog where accessDate between '%s' and '%s' and contenttype like '%s' order by  accessDate desc", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), contentType);
            return helper.getResultSet(sql, new RecordDetailResultSetHandler());
        } catch (DBException ex) {
            logger.error("there was an error getting details for content type "+ex.getMessage());
            throw new ServiceException("there was an error getting top sites by request "+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/details/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}")
    public List<SquidLogRecord> getDetails(@PathParam("contenttype")String contentType,@PathParam("startDate")StartDateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql = String.format("Select * from squidlog where accessDate between '%s' and '%s' order by  accessDate desc", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), contentType);
            return helper.getResultSet(sql, new RecordDetailResultSetHandler());
        } catch (DBException ex) {
            logger.error("there was an error getting details "+ex.getMessage());
            throw new ServiceException("there was an error getting top sites by request "+ex.getMessage(),ex);
        }
    }


}
