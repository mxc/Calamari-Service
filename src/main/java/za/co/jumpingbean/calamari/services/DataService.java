/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.services;

import za.co.jumpingbean.calamari.support.DateTimeParam;
import za.co.jumpingbean.calamari.support.EndDateTimeParam;
import za.co.jumpingbean.calamari.dal.DBException;
import za.co.jumpingbean.calamari.dal.JDBCHelper;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.jumpingbean.calamari.dal.RecordDetailResultSetHandler;
import za.co.jumpingbean.calamari.dal.ChartDataPointResultSetHandler;
import za.co.jumpingbean.calamari.dal.TimeSeriesResultSetHandler;
import za.co.jumpingbean.calamari.model.ChartDataPoint;
import za.co.jumpingbean.calamari.model.SquidLogRecord;
import za.co.jumpingbean.calamari.model.TimeSeriesDataPoint;


/**
 *
 * @author mark
 */
@Path("/dataservice")
@Produces({MediaType.TEXT_XML})
public class DataService {

    private final JDBCHelper helper = JDBCHelper.getJDBCHelper(); // handles all db queries
    private final Logger logger = LoggerFactory.getLogger(DataService.class);


    @GET
    @Path("/topsitesbyhits/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{count: [0-9]*}")
    public List<ChartDataPoint> getTopSitesByHits(@PathParam("count")int topx,@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
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
    public List<ChartDataPoint> getTopSitesBySize(@PathParam("count")int topx,@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
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
    @Path("/contenttype/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{count: [0-9]*}")
    public List<ChartDataPoint> getContentType(@PathParam("count")int topx,@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
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
    public List<ChartDataPoint> getTopUsersByHits (@PathParam("count")int topx,@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
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
    public List<ChartDataPoint> getTopUsersBySize(@PathParam("count")int topx,@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
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
    public List<SquidLogRecord> getDetailsForUser(@PathParam("username")String user,@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            if (user.equalsIgnoreCase("All")) user="%";
            String sql = String.format("Select * from squidlog where accessDate between '%s' and '%s' and rfc931 like '%s' order by  accessDate desc", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), user);
            return helper.getResultSet(sql, new RecordDetailResultSetHandler());
        } catch (DBException ex) {
            logger.error("there was an error getting top sites by request"+ex.getMessage());
            throw new ServiceException("there was an error getting details for users "+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/domaindetails/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{domain}")
    public List<SquidLogRecord> getDetailsForDomain(@PathParam("domain")String domain,@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            if (domain.equalsIgnoreCase("All")) domain="%";
            String sql = String.format("Select * from squidlog where accessDate between '%s' and '%s' and domain like '%%%s%%' order by  accessDate desc", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(), domain);
            return helper.getResultSet(sql, new RecordDetailResultSetHandler());
        } catch (DBException ex) {
            logger.error("there was an error getting details for domain "+ex.getMessage());
            throw new ServiceException("there was an error getting top sites by request"+ex.getMessage(),ex);
        }
    }

    @GET
    @Path("/contenttypedetails/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{contenttype: [a-z][A-z]*}")
    public List<SquidLogRecord> getDetailsForCotentType(@PathParam("contenttype")String contentType,@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
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
    public List<SquidLogRecord> getDetails(@PathParam("contenttype")String contentType,@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
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


    @GET
    @Path("/domainhitsbyhour/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{domain}")
    public List<TimeSeriesDataPoint> getDomainHitsByHour(@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam,@PathParam("domain")String domain) throws ServiceException{
            if (domain.equalsIgnoreCase("All")) domain="%";
            return getTimeSeriesHitData(startDateParam,endDateParam,domain,true,TimeSeriesType.DOMAIN);
    }

    @GET
    @Path("/domainhitsbyday/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{domain}")
    public List<TimeSeriesDataPoint> getDomainHitsByDay(@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam,@PathParam("domain")String domain) throws ServiceException{
            if (domain.equalsIgnoreCase("All")) domain="%";
            return getTimeSeriesHitData(startDateParam,endDateParam,domain,false,TimeSeriesType.DOMAIN);
    }

    @GET
    @Path("/userhitsbyhour/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{user}")
    public List<TimeSeriesDataPoint> getUserHitsByHour(@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam,@PathParam("user")String user) throws ServiceException{
        if (user.equalsIgnoreCase("All")) user="%";
        return getTimeSeriesHitData(startDateParam,endDateParam,user,true,TimeSeriesType.USER);
    }

    @GET
    @Path("/userhitsbyday/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{user}")
    public List<TimeSeriesDataPoint> getUserHitsByDay(@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam,@PathParam("user")String user) throws ServiceException{
        if (user.equalsIgnoreCase("All")) user="%";
        return getTimeSeriesHitData(startDateParam,endDateParam,user,false,TimeSeriesType.USER);
    }

    // Main function to get and process all time series hit data
    private List<TimeSeriesDataPoint> getTimeSeriesHitData(DateTimeParam startDateParam,EndDateTimeParam endDateParam,String parameter,Boolean byHour,TimeSeriesType type) throws ServiceException
    {
        try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql;
            if (type==TimeSeriesType.DOMAIN && byHour) sql= String.format("Select year(accessDate) as year0,month(accessDate) as month0,day(accessDate) as day0,hour(accessDate) as hour0, count(*) as value from squidlog where accessDate between '%s' and '%s'  and rfc931 not like '-' and domain like '%%%s%%'  group by year(accessDate),month(accessDate),day(accessDate),hour(accessDate)", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(),parameter);
            else if (type==TimeSeriesType.DOMAIN && !byHour) sql =String.format("Select year(accessDate) as year0,month(accessDate) as month0,day(accessDate) as day0, count(*) as value from squidlog where accessDate between '%s' and '%s'  and rfc931 not like '-' and domain like '%%%s%%'  group by year(accessDate),month(accessDate),day(accessDate)", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(),parameter);
            else if(byHour) sql= String.format("Select year(accessDate) as year0,month(accessDate) as month0,day(accessDate) as day0,hour(accessDate) as hour0, count(*) as value from squidlog where accessDate between '%s' and '%s'  and rfc931 not like '-' and rfc931 like '%%%s%%'  group by year(accessDate),month(accessDate),day(accessDate),hour(accessDate)", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(),parameter);
            else  sql= String.format("Select year(accessDate) as year0,month(accessDate) as month0,day(accessDate) as day0, count(*) as value from squidlog where accessDate between '%s' and '%s'  and rfc931 not like '-' and rfc931 like '%%%s%%'  group by year(accessDate),month(accessDate),day(accessDate)", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(),parameter);
            logger.debug("SQL = "+ sql);
            List<TimeSeriesDataPoint> points =  helper.getResultSet(sql, new TimeSeriesResultSetHandler(parameter,byHour));
            fillMissingDataPoints(points,byHour,startDate,endDate);
            return points;
        } catch (DBException ex) {
            logger.error("there was an error getting domain hits by hour "+ex.getMessage());
            throw new ServiceException("there was an error getting domain hits by hour "+ex.getMessage(),ex);
        }
    }


    
    @GET
    @Path("/domainsizebyhour/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{domain}")
    public List<TimeSeriesDataPoint> getDomainSizeByHour(@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam,@PathParam("domain")String domain) throws ServiceException{
        if (domain.equalsIgnoreCase("All")) domain="%";
        return this.getTimeSeriesSizeData(startDateParam, endDateParam, domain, Boolean.TRUE,TimeSeriesType.DOMAIN);
    }

    @GET
    @Path("/domainsizebyday/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{domain}")
    public List<TimeSeriesDataPoint> getDomainSizeByDay(@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam,@PathParam("domain")String domain) throws ServiceException{
        if (domain.equalsIgnoreCase("All")) domain="%";
        return this.getTimeSeriesSizeData(startDateParam, endDateParam, domain, Boolean.FALSE,TimeSeriesType.DOMAIN);
    }

    @GET
    @Path("/usersizebyhour/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{user}")
    public List<TimeSeriesDataPoint> getUserSizeByHour(@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam,@PathParam("user")String user) throws ServiceException{
        if (user.equalsIgnoreCase("All")) user="%";
        return this.getTimeSeriesSizeData(startDateParam, endDateParam,user,true,TimeSeriesType.USER);
    }

    @GET
    @Path("/usersizebyday/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/{user}")
    public List<TimeSeriesDataPoint> getUserSizeByDay(@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam,@PathParam("user")String user) throws ServiceException{
        if (user.equalsIgnoreCase("All")) user="%";
        return this.getTimeSeriesSizeData(startDateParam, endDateParam,user,false,TimeSeriesType.USER);
    }

    // Main function to get and process all time series byte/Size data
    private List<TimeSeriesDataPoint> getTimeSeriesSizeData(DateTimeParam startDateParam,EndDateTimeParam endDateParam,String parameter,Boolean byHour,TimeSeriesType type) throws ServiceException{
         try {
            DateTime startDate = startDateParam.getDateTime();
            DateTime endDate = endDateParam.getDateTime();
            String sql;
            if (type==TimeSeriesType.DOMAIN && byHour) sql = String.format("Select year(accessDate) as year0,month(accessDate) as month0,day(accessDate) as day0,hour(accessDate) as hour0, sum(bytes) as value from squidlog where accessDate between '%s' and '%s'  and rfc931 not like '-' and domain like '%%%s%%'  group by year(accessDate),month(accessDate),day(accessDate),hour(accessDate)", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(),parameter);
            else if (type==TimeSeriesType.DOMAIN && !byHour) sql = String.format("Select year(accessDate) as year0,month(accessDate) as month0,day(accessDate) as day0, sum(bytes) as value from squidlog where accessDate between '%s' and '%s'  and rfc931 not like '-' and domain like '%%%s%%'  group by year(accessDate),month(accessDate),day(accessDate)", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(),parameter);
            else if (byHour) sql = String.format("Select year(accessDate) as year0,month(accessDate) as month0,day(accessDate) as day0,hour(accessDate) as hour0, sum(bytes) as value from squidlog where accessDate between '%s' and '%s'  and rfc931 not like '-' and rfc931 like '%%%s%%'  group by year(accessDate),month(accessDate),day(accessDate),hour(accessDate)", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(),parameter);
            else sql = String.format("Select year(accessDate) as year0,month(accessDate) as month0,day(accessDate) as day0, sum(bytes) as value from squidlog where accessDate between '%s' and '%s'  and rfc931 not like '-' and rfc931 like '%%%s%%'  group by year(accessDate),month(accessDate),day(accessDate)", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString(),parameter);

            logger.debug("SQL = "+ sql);
            List<TimeSeriesDataPoint> points =  helper.getResultSet(sql, new TimeSeriesResultSetHandler(parameter,byHour));
            fillMissingDataPoints(points,byHour,startDate,endDate);
            return points;
        } catch (DBException ex) {
            logger.error("there was an error getting domain  stats "+ex.getMessage());
            throw new ServiceException("there was an error getting domain stats "+ex.getMessage(),ex);
        }
    }

    //Add missing datetime value using default local
    private void fillMissingDataPoints(List<TimeSeriesDataPoint> points, Boolean byHour,DateTime startDate, DateTime endDate) {
       if (points==null || points.size()==0) return;
       String name = points.get(0).getName();
       Long start, end, step;

       if (byHour){
          step=3600000L;//milliseconds in hour
       }else{
          step =86400000L;//milliseconds in day
       }
       //Get start date to local time
       startDate = startDate.toDateTime(DateTimeZone.getDefault());
       endDate = endDate.toDateTime(DateTimeZone.getDefault());
       //round down start date
       logger.debug("start Date="+startDate.toString());
       //start = (startDate.getMillis()/step);
       //start*=step;
       //end = (endDate.getMillis()/step);
       //end*=step;
       for (DateTime i =startDate; i.compareTo(endDate)<0;i=i.plus(step)){
       //DateTime date = new DateTime(i,DateTimeZone.getDefault());
       Timestamp tmpTimestamp = new Timestamp(i.getMillis());
       Boolean exists=false;
       for (TimeSeriesDataPoint point : points){
           if (point.getDate().compareTo(tmpTimestamp)==0){
               exists = true;
               break;
            }
       }
       if (!exists){
           TimeSeriesDataPoint point = new TimeSeriesDataPoint();
           point.setDate(tmpTimestamp);
           point.setValue(0);
           point.setName(name);
           points.add(point);
       }
      }
      Collections.sort(points,new Comparator<TimeSeriesDataPoint>(){

            @Override
            public int compare(TimeSeriesDataPoint o1, TimeSeriesDataPoint o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }
}
