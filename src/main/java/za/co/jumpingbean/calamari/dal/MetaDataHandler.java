/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.dal;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mark
 */

/***
 * This abstract class should be used to handle meta data queries. It needs some work. Also see
 * JDBCHelper
 * 
 * @author mark
 */
public abstract class MetaDataHandler {

    private final static Logger logger = LoggerFactory.getLogger(MetaDataHandler.class);

    public void processMetaData(DatabaseMetaData metaData) throws DBException{
        try {
            ResultSet rs = this.handleMetaData(metaData);
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException ex) {
            logger.error("uh oh - error closing metadata result set" + ex.getMessage());
        }
    }

    protected abstract ResultSet handleMetaData(DatabaseMetaData metaData) throws DBException;
}
