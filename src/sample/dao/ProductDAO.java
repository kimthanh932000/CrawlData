/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.NamingException;
import sample.jaxb.product.Product;
import sample.utils.DBUtils;

/**
 *
 * @author Administrator
 */
public class ProductDAO {

    public static int addNewProduct(ArrayList<Product> list, int cateID)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        int count[] = {};
        
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                String sql = "Insert into Product(Name, Code, Description, Price, ImageURL, CategoryID) "
                        + "values(?,?,?,?,?,?)";
                stm = con.prepareStatement(sql);
                
                con.setAutoCommit(false);
                
                for (Product product : list) {

                    stm.setString(1, product.getName());
                    stm.setString(2, product.getCode());
                    stm.setString(3, product.getDescription());
                    stm.setDouble(4, product.getPrice());
                    stm.setString(5, product.getImageURL());
                    stm.setInt(6, cateID);
                    stm.addBatch();
                    
                }
                count = stm.executeBatch();
                
//                for (int i : result) {
//                    if(i == 1)
//                        count++;
//                }
                con.commit();               
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return count.length;
    }
}
