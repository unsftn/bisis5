package bisis5.gui;

import bisis5.auth.User;
import bisis5.gui.login.LoginFrame;
import bisis5.util.NetUtils;
import bisis5.gui.util.SplashScreen;
import bisis5.webclient.UsersClient;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class BisisApp {

  public static final String VERSION = "5.0";
  
  public static void main(String[] args) {
    if (args.length > 0) {
      System.out.println("Radim u standalone modu...");
      standalone = true;
    }
    UIManager.put("swing.boldMetal", Boolean.FALSE);
    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    JDialog.setDefaultLookAndFeelDecorated(true);

    splash = new SplashScreen();
    splash.setImage("/images/bisis-splash.png");
    splash.setVisible(true);
    splash.getMessage().setText("Pokre\u0107em menad\u017eer zapisa");
    String mac = NetUtils.getMACAddress();
    //log.info("Procitana MAC adresa: " + mac);

    /*
    iniFile = new INIFile(BisisApp.class.getResource("/client-config.ini"));
    standalone = iniFile.getBoolean("textsrv", "standalone");
    if (standalone) {
      try {
        Class.forName(iniFile.getString("database", "driver"));
        GenericObjectPool connectionPool = new GenericObjectPool(null);
        DriverManagerConnectionFactory connectionFactory = 
          new DriverManagerConnectionFactory(
              iniFile.getString("database", "url"), 
              iniFile.getString("database", "username"), 
              iniFile.getString("database", "password"));
        PoolableConnectionFactory poolableConnectionFactory =
          new PoolableConnectionFactory(connectionFactory, connectionPool, null, 
              null, false, false);
        PoolingDataSource poolingDataSource = new PoolingDataSource(connectionPool);
        RecordManagerImpl recMgr1 = new RecordManagerImpl();
        String osname = System.getProperty("os.name");
        if (osname.equals("Linux"))
          recMgr1.setIndexPath("/opt/lucene-index");
        else
          recMgr1.setIndexPath("C:/lucene-index"); 
        recMgr1.setDataSource(poolingDataSource);
        recMgr1.setStandalone(true);
        recMgr = recMgr1;
      } catch (Exception ex) {
        log.error(ex);
      }
    } else {
      try {
        Class.forName(iniFile.getString("database", "driver"));
        //HessianProxyFactory proxyFactory = new HessianProxyFactory();
        BurlapProxyFactory proxyFactory = new BurlapProxyFactory();
        recMgr = (RecordManager)proxyFactory.create(RecordManager.class, 
            iniFile.getString("textsrv", "recmgr"));
      } catch (Exception ex) {
        log.error(ex);
      }
    }
    fileMgrEnabled = getINIFile().getBoolean("filestorage", "enabled");    
    fileMgrURL = getINIFile().getString("filestorage", "filemgr");
    */

    splash.getMessage().setText("Otvaram vezu sa bazom");
//    try {
//      connection = DriverManager.getConnection(
//          iniFile.getString("database", "url"), 
//          iniFile.getString("database", "username"), 
//          iniFile.getString("database", "password"));     
//      connection.setAutoCommit(false);
//    } catch (SQLException e) {
//      log.fatal(e);
//      splash.setVisible(false);
//      e.printStackTrace();
//      JOptionPane.showMessageDialog(null, "Gre\u0161ka pri konekciji sa bazom", "Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
//      System.exit(0);
//    }  
    /*
    ServiceFactory factory = getFactory(CommandType.JDBC);
    String category = "commandsrv";
    if (BisisApp.getINIFile().getCategories().contains(mac)){
    	category = mac;
    	log.info("Pronadjena konfiguracija za MAC adresu: " + mac);
    }else{
    	log.info("Nije pronadjena konfiguracija za MAC adresu: " + mac +". Koristi se default konfiguracija [commandsrv]");
    }
    if (BisisApp.getINIFile().getBoolean(category, "remote")){
    	jdbcservice = factory.createService(ServiceType.REMOTE, BisisApp.getINIFile().getString(category, "service"));
    	log.info("Kreirana instanca za udaljen pristup serveru.");
    } else {
    	jdbcservice = factory.createService(ServiceType.LOCAL, null);
    	log.info("Kreirana instanca za lokalni pristup serveru.");
    }
    
    if (jdbcservice == null){
      splash.setVisible(false);
      JOptionPane.showMessageDialog(null, "Gre\u0161ka pri konekciji sa bazom", "Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
      System.exit(0);
    }
    */

    /*
    splash.getMessage().setText("U\u010ditavam UNIMARC");
    format = PubTypes.getFormat();
    try{
    	HoldingsDataCoders.loadData(jdbcservice);
    } catch (Exception e){
    	splash.setVisible(false);
    	JOptionPane.showMessageDialog(null, e.getMessage(), "Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    splash.getMessage().setText("U\u010ditavam parametre");
    ProcessTypeCatalog.init();    
    */
    splash.getMessage().setText("U\u010ditavam prozore");
/*
    mf = new MainFrame();
    mf.setResizable(true);
    mf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    if (Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH))
      mf.setExtendedState(mf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
*/
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    splash.setVisible(false);
    splash.dispose();
    
    LoginFrame login = new LoginFrame();
    boolean correct = false;
    UsersClient usersClient = new UsersClient();
    while (!correct){
	    if (login.isConfirmed()) {
	      User tryLogin = new User(login.getUsername(), login.getPassword());
        librarian = usersClient.get(tryLogin);
	      if (librarian != null) {
	      	correct = true;
	        login.disp();
	        //mf.setVisible(true);
	      } else {
	        JOptionPane.showMessageDialog(null, "Pogre\u0161no ime/lozinka",
	            "Greska", JOptionPane.ERROR_MESSAGE);
	        login.setVis(true);
	        //System.exit(0);
	      }
	    } else {
	      System.exit(0);
	    }
    
    }
    
    //mf.setJMenuBar(new MenuBuilder(librarian));
    //mf.initialize(librarian);
  }

  /*
  public static MainFrame getMainFrame() {
    return mf;
  }

  public static RecordManager getRecordManager() {
    return recMgr;
  }
  */
  
//  public static Connection getConnection() {
//    return connection;
//  }
  
  
  // postoji zbog bekapa
  //TODO napraviti bekap za udaljeni pristup bazi
  /*
  public static Connection getConnection(){
      Connection conn = null;
      try {
	    conn = DriverManager.getConnection(
	        iniFile.getString("database", "url"), 
	        iniFile.getString("database", "username"), 
	        iniFile.getString("database", "password"));     
	    conn.setAutoCommit(false);
	  } catch (SQLException e) {
	    log.fatal(e);
	    splash.setVisible(false);
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(null, "Gre\u0161ka pri konekciji sa bazom", "Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
	    System.exit(0);
	  }  
	  return conn;
  }
  */


  public static User getLibrarian() {
    return librarian;
  }

//  public static void setConnection(Connection c) {
//    connection = c;
//  }
  
  public static SplashScreen getSplash() {
    return splash;
  }

  /*
  public static UFormat getFormat() {
    return format;
  }

  public static INIFile getINIFile() {
    return iniFile;
  }
  */

  public static boolean isStandalone(){
  	return standalone;
  }

  /*
  public static ServiceFactory getFactory(int type){
	  if(type == CommandType.HIBERNATE){
		  return new HibernateServiceFactory();
	  } else if (type == CommandType.HIBERNATEARCHIVE){
		  return new HibernateArchiveServiceFactory();
	  } else if (type == CommandType.JDBC){
		  return new JdbcServiceFactory();
	  } else if (type == CommandType.NONE){
		  ;
	  }
	  return null;
  }
  
  public static Service getJdbcService(){
	  return jdbcservice; 
  }
  
  public static String getFileManagerURL(){
  	return fileMgrURL;
  }
  
  public static boolean isFileMgrEnabled(){
  	return fileMgrEnabled;
  }
  */

  private static SplashScreen splash;
  private static User librarian;
  private static boolean standalone;
  /*
  private static MainFrame mf;
  private static RecordManager recMgr;
  //private static Connection connection;
  private static Service jdbcservice;
  private static Librarian librarian;
  private static UFormat format;
  private static INIFile iniFile;
  private static Log log = LogFactory.getLog(BisisApp.class.getName());
  
  private static String fileMgrURL = "";
  private static boolean fileMgrEnabled = false;
  */
}
