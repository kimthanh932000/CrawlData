/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.NamingException;
import sample.db.DBUtils;

/**
 *
 * @author Administrator
 */
public class CategoryDAO implements Serializable{
    public boolean addNewCategory(String name, String description) 
            throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement stm = null;
        
        try{
            con = DBUtils.makeConnection();
            if(con != null){
                String sql = "Insert into Category(Name, Description) "
                        + "values(?,?)";
                con.prepareStatement(sql);
                stm.setString(1, name);
                stm.setString(2, description);
                
                int row = stm.executeUpdate();
                if(row > 0){
                    return true;
                }
            }
        }finally{
            if(stm != null){
                stm.close();
            }
            if(con != null){
                con.close();
            }
        }
        return false;
    }
}
