package com.chatapp.util;

import com.chatapp.model.Message;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLMessageLogger {
    private static final String XML_LOGS_DIR = "xml-logs";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void logMessage(Message message, String senderUsername, String receiverUsername) {
        try {
            // Create logs directory if it doesn't exist
            File logsDir = new File(XML_LOGS_DIR);
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }
            
            String fileName = getLogFileName(message.getSenderId(), message.getReceiverId());
            File xmlFile = new File(XML_LOGS_DIR, fileName);
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;
            Element root;
            
            if (xmlFile.exists()) {
                doc = builder.parse(xmlFile);
                root = doc.getDocumentElement();
            } else {
                doc = builder.newDocument();
                root = doc.createElement("chat_history");
                doc.appendChild(root);
            }
            
            // Create message element
            Element messageElement = doc.createElement("message");
            messageElement.setAttribute("id", String.valueOf(message.getMessageId()));
            messageElement.setAttribute("timestamp", dateFormat.format(new Date()));
            
            Element sender = doc.createElement("sender");
            sender.setAttribute("id", String.valueOf(message.getSenderId()));
            sender.setTextContent(senderUsername);
            messageElement.appendChild(sender);
            
            if (message.getReceiverId() > 0) {
                Element receiver = doc.createElement("receiver");
                receiver.setAttribute("id", String.valueOf(message.getReceiverId()));
                receiver.setTextContent(receiverUsername);
                messageElement.appendChild(receiver);
            }
            
            if (message.getGroupId() > 0) {
                Element group = doc.createElement("group");
                group.setAttribute("id", String.valueOf(message.getGroupId()));
                messageElement.appendChild(group);
            }
            
            Element content = doc.createElement("content");
            content.setAttribute("type", message.getMessageType());
            content.setTextContent(message.getContent());
            messageElement.appendChild(content);
            
            if (message.getFileName() != null) {
                Element fileNameElement = doc.createElement("file_name");
                fileNameElement.setTextContent(message.getFileName());
                messageElement.appendChild(fileNameElement);
            }
            
            root.appendChild(messageElement);
            
            // Save to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<Message> getChatHistory(int userId1, int userId2) {
        List<Message> messages = new ArrayList<>();
        try {
            String fileName = getLogFileName(userId1, userId2);
            File xmlFile = new File(XML_LOGS_DIR, fileName);
            
            if (!xmlFile.exists()) {
                return messages;
            }
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            
            NodeList messageNodes = doc.getElementsByTagName("message");
            for (int i = 0; i < messageNodes.getLength(); i++) {
                Element messageElement = (Element) messageNodes.item(i);
                Message message = new Message();
                
                message.setMessageId(Integer.parseInt(messageElement.getAttribute("id")));
                
                Element sender = (Element) messageElement.getElementsByTagName("sender").item(0);
                message.setSenderId(Integer.parseInt(sender.getAttribute("id")));
                
                NodeList receiverNodes = messageElement.getElementsByTagName("receiver");
                if (receiverNodes.getLength() > 0) {
                    Element receiver = (Element) receiverNodes.item(0);
                    message.setReceiverId(Integer.parseInt(receiver.getAttribute("id")));
                }
                
                Element content = (Element) messageElement.getElementsByTagName("content").item(0);
                message.setContent(content.getTextContent());
                message.setMessageType(content.getAttribute("type"));
                
                messages.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    private static String getLogFileName(int userId1, int userId2) {
        int smaller = Math.min(userId1, userId2);
        int larger = Math.max(userId1, userId2);
        return "chat_" + smaller + "_" + larger + ".xml";
    }
}