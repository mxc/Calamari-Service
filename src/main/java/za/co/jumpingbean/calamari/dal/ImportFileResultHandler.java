/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import za.co.jumpingbean.calamari.model.ImportFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mark
 */
public class ImportFileResultHandler implements ResultSetHandler<List<ImportFile>>{

    private final Logger logger = LoggerFactory.getLogger(RecordDetailResultSetHandler.class);

    @Override
    public List<ImportFile> handleResultSet(ResultSet rs) throws DBException {
        try {
            List<ImportFile> data = new ArrayList<ImportFile>();
            while (rs.next()) {
                ImportFile file = new ImportFile();
                file.setChecksum(rs.getLong(3));
                file.setFileName(rs.getString(1));
                file.setImportDate(rs.getTimestamp(2));
                data.add(file);
            }
            return data;
        } catch (SQLException ex) {
           logger.error("there was an error building RecordDetail result set "+ex.getMessage());
           throw new DBException("there was an error building RecordDetail result set ",ex);
        }
    }

}
