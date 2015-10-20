package net.chi6rag.twitchblade;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.sql.*;

public class DbConnection {
    private String host;
    private String port;
    private String username;
    private String password;
    private String dbName;
    private Connection connection;

    public DbConnection(String environment) {
        setDatabaseDriver("org.postgresql.Driver");
        setConfiguration(environment);
        setConnection();
    }

    public PreparedStatement prepareStatement(String query) throws SQLException{
        return this.connection.prepareStatement(query);
    }

    public void close(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to close Database Connection");
            // e.printStackTrace();
        }
    }

    public void commit(){
        try {
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    public void setAutoCommit(boolean val) {
        try {
            this.connection.setAutoCommit(val);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollback(){
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setConfiguration(String env){
        DOMParser xmlDomParser = new DOMParser();
        try {
            xmlDomParser.parse("src/database_config.xml");
        } catch (SAXException e) {
             // e.printStackTrace();
        } catch (IOException e) {
             // e.printStackTrace();
        }
        Document doc = xmlDomParser.getDocument();
        Node envNode = doc.getElementsByTagName(env).item(0);
        NodeList envConfig = envNode.getChildNodes();

        for (int i = 0; i < envConfig.getLength(); i++) {
            Node node = envConfig.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();
                if (nodeName.equals("host")) {
                    this.host = node.getTextContent();
                }
                else if (nodeName.equals("dbname")){
                    this.dbName = node.getTextContent();
                }
                else if (nodeName.equals("port")) {
                    this.port = node.getTextContent();
                }
                else if (nodeName.equals("username")) {
                    this.username = node.getTextContent();
                }
                else if (nodeName.equals("password")) {
                    this.password = node.getTextContent();
                }
            }
        }

    }

    private void setDatabaseDriver(String driverUrl){
        try {
            Class.forName(driverUrl);
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not Found!");
            // e.printStackTrace();
        }
    }

    private void setConnection(){
        try {
            this.connection = DriverManager.getConnection(
                "jdbc:postgresql://" + this.host + ":" + this.port +
                "/" + this.dbName, this.username, this.password);
        } catch (SQLException e) {
            System.out.println("Configuration Not Specified");
            //e.printStackTrace();
        }
    }

}