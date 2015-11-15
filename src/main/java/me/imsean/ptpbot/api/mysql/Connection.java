package me.imsean.ptpbot.api.mysql;

import java.sql.*;

public class Connection {

    static final String DRIVER = "com.mysql.jdbc.Driver";

    private String host, name, username, password;

    public static java.sql.Connection conn = null;

    private PreparedStatement stmt = null;


    public Connection(String host, String database, String username, String password) {
        this.host = host;
        this.name = database;
        this.username = username;
        this.password = password;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection("jdbc:mysql://" + this.host + "/" + this.name, this.username, this.password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection query(String sql) {
        try {
            this.stmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ResultSet execute() throws SQLException {
        return this.stmt.executeQuery();
    }

    public PreparedStatement getStatement() {
        return this.stmt;
    }

}
