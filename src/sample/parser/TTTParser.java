/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.parser;

import sample.utils.ParserUtils;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.naming.NamingException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Administrator
 */
public class TTTParser {

    public static Set<String> productURLs = null;

    public static Set<String> getProductLinks(String content)
            throws SQLException, NamingException, XMLStreamException {
        XMLEvent event = null;

        XMLEventReader reader = ParserUtils.getReader(content);
        Iterator<XMLEvent> iterator = ParserUtils.fixWellForm(reader);
        String url = "";

        while (iterator.hasNext()) {

            event = iterator.next();

            if (event.isStartElement()) {
                StartElement se = event.asStartElement();
                String seQName = se.getName().getLocalPart();

                if (seQName.equals("a")) {
                    Iterator<Attribute> attributes = se.getAttributes();
                    Attribute attr = null;

                    while (attributes.hasNext()) {
                        attr = attributes.next();
                        String name = attr.getName().getLocalPart().trim();
                        String value = attr.getValue().trim();

                        if (name.equals("href") && value.contains("/p/")) {
                            url = value;
                            if (productURLs == null) {
                                productURLs = new HashSet<>();
                            }
                            productURLs.add(url);
                            break;
                        }
                    }
                }
            }
        }
        return productURLs;
    }
}
