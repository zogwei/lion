package com.alacoder.lion.tcc.spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.alacoder.lion.tcc.spring.demo.DemoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TccServiceImpl implements TccService {

    private final static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    private DataSource dataSource;

    public void start(String businessType, String businessId, Properties properties) {
        TransactionSynchronization synchronization = new TccTransactionSynchronization();
        TransactionSynchronizationManager.registerSynchronization(synchronization);


        // 拿到本地事务与连接
        DataSource transactionDataSource = null;
        ConnectionHolder connectionHolder = null;
        Connection transactionConn = null;

        Map<Object, Object> resMap = TransactionSynchronizationManager.getResourceMap();
        for (Map.Entry<Object, Object> item : resMap.entrySet()) {
            Object key = item.getKey();
            if (key instanceof DataSource) {
                transactionDataSource = (DataSource) key;
                connectionHolder = (ConnectionHolder) item.getValue();
                transactionConn = connectionHolder.getConnection();
            }
        }
        
        String tx_id = addActivity(transactionDataSource, businessType, businessId, properties);

        updateActivityState(transactionConn, tx_id);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void updateActivityState(Connection conn, String tx_id) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE `lion_tcc_business_activity` SET `state`='commit' WHERE `tx_id`='" + tx_id
                               + "';");
        } catch (Exception e) {

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private String addActivity(DataSource dataSource, String businessType, String businessId, Properties properties) {
        Statement stmt = null;
        Connection conn = null;
        String tx_id = System.currentTimeMillis() + "";
        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement(); 
            stmt.executeUpdate("INSERT INTO `lion_tcc_business_activity` (`tx_id`, `state`, `propagation`, `cotext`) VALUES ('"
                               + tx_id + "', 'init', 'dd', 'dd');");
        } catch (Exception e) {

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return tx_id;
    }

}
