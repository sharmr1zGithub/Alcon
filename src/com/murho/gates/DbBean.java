package com.murho.gates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import com.murho.utils.MLogger;
//import javax.transaction.TransactionManager;

/********************************************************************************************************
 *  PURPOSE :  The main  class for establishing Database Connection
 *   using paramters from the c:/props/seiko/config/seiko.properties file
 *   from the pool of Connections from the class ConnectionPool
 *******************************************************************************************************/

public class DbBean {

  public static String DATA_SOURCE, MY_CONTEXT_FACTORY, MY_CONTEXT_URL,ODBC_DRIVER,ODBC_URL,ODBC_URL_WMS;
  public static String BEAN_ERROR_LOG_FILE, BEAN_ERR_LOG_DESC_FILE,URL_PKG_PREFIXES,APP_SERVER;
  public static String JASPER_INPUT,  JASPER_OUTPUT, JASPER_XML, JASPER_DOWNLOAD;
  public static String WHID_SO,MAIN_WHS, PROD_WHS;
  public static String THREAD_SLEEPING;
  public static String USERNAME,PASSWORD,USERNAME_WMS,PASSWORD_WMS;
  public static String USERTRANSACTION;
  public static String SO_SYNC,DO_SYNC_PICKLIST,DO_SYNC_CONFIRM,STKTAKE_NUMOFREC,PALLET_PRINTER_NAME,SHIPMARK_PRINTER_NAME ;
  public static Connection connection;
  public static String DB_PROPS_FILE ="C:/props/CibaVision/config/CibaVision.properties";
 // public static String DB_PROPS_FILE ="C:/props/CibaVision/config/CibaVision_testEnv.properties";
 // public static String DB_PROPS_FILE ="C:/props/CibaVision/config/CibaVision_archiveEnv.properties";
  public static Boolean refBoolean;

  static {
  //System.out.println("###### DbBean::Connecting to testing environment ... #####");
    Properties dbpr;
    InputStream dbip;

    try {
      dbip = new FileInputStream(new File(DB_PROPS_FILE));
      dbpr = new Properties();
      dbpr.load(dbip);
      DATA_SOURCE        = dbpr.getProperty("DATA_SOURCE"); //  Database Driver
      MY_CONTEXT_FACTORY = dbpr.getProperty("CONTEXT_FACTORY");
      MY_CONTEXT_URL     = dbpr.getProperty("CONTEXT_URL");
      ODBC_URL           = dbpr.getProperty("ODBC_URL");
      ODBC_DRIVER        = dbpr.getProperty("ODBC_DRIVER");
      URL_PKG_PREFIXES   = dbpr.getProperty("URL_PKG_PREFIXES");
      APP_SERVER         = dbpr.getProperty("APP_SERVER");
      JASPER_OUTPUT      = dbpr.getProperty("JASPER_OUTPUT");
      JASPER_INPUT       = dbpr.getProperty("JASPER_INPUT");
      JASPER_XML         = dbpr.getProperty("JASPER_XML");
      USERTRANSACTION     = dbpr.getProperty("USERTRANACTION");
      USERNAME           = dbpr.getProperty("USERNAME");
      PASSWORD           = dbpr.getProperty("PASSWORD");
      ODBC_URL_WMS       = dbpr.getProperty("ODBC_URL_WMS");
      THREAD_SLEEPING    = dbpr.getProperty("THREAD_SLEEPING");
      USERNAME_WMS       = dbpr.getProperty("USERNAME_WMS");
      PASSWORD_WMS       = dbpr.getProperty("PASSWORD_WMS");
      JASPER_DOWNLOAD    = dbpr.getProperty("JASPER_DOWNLOAD");
      MAIN_WHS           = dbpr.getProperty("MAIN_WHS");
      PROD_WHS           = dbpr.getProperty("PROD_WHS");
      SO_SYNC            = dbpr.getProperty("SO_SYNC");
      DO_SYNC_PICKLIST   = dbpr.getProperty("DO_SYNC_PICKLIST");
      DO_SYNC_CONFIRM    = dbpr.getProperty("DO_SYNC_CONFIRM");
      STKTAKE_NUMOFREC  = dbpr.getProperty("STKTAKE_NUMOFREC");
      PALLET_PRINTER_NAME  = dbpr.getProperty("PALLET_PRINTER_NAME");
      SHIPMARK_PRINTER_NAME=dbpr.getProperty("SHIPMARK_PRINTER_NAME");

      refBoolean = new Boolean((String)dbpr.getProperty("DEBUG"));


      WHID_SO    = dbpr.getProperty("WHID_SO");
      log(" ####################### INITIALISATION ####################### ");
      log("Data Source :" + DATA_SOURCE + "\n\n" + "Factory : " +
          MY_CONTEXT_FACTORY);
      log(" ####################### INITIALISATION ####################### ");

    }
    catch (FileNotFoundException fnfe) {

      log("#### Could not find the db_param file..");
      writeError("DbBean", "static()", fnfe);
    }
    catch (Exception e) {
      log(e.toString());
      writeError("DbBean", "static()", e);
    }
  }

   public static final boolean DEBUG = refBoolean.booleanValue();

   static int conCount=0;


  /********************************************************************************************************
   *    PURPOSE          :  Method for gettting Database Connection from the Connection Pool
   *   PARAMETER 1 :  Nil
   *   RETURNS          : Connection Object
   *******************************************************************************************************/
  public static synchronized Connection getConnection() throws NamingException, SQLException {
    MLogger.info("Opening connection : " + (conCount+1));
    conCount++;
    Context ctx = getInitialContext();
    DataSource dataSource = (DataSource) ctx.lookup(DATA_SOURCE);
    connection = dataSource.getConnection();
    
    MLogger.info("Total connection opened : " + conCount);
    
    return connection;

  }

  /********************************************************************************************************
   *   PURPOSE          :  Method for gettting Database Connection from the Connection Pool
   *   PARAMETER 1 :  Nil
   *   RETURNS          : Connection Object
   *******************************************************************************************************/
/*
  public static synchronized Connection getODBCConnection() throws NamingException, SQLException {
      //  log("  calling getODBCConnection() ");
        Connection con  = null;
     //   log("DbBean :: getODBCConnection(Driver : "+ ODBC_DRIVER+" url : "+ODBC_URL+" User name : "+USERNAME + " PASSWORD : " + PASSWORD);
        try{
           Class.forName(ODBC_DRIVER);
           con = DriverManager.getConnection(ODBC_URL,USERNAME,PASSWORD);

    //       log("after call for name "+con.toString());
       }catch(Exception e){
              log(" ************  getODBCConnection :: Exception :" + e.toString());
       }
   return con;
   }
  */
  
    public static synchronized Connection getODBCConnection() throws NamingException, SQLException, Exception  {
    log("  calling getODBCConnection() Enter ");
    try
    {
      //create dsn useing name 'MAX_SEIKO_DSN'
      //set the dsn in progress-ds.xml
      
      Context ctx = getInitialContext();
      DataSource dataSource = (DataSource) ctx.lookup("java:/ProgressDS");
      connection = dataSource.getConnection();
      
      
      
    }
    catch (Exception e)
    {
      // log(" Exception : " + e.getMessage());
      throw e;
    }
     log("  calling getODBCConnection() Exit ");
    return connection;

  }
 
   public static synchronized Connection getODBCConnection4BPCS_MASTERS() throws NamingException,
       SQLException {
     Connection con = null;
     try{
       Class.forName(ODBC_DRIVER);
       //changes by rani 08-03-2004.
       //con = DriverManager.getConnection(ODBC_URL_WMS,USERNAME_WMS,PASSWORD_WMS);
       con = DriverManager.getConnection(ODBC_URL_WMS,USERNAME,PASSWORD);
     }catch(Exception e){System.out.println("getODBCConnection4BPCS_MASTERS :: Exception :" + e.toString()); }
    return con;
    }

  /** *****************************************************************************************************
   *   PURPOSE   :  Method for closing a open Database Connection
                          That is, returning to the pool of connections
   *   PARAMETER  : Connection to be freed
   *   RETURNS    : void
   *******************************************************************************************************/
  public static synchronized void closeConnection(Connection connection, PreparedStatement statement) {
  MLogger.info("Closing connection and Prepared Statement : " + conCount); 
   try {
    if (statement != null) {
      statement.close();
    }
  }
  catch(SQLException e) {
    log(e.toString());
    writeError("DbBean", "closeConnection()",
               "Error closing connection in Statement Pool : " + e);

  }
  try {
    if (connection != null) {
      connection.close();
    }
  }
  catch(SQLException e) {
    log(e.toString());
          writeError("DbBean", "closeConnection()",
           "Error closing connection in Connection Pool : " + e);

  }
  
   conCount--;
   MLogger.info("Total Connection available : " + conCount);
  
  
}

  public static synchronized void closeConnection(Connection connection, CallableStatement statement) {
 // MLogger.info("Closing connection and Callable Statement : " + conCount);
  try {
      if (statement != null) {  statement.close();    }
   } catch(SQLException e) {
     log(e.toString());
     writeError("DbBean", "closeConnection()",
             "Error closing connection in Statement Pool : " + e);
  }
  try {
      if (connection != null) {  connection.close();  }
  }catch(SQLException e) {
     log(e.toString());
        writeError("DbBean", "closeConnection()",
         "Error closing connection in Connection Pool : " + e);
  }
   conCount--;
 //  MLogger.info("Total Connection available : " + conCount);
 }

  /*public static synchronized void closeConnection(Connection connection,
                                                  PreparedStatement ps) {
    try {
      connection.close();
    }
    catch (Exception e) {
      log(e.toString());
      writeError("DbBean", "closeConnection()",
                 "Error closing connection in Connection Pool : " + e);
    }
  }*/
  /** *****************************************************************************************************
    *   PURPOSE   :  Method for closing a open Database Connection
                           That is, returning to the pool of connections
    *   PARAMETER  : Connection to be freed
    *   RETURNS    : void
    *******************************************************************************************************/
    public static synchronized void closeODBCConnection(Connection connection, PreparedStatement statement) {
     try {
       if (statement != null) {
         statement.close();
       }
     }
     catch(SQLException e) {
       log(e.toString());
           writeError("DbBean", "closeConnection()",
           "Error closing connection in Connection Pool : " + e);

     }
     try {
       if (connection != null) {
         connection.close();
       }
     }
     catch(SQLException e) {
       log(e.toString());
            writeError("DbBean", "closeConnection()",
           "Error closing connection in Connection Pool : " + e);

     }
   }

  public static synchronized void closeConnection(Connection connection) {

  MLogger.info("Closing connection : " + conCount);
    try {
      if (connection != null) 
      connection.close();
    }
    catch (Exception e) {
      log(e.toString());
      writeError("DbBean", "closeConnection()",
                 "Error closing connection in Connection Pool : " + e);
    }
    conCount--;
  MLogger.info("Total Connection available : " + conCount);
  }
  public static synchronized void closeODBCConnection(Connection connection) {
      try {
        if (connection != null) connection.close();
      }
      catch (Exception e) {
        log(e.toString());
        writeError("DbBean", "closeConnection()",
                   "Error closing connection in Connection Pool : " + e);
      }
    }

  /** *****************************************************************************************************
   *    PURPOSE     :  Method for writing errors to error log - with Exception Object
   *   PARAMETER 1 :  Name of the Bean where the error occured
   *   PARAMETER 2 :  Name of the Method where the error occured
   *   PARAMETER 3 :  The Exception object caught
   *   RETURNS     : void
   *******************************************************************************************************/
  public static void writeError(String beanName, String methodName, Exception e) {

//    Class c = e.getClass();
//    String error;
//    if (c.getSuperclass().getName() == "java.sql.SQLException") {
//      SQLException sq = (SQLException) e;
//      error = "SQLSTATE = " + sq.getSQLState() + " ERROR-CODE = " +
//          sq.getErrorCode() + " " + sq.toString();
//    }
//    else {
//      error = e.toString();
//    }
//    try {
//      e.printStackTrace(new PrintStream(new FileOutputStream(
//          BEAN_ERR_LOG_DESC_FILE, true)));
//    }
//    catch (Exception es) {
//      log("@@@@ Could not write to " + BEAN_ERR_LOG_DESC_FILE);
//    }
//
//    writeError(beanName, methodName, error);
  }

  /********************************************************************************************************
   *   PURPOSE     :  Method for writing errors to error log - all String
   *   PARAMETER 1 :  Name of the Bean where the error occured
   *   PARAMETER 2 :  Name of the Method where the error occured
   *   PARAMETER 3 :  The description of Exception caught
   *   RETURNS     : void
   *******************************************************************************************************/
  public static void writeError(String beanName, String methodName,
                                String error) {

//    String time = "";
//    Date dt = new java.util.Date();
//    Calendar calendar = new GregorianCalendar();
//    calendar.setTime(dt);
//    SimpleDateFormat formatter2 = new SimpleDateFormat(
//        "dd/MM/yyyy 'at' HH:mm:ss");
//    time = formatter2.format(dt);
//
//    StringReader sr = new StringReader("\n[Time] : " + time + " [Origin] : " +
//                                       beanName + " [Method] : " + methodName +
//                                       " [Error] : " + error);
//    FileOutputStream fo;
//    int n;
//    try {
//      fo = new FileOutputStream(BEAN_ERROR_LOG_FILE, true);
//      fo.write(13); // next line
//      while ( (n = sr.read()) != -1) {
//        fo.write(n);
//      }
//
//      System.out.println();
//      fo.flush();
//      fo.close();
//      Runtime.getRuntime().gc(); //  Invoking the garbage collecting thread
//    }
//    catch (FileNotFoundException fnfe) {
//      log("Could not find/open the BeanError.log file..");
//    }
//    catch (Exception e) {
//      log("Unable to write error to Error file .. ");
//      log(e.toString());
//    }
  }

  /************************************************************************
   * PURPOSE  : TO GET THE INITIAL CONTEXT
   * PARAMS   :
   * RETURNS  : CONTEXT
   ************************************************************************/

  public static Context getInitialContext() throws NamingException {
    Properties props = new Properties();
    props.put(Context.INITIAL_CONTEXT_FACTORY,
              MY_CONTEXT_FACTORY);
    props.put(Context.PROVIDER_URL, MY_CONTEXT_URL);
    if(APP_SERVER.equalsIgnoreCase("JBOSS")){
     props.put(Context.URL_PKG_PREFIXES,URL_PKG_PREFIXES);
   }
    return new InitialContext(props);
  }
  /**
   * @method : getUserTranaction()
   * @description : Look up the User Transaction
   * @return UserTransaction
   * @throws NamingException
   */
  public static UserTransaction getUserTranaction() throws NamingException{
    UserTransaction userTrans = null;
    Context ctx = DbBean.getInitialContext();
    userTrans = (UserTransaction)ctx.lookup(USERTRANSACTION);
    return userTrans;
  }
  public static UserTransaction getBPCSUserTranaction() throws NamingException{
    UserTransaction userTrans = null;
    Context ctx = DbBean.getInitialContext();
    userTrans = (UserTransaction)ctx.lookup(USERTRANSACTION);
    return userTrans;
  }
  
   public static boolean CommitTran(UserTransaction ut) throws Exception{
    boolean flag=false;
    try
    {
      MLogger.log(0, "DbBean : Transaction CommitTran : Start");
      ut.commit();
      flag=true;
      MLogger.log(0, "DbBean : Transaction CommitTran : Ends");
    }
    catch (Exception e)
    {
      flag=false;
      MLogger.exception(" DbBean :: CommitTran() :: ",e);
//    Code added by Arun on 15 June 2011 for #1851 change - to throw exception 
      throw e;
    }
    return flag;
  }
  
   public static boolean RollbackTran(UserTransaction ut){
    boolean flag=false;
    try
    {
      MLogger.log(0, "DbBean : Transaction rollback : Start");
      ut.rollback();
      flag=true;
      MLogger.log(0, "DbBean : Transaction rollback : Ends");
    }
    catch (Exception e)
    {
      flag=false;
      MLogger.exception(" DbBean :: RollbackTran() :: ",e);
    }
    return flag;
  }
  
  private static void log(String s) {
    if (DEBUG) {
      System.out.println("DB Bean : " + s);
    }
  }
}
