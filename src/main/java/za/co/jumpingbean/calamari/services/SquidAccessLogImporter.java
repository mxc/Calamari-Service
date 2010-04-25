/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import sun.misc.BASE64Encoder;
import za.co.jumpingbean.calamari.dal.DBException;
import za.co.jumpingbean.calamari.support.LogFileImporter;

/**
 *
 * @author mark
 */
public class SquidAccessLogImporter extends LogFileImporter{

    @Override
    public void importLog(File logFile) throws ServiceException {
        String sql="";
        try {
            BufferedReader reader;
            CheckedInputStream check;
            logger.debug("importing log file " +logFile.getName());
            if (logFile.getName().indexOf("gz")!=-1){
                logger.info("decompressing log file" + logFile.getName());
                check = new CheckedInputStream(new FileInputStream(decompressGZ(logFile)),new CRC32());
                reader = new BufferedReader(new InputStreamReader(check));
            }else{
                check = new CheckedInputStream(new FileInputStream(logFile),new CRC32());
                reader = new BufferedReader(new InputStreamReader(check));
            }
            String line;
            //Group 1 = Date/Server/Proc Info
            //Group 2 = Date in unix epocj time
            //Group 3 = Elapsed Time
            //Group 4 = Remote IP/Host
            //Group 5 = code/status
            //group 6 = Size in bytes
            //Group 7 = method
            //Group 8 = URL (domain)
            //Group 9 = URL (parameters)
            //Group 10 = rfc931 (User Id or -)
            //Group 11 = peerstatus/peerhost
            //Group 12 = type (application mime type)
            // - 13 - calculate MD5 for line
            final Pattern regex = Pattern.compile("(.*\\[\\d*\\]:) (\\d{10}\\.\\d{3})\\s*(\\d*)\\s*([\\S]*)\\s*(\\S*/\\d{3})\\s*(\\d*)\\s*(\\w*)\\s*(https?://[\\S&&[^/]]*)(/[\\S]*)\\s*([\\w-]*)\\s*(\\S*)\\s*([\\S]*)");
            while ((line = reader.readLine())!=null){
                String hash = this.getCheckSum(line);
                if (checkDuplicate(hash)) {
                    logger.warn("Skipping line, already imported according to sha-1");
                    logger.debug(line);
                    continue;
                }
                Matcher matcher = regex.matcher(line);
                if (matcher.matches()){
                    //StringBuffer buf = new StringBuffer();
                    //Convert from Unix time stamp in seconds with milliseconds as decimal to
                    //Milliseconds needed by Java Timestamp
                    Double dbl = Double.parseDouble(matcher.group(2))*1000;
                    Timestamp timestamp = new Timestamp((dbl.longValue()));
                    String serverInfo = truncateString(matcher.group(1),255);
                    String elapsed = matcher.group(3);
                    String remotehost = truncateString(matcher.group(4),255);
                    String codeStatus = truncateString(matcher.group(5),255);
                    String method = truncateString(matcher.group(7),255);
                    String domain = truncateString(matcher.group(8),355);
                    String user = truncateString(matcher.group(10),255);
                    String peerStatus = truncateString(matcher.group(11),255);
                    String contentType = truncateString(matcher.group(12),255);
                    String parameters = truncateString(matcher.group(9),32700);
                    sql = String.format("Insert into squidlog (serverInfo,accessDate,elapsed,remotehost,codeStatus,bytes,method,domain,parameters,rfc931,peerstatusPeerhost,contentType,checksum) values('%s','%s',%s,'%s','%s',%s,'%s','%s','%s','%s','%s','%s','%s')"
                    ,serverInfo,timestamp.toString(),elapsed,remotehost,
                     codeStatus,matcher.group(6),method,
                     domain,parameters,user,
                     peerStatus,contentType,hash);
                    helper.executeUpdate(sql);
                }
            }
            reader.close();
            Long fileHash=check.getChecksum().getValue();
            sql = String.format("Insert into importfile (fileName,importDate,checksum) values('%s','%s',%d)",logFile.getCanonicalPath(),new Timestamp((new Date()).getTime()),fileHash);
            helper.executeUpdate(sql);
        } catch (DBException ex) {
            logger.error("error executing sql " + sql);
            throw new ServiceException("error executing sql " + sql,ex);
        } catch (FileNotFoundException ex) {
            logger.error(logFile.getAbsolutePath() + " file not found!");
            throw new ServiceException(logFile.getAbsolutePath() + " file not found!",ex);
        } catch (IOException ex) {
            logger.error("Error reading file" + ex.getMessage());
        }

        }



    private boolean checkDuplicate(String hash) throws ServiceException {
        try {
            String sql = String.format("Select id from squidlog where checksum like '%s' fetch first 1 rows only",hash);
            if (helper.getSingleResult(sql)!=null) {
                logger.info("duplicate found - skipping");
                return true;
            }
            else {
                    return false;
            }
        } catch (DBException ex) {
            logger.error("Db exception checking for duplicate" + ex.getMessage());
            throw new ServiceException("Db exception checking for duplicate ",ex);
        }
    }
    }


