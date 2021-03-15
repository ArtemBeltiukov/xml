import model.Catalog;
import model.Category;
import org.xml.sax.SAXException;
import services.DataGenerator;
import services.DocumentService;
import services.SchemaGenerator;
import services.XmlParser;
import javax.xml.bind.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    private static final String xmlPath = "catalog.xml";
    public static void main(String[] args) {
        DocumentService documentService = new DocumentService();
        XmlParser parser = new XmlParser();
        // Создание объекта Catalog, и его заполнение
        Catalog catalog = DataGenerator.generateCatalog();
        try {
            // Создание объекта Schema из xsd файла
            Schema schema = new SchemaGenerator().generateSchema();
            // Создание и парсинг xml
            File xmlFile = documentService.createDocument(xmlPath,catalog, schema);
            Catalog unmarshalCatalog = parser.fromXmlToObject(xmlFile.getPath());
            if (unmarshalCatalog != null) {
                List<Category> categoryList = unmarshalCatalog.getCategories();
                categoryList.forEach(System.out::println);
            }
            String json = parser.convertXMLToJSON(unmarshalCatalog);
            System.out.println(json);
            Catalog catalog1 = (Catalog)parser.convertJSONtoXML(json, Catalog.class);
            System.out.println(catalog1);
            // Достаем StAX подкатегории
            XMLInputFactory xif = XMLInputFactory.newFactory();
            StreamSource xml = new StreamSource(xmlFile);
            XMLStreamReader xsr = xif.createXMLStreamReader(xml);
            xsr.nextTag();
            while(!xsr.getLocalName().equals("category")) {
                xsr.nextTag();
            }

            JAXBContext jc = JAXBContext.newInstance(Category.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<Category> jb = unmarshaller.unmarshal(xsr, Category.class);
            xsr.close();

            Category catalog2 = jb.getValue();
            catalog2.getSubCategory().forEach(System.out::println);
        } catch (MarshalException e){
            System.out.println(e);
        } catch (SAXException | IOException | JAXBException | XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
