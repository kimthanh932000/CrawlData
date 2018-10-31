/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.main;

import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import sample.crawler.NhaThuoc365;
import sample.crawler.TrungTamThuoc;

/**
 *
 * @author Administrator
 */
public class Main {

    public static int count = 0;   //count product

    public static void main(String[] args)
            throws IOException, XMLStreamException, SQLException, NamingException, JAXBException {
        TrungTamThuoc.Crawler();
        NhaThuoc365.Crawler();
    }
}
