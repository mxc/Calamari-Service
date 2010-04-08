/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.support;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.jumpingbean.calamari.dal.JDBCHelper;
import za.co.jumpingbean.calamari.services.ServiceException;

/**
 *
 * @author mark
 */
public abstract class LogFileImporter {

        protected final JDBCHelper helper = JDBCHelper.getJDBCHelper();
        protected final Logger logger = LoggerFactory.getLogger(LogFileImporter.class);

        protected String truncateString(String value, int maxLength){
            return value.substring(0,value.length()>maxLength?maxLength:value.length());
        }
        public abstract void importLog(File logFile) throws ServiceException;
}
