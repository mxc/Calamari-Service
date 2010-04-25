/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;
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

        protected File decompressGZ(File file) throws FileNotFoundException, IOException{
            FileInputStream is = new FileInputStream(file);
            GZIPInputStream gz = new GZIPInputStream(is);
            File oFile = File.createTempFile("access-log","txt");
            FileOutputStream os = new FileOutputStream(oFile);
            int data;
            while ((data=gz.read())!=-1){
                os.write(data);
            }
            is.close();
            gz.close();
            os.flush();
            os.close();
            return oFile;
        }

      protected String getCheckSum(String line) throws ServiceException{
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-1");
            algorithm.reset();
            algorithm.update(line.getBytes());
            byte[] digest = algorithm.digest();
            return (new BASE64Encoder()).encode(digest);
        } catch (NoSuchAlgorithmException ex) {
            logger.error("no SHA-1 algorithm ");
            throw new ServiceException("no SHA-1 algorithm ",ex);
        }
    }

}
