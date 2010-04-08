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
import za.co.jumpingbean.calamari.model.SquidLogRecord;

/**
 *
 * @author mark
 */
public class RecordDetailResultSetHandler implements ResultSetHandler<List<SquidLogRecord>> {

    private final Logger logger = LoggerFactory.getLogger(RecordDetailResultSetHandler.class);

    @Override
    public List<SquidLogRecord> handleResultSet(ResultSet rs) throws DBException {
        try {
            List<SquidLogRecord> data = new ArrayList<SquidLogRecord>();
            while (rs.next()) {
                SquidLogRecord point = new SquidLogRecord();
                point.setBytes(rs.getInt("bytes"));
                point.setAccessDate(rs.getTimestamp("accessDate"));
                point.setElapsed(rs.getInt("elapsed"));
                point.setCodeStatus(rs.getString("codestatus"));
                point.setContentType(rs.getString("contenttype"));
                point.setMethod(rs.getString("method"));
                point.setParameters(rs.getString("parameters"));
                point.setPeerStatusPeerHost(rs.getString("peerstatuspeerhost"));
                point.setRemoteHost(rs.getString("remotehost"));
                point.setRfc931(rs.getString("rfc931"));
                point.setServerInfo(rs.getString("serverinfo"));
                point.setDomain(rs.getString("domain"));
                data.add(point);
            }
            return data;
        } catch (SQLException ex) {
           logger.error("there was an error building RecordDetail result set "+ex.getMessage());
           throw new DBException("there was an error building RecordDetail result set ",ex);
        }
    }

}
