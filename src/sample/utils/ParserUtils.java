/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.utils;

import com.sun.xml.internal.stream.events.EndElementEvent;
import com.sun.xml.internal.stream.events.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Administrator
 */
public class ParserUtils {

    public static Iterator<XMLEvent> fixWellFormPoductDetails(XMLEventReader reader) {
        ArrayList<XMLEvent> IEvents = new ArrayList<>();
        int endTagMarker = 0;
        while (endTagMarker >= 0) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
//                System.out.println(event);
                if (event.isEndDocument()) {
                    break;
                }
            } catch (XMLStreamException exception) {
                String msg = exception.getMessage();
//                System.out.println(msg);
                String msgErrorString = "The element type \"";

                if (msg.contains(msgErrorString)) {
                    String missingTagName = msg.substring(msg.indexOf(msgErrorString) + msgErrorString.length(), msg.indexOf("\" must be terminated"));
                    EndElement missingTag = new EndElementEvent(new QName(missingTagName));
                    event = missingTag;
                }
                msgErrorString = "Element type \"";

                if (msg.contains(msgErrorString)) {
                    String missingTagName = msg.substring(msg.indexOf(msgErrorString) + msgErrorString.length(), msg.indexOf("\" must be followed"));
                    EndElement missingTag = new EndElementEvent(new QName(missingTagName));

                    event = missingTag;
                }

            } catch (NullPointerException exception) {
                break;
            }
            if (event != null) {
                if (event.isStartElement()) {
                    endTagMarker++;
                } else if (event.isEndElement()) {
                    endTagMarker--;

                }
                if (endTagMarker >= 0) {
                    IEvents.add(event);
                }
            }
        }
//        while(IEvents.iterator().hasNext()){
//            System.out.println(IEvents.iterator().next());
//        }
        return IEvents.iterator();
    }

    public static Iterator<XMLEvent> fixWellForm(XMLEventReader reader) {
        ArrayList<XMLEvent> IEvents = new ArrayList<>();
        int endTagMarker = 0;
        while (endTagMarker >= 0) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
                if (event.isEndDocument()) {
                    break;
                }
            } catch (XMLStreamException exception) {
                String msg = exception.getMessage();
                String msgErrorString = "The element type \"";

                if (msg.contains(msgErrorString)) {
                    String missingTagName = msg.substring(msg.indexOf(msgErrorString) + msgErrorString.length(), msg.indexOf("\" must be terminated"));
                    EndElement missingTag = new EndElementEvent(new QName(missingTagName));
                    event = missingTag;
                }
                msgErrorString = "Element type \"";

                if (msg.contains(msgErrorString)) {
                    String missingTagName = msg.substring(msg.indexOf(msgErrorString) + msgErrorString.length(), msg.indexOf("\" must be followed"));
                    EndElement missingTag = new EndElementEvent(new QName(missingTagName));

                    event = missingTag;
                }

            } catch (NullPointerException exception) {
                break;
            }
            if (event != null) {
                if (event.isStartElement()) {
                    endTagMarker++;
                } else if (event.isEndElement()) {
                    endTagMarker--;

                }
                if (endTagMarker >= 0) {
                    IEvents.add(event);
                }
            }
        }

        return IEvents.iterator();
    }

    //get reader
    public static XMLEventReader getReader(String content) throws XMLStreamException {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        StreamSource streamsource = new StreamSource(is);
        XMLEventReader reader = null;
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setEventAllocator(new XMLEventAllocatorImpl());
        reader = xif.createXMLEventReader(streamsource.getInputStream());
        return reader;
    }
}
