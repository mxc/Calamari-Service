/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.dal;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An attempt at a wrapper class to handle all the JDBC exceptions
 *
 * @author mark
 */
public class JDBCHelper {
        private static JDBCHelper helper;
        private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        private final static Logger logger = LoggerFactory.getLogger(JDBCHelper.class);
        public final static String dbName = "Calamari";

        public void updateConfig(String configkey, String configvalue) throws DBException{
                    String sql= String.format("Select configvalue from config where configkey ='%s'", configkey);
                    String oldvalue = (String)this.getSingleResult(sql);
                    if (oldvalue!=null) {
                        sql = String.format("Update config set configvalue='%s' where configkey='%s'", configvalue, configkey);
                    } else {
                        sql = String.format("Insert into config (configkey,configvalue) values('%s','%s')", configkey, configvalue);
                    }
                    helper.executeUpdate(sql);
        }

        public String getConfig(String configkey) throws DBException{
             String sql= String.format("Select configvalue from config where configkey ='%s'", configkey);
             String value = (String)this.getSingleResult(sql);
             if (value==null) return "";
             else return value;
        }


        private enum Type { QUERY,UPDATE };

        private JDBCHelper() throws DBException{
            try{
                 Class.forName(driver).newInstance();
            } catch (ClassNotFoundException ex) {
                logger.error("Derby driver not found " + ex.getMessage());
                throw new DBException("Derby driver not found",ex);
            } catch (InstantiationException ex) {
                logger.error("Could not instantiate Derby driver" + ex.getMessage());
                throw new DBException("Could not instantiate Derby driver",ex);
            } catch (IllegalAccessException ex) {
                logger.error("Error instantiating Derby driver" + ex.getMessage());
                throw new DBException("Error instantiating Derby driver",ex);
            }
        }


        public synchronized static JDBCHelper getJDBCHelper() {
            if(helper==null){
            try {
                helper = new JDBCHelper();
            } catch (DBException ex) {
                logger.error("could not load derby db driver!");
                throw new RuntimeException("Could not load derby db driver");
            }
            }
            return  helper;
        }

        private Connection getConnection() throws DBException {
            try {
                Connection conn = DriverManager.getConnection("jdbc:derby:"+JDBCHelper.dbName+";create=true");
                return conn;
            } catch (SQLException ex) {
                logger.error("Could not create Derby db connection " + ex.getMessage());
                throw new DBException("Could not create Derby db connection",ex);
            }
        }

        public String getSingleResult(String sql) throws DBException{
                return this.processQuery(sql,new ResultSetHandler<String>(){
                            @Override
                            public String handleResultSet(ResultSet rs) throws DBException {
                               try{
                                   if (rs.next()){
                                    return rs.getString(1);
                                }else{
                                    return null;
                                }
                               }catch(SQLException ex){
                                   logger.error("error getting single result "+ ex.getMessage());
                                   throw new DBException ("error getting single result "+ ex.getMessage(),ex);
                               }
                            }
                },Type.QUERY);
        }


        public <E> E getResultSet(String sql,ResultSetHandler<E> handle) throws DBException{
                return this.processQuery(sql, handle,Type.QUERY);
        }

        public Integer executeUpdate(String sql) throws DBException{
            return this.processQuery(sql, new ResultSetHandler<Integer>(){
                //This function should never be called. It is just a place hodler.
                @Override
                public Integer handleResultSet(ResultSet rs) throws DBException {
                   
                    return 0;
                }
            }, Type.UPDATE);
        }

        private <E> E processQuery(String sql,ResultSetHandler<E> handle,Type type) throws DBException{
            Connection conn = this.getConnection();
            E obj = null;
            try{
                try {
                    Statement statement = conn.createStatement();
                    try{
                        ResultSet rs=null;
                        if (type==Type.QUERY){
                            rs = statement.executeQuery(sql);
                        }else{
                            //obj = (E)
                            statement.executeUpdate(sql);
                            //return obj;
                        }
                        try{
                            if (handle!=null) obj =   handle.handleResultSet(rs);
                            return obj;
                        }finally{
                            if (rs!=null) rs.close();
                        }
                    }finally{
                        statement.close();
                    }
                } catch (SQLException ex) {
                    logger.error("error creating db statement! " + ex.getMessage());
                    throw new DBException("error creating db statment" + ex.getMessage(),ex);
                }
                }finally{
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        logger.error("error closing db connect " + ex.getMessage());
                        throw new DBException("error creating db statment " + ex.getMessage(),ex);
                    }
                }
        }

        public void processMetaDataResultSet(MetaDataHandler handle) throws DBException{
            Connection conn = this.getConnection();
            try{
                try {
                    DatabaseMetaData metaData = conn.getMetaData();
                    handle.processMetaData(metaData);
                } catch (SQLException ex) {
                    logger.error("error creating db statement " + ex.getMessage());
                    throw new DBException("error creating db statment " + ex.getMessage(),ex);
                }
                }finally{
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        logger.error("error closing db connect " + ex.getMessage());
                        throw new DBException("error creating db statment " + ex.getMessage(),ex);
                    }
                }
        }

     public void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:Calamari;shutdown=true");
        } catch (SQLException ex) {
            logger.info("Db has been shutdown " + ex.getMessage());
        }
    }
}
