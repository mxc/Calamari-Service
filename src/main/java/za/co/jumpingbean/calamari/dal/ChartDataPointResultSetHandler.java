/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.jumpingbean.calamari.model.ChartDataPoint;

/**
 *
 * @author mark
 */
public class ChartDataPointResultSetHandler implements ResultSetHandler<List<ChartDataPoint>> {

    String valueField;

    public ChartDataPointResultSetHandler(String valueField){
        this.valueField=valueField;
    }

    private final Logger logger = LoggerFactory.getLogger(ChartDataPointResultSetHandler.class);

    @Override
    public List<ChartDataPoint> handleResultSet(ResultSet rs) throws DBException {
        try {
            List<ChartDataPoint> data = new ArrayList<ChartDataPoint>();
            while (rs.next()) {
                ChartDataPoint point = new ChartDataPoint();
                point.setBytes(rs.getInt("size"));
                point.setHits(rs.getInt("hits"));
                point.setName(rs.getString(valueField));
                data.add(point);
            }
            return data;
        } catch (SQLException ex) {
           logger.error("there was an error building RecoChartDataPoint result set "+ex.getMessage());
           throw new DBException("there was an error building RecoChartDataPoint result set ",ex);
        }
    }
}
