package services;

import org.xml.sax.SAXException;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import java.io.File;
import java.io.IOException;

public class DocumentService {
    public File createDocument(String fileName, Object catalog, Schema schema) throws SAXException, IOException, JAXBException {
        XmlParser parser = new XmlParser();
        parser.convertObjectToXml(catalog, fileName, schema);
        return new File(fileName);
    }
}
