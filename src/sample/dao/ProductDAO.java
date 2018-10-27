/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.NamingException;
import sample.utils.DBUtils;

/**
 *
 * @author Administrator
 */
public class ProductDAO {
    public boolean addNewProduct(String name, String code, String description, double price, String imageURL, int cateID) 
            throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement stm = null;
        
        try{
            con = DBUtils.makeConnection();
            if(con != null){
                String sql = "Insert into Product(Name, Code, Description, Price, ImageURL, CategoryID) "
                        + "values(?,?,?,?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, name);
                stm.setString(2, code);
                stm.setString(3, description);
                stm.setDouble(4, price);
                stm.setString(5, imageURL);
                stm.setInt(6, cateID);
                
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
