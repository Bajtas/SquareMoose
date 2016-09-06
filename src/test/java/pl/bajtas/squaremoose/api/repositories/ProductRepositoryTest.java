package pl.bajtas.squaremoose.api.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pl.bajtas.squaremoose.api.configuration.RepositoryConfiguration;
import pl.bajtas.squaremoose.api.domain.Category;
import pl.bajtas.squaremoose.api.domain.Product;
import pl.bajtas.squaremoose.api.repository.CategoryRepository;
import pl.bajtas.squaremoose.api.repository.ProductRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RepositoryConfiguration.class})
public class ProductRepositoryTest {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //@Autowired
    private NodeList nList;

    @Before
    public void setUpData() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File fXmlFile = new File(classLoader.getResource("test/ProductRepository_data1.xml").getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            nList = doc.getElementsByTagName("record");

            System.out.println("----------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveProductWithoutCategory() {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            long productCount = productRepository.count();
            assertNotNull(productCount);

            Node nNode = nList.item(temp);

            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                //setup product
                Product product = new Product();
                product.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                product.setPrice(Float.parseFloat(eElement.getElementsByTagName("price").item(0).getTextContent()));
                product.setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
                product.setAddedOn(new Date());
                product.setLmod(new Date());


                //save product, verify has ID value after save
                assertNull(product.getId()); //null before save
                productRepository.save(product);
                assertNotNull(product.getId()); //not null after save

                //fetch from DB
                Product fetchedProduct = productRepository.findOne(product.getId());

                //should not be null
                assertNotNull(fetchedProduct);

                //should equal
                assertEquals(product.getId(), fetchedProduct.getId());
                assertEquals(product.getDescription(), fetchedProduct.getDescription());

                //update description and save
                fetchedProduct.setDescription("New Description");
                productRepository.save(fetchedProduct);

                //get from DB, should be updated
                Product fetchedUpdatedProduct = productRepository.findOne(fetchedProduct.getId());
                assertEquals(fetchedProduct.getDescription(), fetchedUpdatedProduct.getDescription());

                long productAfterCount = productRepository.count();

                //verify count of products in DB
                assertEquals(productCount+1, productAfterCount);
            }
        }
    }

    @Test
    public void testSaveProductWithCategory() {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            long productCount = productRepository.count();
            assertNotNull(productCount);

            Node nNode = nList.item(temp);

            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                //setup product
                Product product = new Product();
                product.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                product.setPrice(Float.parseFloat(eElement.getElementsByTagName("price").item(0).getTextContent()));
                product.setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
                product.setAddedOn(new Date());
                product.setLmod(new Date());

                assertNull(product.getCategory());
                Category category = new Category();
                assertNull(category.getId());

                category.setName("testCategory");

                categoryRepository.save(category);
                assertNotNull(category.getId());

                product.setCategory(category);
                //save product, verify has ID value after save
                assertNull(product.getId()); //null before save
                productRepository.save(product);
                assertNotNull(product.getId()); //not null after save

                assertEquals(product.getCategory(), category);
                List<Product> products = new ArrayList<Product>();
                products.add(product);

                assertEquals(products.size(), 1);

                category.setProducts(products);

                categoryRepository.save(category);

                assertEquals(category.getProducts().get(0), product);

                //fetch from DB
                Product fetchedProduct = productRepository.findOne(product.getId());

                //should not be null
                assertNotNull(fetchedProduct);

                //should equal
                assertEquals(product.getId(), fetchedProduct.getId());
                assertEquals(product.getDescription(), fetchedProduct.getDescription());

                //update description and save
                fetchedProduct.setDescription("New Description");
                productRepository.save(fetchedProduct);

                //get from DB, should be updated
                Product fetchedUpdatedProduct = productRepository.findOne(fetchedProduct.getId());
                assertEquals(fetchedProduct.getDescription(), fetchedUpdatedProduct.getDescription());

                long productAfterCount = productRepository.count();

                //verify count of products in DB
                assertEquals(productCount+1, productAfterCount);
            }
        }
    }
}
