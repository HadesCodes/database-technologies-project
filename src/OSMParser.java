import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;

public class OSMParser {
    public static void main(String[] args) {
        try {
            // Αρχείο εισόδου .osm και αρχείο εξόδου .csv
            File inputOSM = new File("src/resources/kardia_map.osm");
            File outputCSV = new File("src/resources/data.csv");

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(inputOSM);
            xmlDocument.getDocumentElement().normalize();

            NodeList nodeList = xmlDocument.getElementsByTagName("node");

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputCSV))) {
                bufferedWriter.write("id,name,lat,lon\n");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element nodeElem = (Element) nodeList.item(i);
                    String id = nodeElem.getAttribute("id");
                    String lat = nodeElem.getAttribute("lat");
                    String lon = nodeElem.getAttribute("lon");

                    String nodeName = " ";
                    NodeList tagList = nodeElem.getElementsByTagName("tag");

                    for (int j = 0; j < tagList.getLength(); j++) {
                        Element tagElem = (Element) tagList.item(j);
                        if ("name".equals(tagElem.getAttribute("k"))) {
                            nodeName = tagElem.getAttribute("v").replace(",", "");
                            break;
                        }
                    }

                    bufferedWriter.write(id + "," + nodeName + "," + lat + "," + lon + "\n");
                }
            }
            System.out.println("CSV has been successfully written to: data.csv");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

