/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.jumpingbean.calamari.model.TimeSeriesDataPoint;

/**
 *
 * @author mark
 */
public class TimeSeriesResultSetHandler implements ResultSetHandler<List<TimeSeriesDataPoint>>{
    private String valueField;
    private Boolean byHour;

    public TimeSeriesResultSetHandler(String valueField,Boolean byHour){
        this.valueField=valueField;
        this.byHour=byHour;
    }

    private final Logger logger = LoggerFactory.getLogger(ChartDataPointResultSetHandler.class);

    /**
     * Return timestamp
     * 
     * @param rs
     * @return
     * @throws DBException
     */
    @Override
    public List<TimeSeriesDataPoint> handleResultSet(ResultSet rs) throws DBException {
        try {
            List<TimeSeriesDataPoint> data = new ArrayList<TimeSeriesDataPoint>();
            while (rs.next()) {
                TimeSeriesDataPoint point = new TimeSeriesDataPoint();
                Timestamp timestamp;
                if (byHour){
                    DateTime date = new DateTime(rs.getInt("year0"),rs.getInt("month0"),rs.getInt("day0"),rs.getInt("hour0"),0,0,0);
                    timestamp= new Timestamp(date.getMillis());
                }else{
                    DateTime date = new DateTime(rs.getInt("year0"),rs.getInt("month0"),rs.getInt("day0"),0,0,0,0);
                    timestamp= new Timestamp(date.getMillis());
                }
                point.setDate(timestamp);
                point.setValue(rs.getInt("value"));
                point.setName(valueField);
                data.add(point);
            }
            return data;
        } catch (SQLException ex) {
           logger.error("there was an error building Timeseries data point result set "+ex.getMessage());
           throw new DBException("there was an error building Timeseries data point ",ex);
        }
    }
}
