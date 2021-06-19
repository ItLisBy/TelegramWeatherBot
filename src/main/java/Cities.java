import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Cities {

    private HashMap<String, City> cities = new HashMap<>();

    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        XMLHandler handler = new XMLHandler();
        try {
            parser.parse(new File("cities.xml"), handler);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    public City getCity(String city){
        return cities.get(city);
    }

    private class City{
        private String lat;
        private String lon;

        City(String lat, String lon){
            this.lat = lat;
            this.lon = lon;
        }
    }
    private class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("City")) {
                String name = attributes.getValue("name");
                String lat = attributes.getValue("lat");
                String lon = attributes.getValue("lon");
                cities.put(name , new City(lat, lon));
            }
        }
    }
}