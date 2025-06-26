import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;

public class OSMParserSAX {
    public static void main(String[] args) {
        File osmInput = new File("src/resources/greece.osm"); // ή αλλάξτε το σε όποιο OSM έχετε
        File csvOutput = new File("src/resources/data.csv");

        try {
            SAXParserFactory spFactory = SAXParserFactory.newInstance();
            SAXParser parser = spFactory.newSAXParser();

            try (BufferedWriter out = new BufferedWriter(new FileWriter(csvOutput))) {
                out.write("id,name,lat,lon\n");

                DefaultHandler osmHandler = new DefaultHandler() {
                    String id, lat, lon;
                    String name = " ";
                    boolean isNode = false;

                    public void startElement(String uri, String local, String qName, Attributes attrs) {
                        if ("node".equals(qName)) {
                            isNode = true;
                            id = attrs.getValue("id");
                            lat = attrs.getValue("lat");
                            lon = attrs.getValue("lon");
                            name = " ";
                        } else if (isNode && "tag".equals(qName)) {
                            if ("name".equals(attrs.getValue("k"))) {
                                name = attrs.getValue("v").replace(",", "");
                            }
                        }
                    }

                    public void endElement(String uri, String local, String qName) throws SAXException {
                        if ("node".equals(qName)) {
                            try {
                                out.write(id + "," + name + "," + lat + "," + lon + "\n");
                            } catch (IOException e) {
                                throw new SAXException("Write failed", e);
                            }
                            isNode = false;
                        }
                    }
                };
                parser.parse(osmInput, osmHandler);
                System.out.println("Exported successfully to file data.csv");
            }
        } catch (Exception ex) {
            System.err.println("Error with parse: " + ex.getMessage());
        }
    }
}

