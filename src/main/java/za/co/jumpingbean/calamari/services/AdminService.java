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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.jumpingbean.calamari.dal.DBException;
import za.co.jumpingbean.calamari.dal.ImportFileResultHandler;
import za.co.jumpingbean.calamari.dal.MetaDataHandler;
import za.co.jumpingbean.calamari.dal.ResultSetHandler;
import za.co.jumpingbean.calamari.dal.JDBCHelper;
import za.co.jumpingbean.calamari.dal.RecordDetailResultSetHandler;
import za.co.jumpingbean.calamari.model.ImportFile;
import za.co.jumpingbean.calamari.support.Config;
import za.co.jumpingbean.calamari.support.EndDateTimeParam;
import za.co.jumpingbean.calamari.support.LogFileImporter;
import za.co.jumpingbean.calamari.support.DateTimeParam;


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
        private Thread importThread=null;


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
                    File file = new File("/var/lib/database/Calamari");
                    logger.info("file path is "+ file.getAbsolutePath());
                    if (file.exists()) {
                        logger.warn("deleting database as requested ...");
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
                        boolean initialised = false;
                        try {
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
                        } catch (SQLException ex) {
                            logger.error("error retrieve tables metadata from derby db" + ex.getMessage());
                            throw new DBException("error retrieve tables metadata from derby db",ex);
                        }catch(Exception ex){
                            //db does not exist!
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
                                    + "peerstatusPeerhost varchar(255),contentType varchar(255),"
                                    + "checksum varchar(255))"
                                    );
                            //logger.debug("number of tables created " + updated);
                            helper.executeUpdate("create index webdomain on squidlog (domain)");
                            helper.executeUpdate("create index checksumIdx on squidlog (checksum)");
                            helper.executeUpdate("create index proxyUserDate on squidlog (rfc931,accessDate)");
                            helper.executeUpdate("create table config (id integer primary key generated always as identity,"
                                                +"configkey varchar(255)," +
                                                "configvalue varchar(255))");
                            helper.executeUpdate("create table importFile (id integer primary key generated always as identity,"
                                                +"fileName varchar(255)," +
                                                "importDate timestamp, checksum bigint)");
                            helper.updateConfig(Config.LOGFOLDER.getName(),"/var/log/squid");//set default log file location
                            buf.append(" new database initialised.");
                        }else {
                            buf.append(" existing database found, taking no action.");
                        }
                        return null;
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
                //Limit the files to ones that contain the word access.
                //We are interested in the squid access.log files.
                synchronized(AdminService.importStatus){
                    if (AdminService.importStatus.isImporting()){
                    return "import is currently in progress. please wait before starting another.";
                }
                }
                File logFolder = new File(folder);
                final File list[];
                //try{
                if (logFolder.isDirectory() && (list = logFolder.listFiles(new FileFilter(){
                @Override
                public boolean accept(File pathname) {
                    if (pathname.getName().indexOf("access.log")!=-1) {
                        logger.debug("file "+pathname.getName()+" accepted for processing...");
                        return true;
                    }
                    else return false;
                }
                })).length>0){
                    importThread=(new Thread(){
                    @Override
                    public void run() {
                        synchronized(AdminService.importStatus){
                            AdminService.importStatus.setImporting(true);
                        }
                        for (File file : list){
                            try {
                                synchronized(AdminService.importStatus){
                                    AdminService.importStatus.setCurrentFile(file.getName());
                                };
                               importer.importLog(file);
                            } catch (ServiceException ex) {
                                logger.error("Could not import file "+ file.getAbsolutePath());
                                AdminService.importStatus.setCurrentFile("File Not imported! Check file read permisions!");
                            }
                      }
                      synchronized(AdminService.importStatus){
                            AdminService.importStatus.setImporting(false);
                            AdminService.importStatus.setCurrentFile("");
                      }
                    }
                   });
                   importThread.start();
                }else{
                    logger.warn("there are no access.log files to import in folder " +folder);
                    throw new ServiceException("there are no access.log files to import in folder " +folder,null);
                }
                return "importing log files....";
            } catch (DBException ex) {
                logger.error("database error getting log folder "+ex.getMessage());
                throw new ServiceException("database error getting log folder "+ex.getMessage(),ex);
            }catch (NullPointerException ex) {
                logger.error("your web server does not have read permissions on log squid folder "+ex.getMessage());
                throw new ServiceException("your web server does not have read permissions on squid log folder "+ex.getMessage(),ex);
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

        @GET
        @Path("/importhistory/{startDate: [0-9]{8}}/{endDate: [0-9]{8}}/")
        @Produces(MediaType.TEXT_XML)
        public List<ImportFile> getImportHistory(@PathParam("startDate")DateTimeParam startDateParam,@PathParam("endDate")EndDateTimeParam endDateParam) throws ServiceException{
           try{
                DateTime startDate = startDateParam.getDateTime();
                DateTime endDate = endDateParam.getDateTime();
                String sql = String.format("Select fileName,importDate,checksum from importFile where importDate between '%s' and '%s' order by  importDate desc", new Timestamp(startDate.getMillis()).toString(), new Timestamp(endDate.getMillis()).toString());
                return helper.getResultSet(sql, new ImportFileResultHandler());
            } catch (DBException ex) {
                logger.error("there was an error getting import file history "+ex.getMessage());
                throw new ServiceException("there was an error getting import file history "+ex.getMessage(),ex);
            }
        }

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