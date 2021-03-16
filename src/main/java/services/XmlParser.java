package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Catalog;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import java.io.File;
import java.io.IOException;

public class XmlParser {

    public File convertObjectToXml(Object catalog, String filePath, Schema schema) throws JAXBException {
        File xmlFile = new File(filePath);
        JAXBContext context = JAXBContext.newInstance(Catalog.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setSchema(schema);
        marshaller.marshal(catalog, xmlFile);
        return xmlFile;
    }

    public Catalog fromXmlToObject(String filePath) throws IOException, SAXException, JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
        Unmarshaller un = jaxbContext.createUnmarshaller();
        return (Catalog) un.unmarshal(new File(filePath));
    }

    public String convertXMLToJSON(Catalog xml){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(xml);
    }

    public  Object convertJSONtoXML(String json, Class XMLClass){
        return new Gson().fromJson(json, XMLClass);
    }
}
