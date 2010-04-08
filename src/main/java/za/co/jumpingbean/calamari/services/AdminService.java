package za.co.jumpingbean.calamari.services;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.jumpingbean.calamari.dal.DBException;
import za.co.jumpingbean.calamari.dal.MetaDataHandler;
import za.co.jumpingbean.calamari.dal.ResultSetHandler;
import za.co.jumpingbean.calamari.dal.JDBCHelper;
import za.co.jumpingbean.calamari.support.Config;
import za.co.jumpingbean.calamari.support.LogFileImporter;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class imports the squid log file in native format.
 * @author mark
 */
@Path("/admin")
public class AdminService {

        private final JDBCHelper helper = JDBCHelper.getJDBCHelper();
        private final Logger logger = LoggerFactory.getLogger(AdminService.class);
        private static final Status importStatus = new Status();


        @GET
        @Path("/initdb")
        @Produces(MediaType.TEXT_PLAIN)
        /**
         *  Initialise the database for use.
         *  @Param dropIfExists - defaults to false when called via web service
         * */
        public String initDatabase(@DefaultValue("false") @QueryParam("dropifexists")final boolean dropIfExists) throws DBException {
                final StringBuffer buf = new StringBuffer();
                if(dropIfExists){
                    logger.info("drop database requested...");
                    helper.shutdown();
                    File file = new File("Calamari");
                    logger.info("file path is "+ file.getAbsolutePath());
                    if (file.exists()) {
                        logger.warn("deleting dabtase as requested ...");
                        delete(file);
                        buf.append("old database deleted.");
                    }else{
                        logger.warn("no old database found to drop ...");
                        buf.append("No old database found to drop.");
                    }
                }
                helper.processMetaDataResultSet(new MetaDataHandler(){
                @Override
                protected ResultSet handleMetaData(final DatabaseMetaData metaData) throws DBException{
                    try {
                        boolean initialised = false;
                        //See if the datbase has been initialised
                        final ResultSet resultSet = metaData.getTables(JDBCHelper.dbName, null, "SQUIDLOG", null);
                        while (resultSet.next() && !initialised) {
                            logger.debug("iterating over metadata result set");
                            final String column = resultSet.getString("TABLE_NAME");
                            if (column != null && column.equalsIgnoreCase("squidlog")) {
                                logger.debug("found matching table 'squidlog'");
                                initialised = true;
                            }
                        }
                        //If not then create derby db
                        if (!initialised) {
                            logger.debug("table 'squidlog' not found, creating now ...");
                            helper.executeUpdate(
                                    "create table squidlog (id integer primary key generated always as identity,"
                                    +"serverInfo varchar(255),"
                                    + "accessDate timestamp, elapsed int, remotehost "
                                    + "varchar(255), codeStatus varchar(255), "
                                    + "bytes int, method varchar(255), domain  varchar(355), "
                                    + "parameters long varchar, rfc931 varchar(255), "
                                    + "peerstatusPeerhost varchar(255),contentType varchar(255))");
                            //logger.debug("number of tables created " + updated);
                            helper.executeUpdate("create index webdomain on squidlog (domain)");
                            helper.executeUpdate("create index proxyUserDate on squidlog (rfc931,accessDate)");
                            helper.executeUpdate("create table config (id integer primary key generated always as identity,"
                                                +"configkey varchar(255)," +
                                                "configvalue varchar(255))");
                            buf.append(" new database initialised.");
                        }else {
                            buf.append(" existing database found, taking no action.");
                        }
                        return null;
                    } catch (SQLException ex) {
                        logger.error("error retrieve tables metadata from derby db" + ex.getMessage());
                        throw new DBException("error retrieve tables metadata from derby db",ex);
                    }
                }
                });
                return buf.toString();
        }

        private void delete(File file) throws DBException{
            if (file.isDirectory()){
                File[] files= file.listFiles();
                for (File sub : files){
                    delete(sub);
                }
            }
            if (!file.delete()) {
                logger.error("could not delte file " + file.getName());
                throw new DBException("could not drop database",null);
            }
        }

        public void shutdownDB()  {
            helper.shutdown();
        }
        

        @POST
        @Path("/settings/squidlogfolder")
        @Consumes(MediaType.TEXT_PLAIN)
        @Produces(MediaType.TEXT_PLAIN)
        public String setLogFolder(@QueryParam("path") String newFolder) throws ServiceException{
            try {
                helper.updateConfig(Config.LOGFOLDER.getName(),newFolder);
                return "config updated";
            } catch (DBException ex) {
                logger.error("error updating/inserting log folder into config table" + ex.getMessage());
                throw new ServiceException("error updating/inserting log folder into config table" + ex.getMessage(),ex);
            }
        }

        @GET
        @Path("/settings/squidlogfolder")
        @Produces(MediaType.TEXT_PLAIN)
        public String getLogFolder() throws ServiceException{
        try {
            String sql = String.format("Select configvalue from config where configkey ='%s'",Config.LOGFOLDER.getName());
            return helper.getResultSet(sql, new ResultSetHandler<String>() {
                @Override
                public String handleResultSet(ResultSet rs) throws DBException {
                    try {
                        if (rs.next()) {
                            return rs.getString("configvalue");
                        }else {
                            logger.warn("logFolder appears not to have been set yet");
                            return "";
                        }
                    } catch (SQLException ex) {
                       logger.info("error getting log folder from config table " + ex.getMessage());
                       return "";
                    }
                }
            });
        } catch (DBException ex) {
                logger.error("error getting log folder from config table " + ex.getMessage());
                throw new ServiceException("error getting log folder from config table " + ex.getMessage(),ex);
        }
        }

        @GET
        @Path("/importlogfiles")
        @Produces(MediaType.TEXT_PLAIN)
        public String importSquidLogFiles() throws ServiceException{
            return importLogFiles(new SquidAccessLogImporter(),Config.LOGFOLDER.getName());
        }



        private String importLogFiles(final LogFileImporter importer, String configKey) throws ServiceException{
            try {
                String folder = helper.getConfig(configKey);
                if (folder==null || folder.isEmpty()){
                    logger.warn("log folder variable not configured");
                    throw new ServiceException("Log folder variable has not been set. Please configure.",null);
                }
                //Limit the files to ones that contain the workd access.
                //We are interested in the squid access.log files.
                if (AdminService.importStatus.isImporting()){
                    return "import is currently in progress. please wait before starting another.";
                }
                File logFolder = new File(folder);
                final File list[];
                if (logFolder.isDirectory() && (list = logFolder.listFiles(new FileFilter(){
                @Override
                public boolean accept(File pathname) {
                    logger.debug("info = "+ pathname.getName());
                    if (pathname.getName().equalsIgnoreCase("access.log")) return true;
                    else return false;
                }
                })).length>0){
                      new Runnable(){
                    @Override
                    public void run() {
                        synchronized(AdminService.importStatus){
                            AdminService.importStatus.setImporting(true);
                        }
                        for (File file : list){
                            try {
                                synchronized(AdminService.importStatus){AdminService.importStatus.setCurrentFile(file.getName());};
                               importer.importLog(file);
                            } catch (ServiceException ex) {
                                logger.error("Could not import file "+ file.getAbsolutePath());
                            }
                      }
                      synchronized(AdminService.importStatus){
                            AdminService.importStatus.setImporting(false);
                            AdminService.importStatus.setCurrentFile("");
                      }
                    }
                   }.run();
                }else{
                    logger.warn("there are no access.log files to import in folder " +folder);
                    throw new ServiceException("there are no access.log files to import in folder " +folder,null);
                }
                return "importing log files....";
            } catch (DBException ex) {
                logger.error("database error getting log folder "+ex.getMessage());
                throw new ServiceException("database error getting log folder "+ex.getMessage(),ex);
            }
        }

        @GET
        @Path("/importlogfilesstatus")
        @Produces(MediaType.TEXT_PLAIN)
        public String getImportStatus(){
            String status;
            synchronized(AdminService.importStatus){
                if (AdminService.importStatus.isImporting()){
                    status = String.format("currently importing file " + AdminService.importStatus.getCurrentFile());
                }else{
                    status="no import in progress";
                }
            }
            return status;
        }

//        private void importLog(File logFile) throws ServiceException {
//        String sql="";
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
//            String line;
//            //Group 1 = Date/Server/Proc Info
//            //Group 2 = Date in unix epocj time
//            //Group 3 = Elapsed Time
//            //Group 4 = Remote IP/Host
//            //Group 5 = code/status
//            //group 6 = Size in bytes
//            //Group 7 = method
//            //Group 8 = URL (domain)
//            //Group 9 = URL (parameters)
//            //Group 10 = rfc931 (User Id or -)
//            //Group 11 = peerstatus/peerhost
//            //Group 12 = type (application mime type)
//            final Pattern regex = Pattern.compile("(.*\\[\\d*\\]:) (\\d{10}\\.\\d{3})\\s*(\\d*)\\s*([\\S]*)\\s*(\\S*/\\d{3})\\s*(\\d*)\\s*(\\w*)\\s*(https?://[\\S&&[^/]]*)(/[\\S]*)\\s*([\\w-]*)\\s*(\\S*)\\s*([\\S]*)");
//            while ((line = reader.readLine())!=null){
//                Matcher matcher = regex.matcher(line);
//                if (matcher.matches()){
//                    //StringBuffer buf = new StringBuffer();
//                    //Convert from Unix time stamp in seconds with milliseconds as decimal to
//                    //Milliseconds needed by Java Timestamp
//                    Double dbl = Double.parseDouble(matcher.group(2))*1000;
//                    Timestamp timestamp = new Timestamp((dbl.longValue()));
//                    String serverInfo = truncateString(matcher.group(1),255);
//                    String elapsed = matcher.group(3);
//                    String remotehost = truncateString(matcher.group(4),255);
//                    String codeStatus = truncateString(matcher.group(5),255);
//                    String method = truncateString(matcher.group(7),255);
//                    String domain = truncateString(matcher.group(8),355);
//                    String user = truncateString(matcher.group(10),255);
//                    String peerStatus = truncateString(matcher.group(11),255);
//                    String contentType = truncateString(matcher.group(12),255);
//                    String parameters = truncateString(matcher.group(9),32700);
//                    sql = String.format("Insert into squidlog (serverInfo,accessDate,elapsed,remotehost,codeStatus,bytes,method,domain,parameters,rfc931,peerstatusPeerhost,contentType) values('%s','%s',%s,'%s','%s',%s,'%s','%s','%s','%s','%s','%s')"
//                    ,serverInfo,timestamp.toString(),elapsed,remotehost,
//                     codeStatus,matcher.group(6),method,
//                     domain,parameters,user,
//                     peerStatus,contentType);
//                    helper.executeUpdate(sql);
//                }
//            }
//            reader.close();
//        } catch (DBException ex) {
//            logger.error("error executing sql " + sql);
//            throw new ServiceException("error executing sql " + sql,ex);
//        } catch (FileNotFoundException ex) {
//            logger.error(logFile.getAbsolutePath() + " file not found!");
//            throw new ServiceException(logFile.getAbsolutePath() + " file not found!",ex);
//        } catch (IOException ex) {
//            logger.error("Error reading file" + ex.getMessage());
//        }
//
//        }



}


  class Status{
            private String currentFile;
            private boolean importing;

    /**
     * @return the currentFile
     */
    public String getCurrentFile() {
        return currentFile;
    }

    /**
     * @param currentFile the currentFile to set
     */
    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * @return the importing
     */
    public boolean isImporting() {
        return importing;
    }

    /**
     * @param importing the importing to set
     */
    public void setImporting(boolean importing) {
        this.importing = importing;
    }

        }