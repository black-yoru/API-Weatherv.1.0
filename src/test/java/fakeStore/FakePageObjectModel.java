package fakeStore;

import helper.Utility;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



/* API fakestore tidak bisa membuat value data abaru default seperti id, rating, rate, count
untuk merubah data existing dapat dilakukan
* */



public class FakePageObjectModel {

// Step 1: memanggil class Utility dan diberi attribute name akan memiliki nilai objek utility
    private Utility helper;

// Rest Assured secara spesifik untuk menghindari error test paralel
    private RequestSpecification requestSpecification;
    private String URL;
    private int [] idProduct = {2, 1, 21, 22};
    private int [] id = {1, 2, 5, 6};



    @BeforeClass
    void setUp() throws IOException {
//  Step 2: membuat konstruktor class Utility

        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/config.properties"));

        URL = properties.getProperty("fake.url");

        requestSpecification = RestAssured.given().baseUri(URL);
        helper = new Utility(requestSpecification);

    }

    @Test
    void getData(){

        Response getData = helper.getData();
        System.out.println(getData.asPrettyString());

    }


    @Test
    void getSingleProduct(){
        int id = idProduct[0];
        Response getData = helper.getDataById(id);
        System.out.println(getData.asPrettyString());
    }


    @Test
    void createProduct(){
//        int id = idProduct [0];

        Map<String, Object> productData = new HashMap<>();
//        productData.put("id", idProduct);
        productData.put("title", "Nike t-shirt");
        productData.put("price", 29.95);
        productData.put("description", "Perfect t-shirt classic graph");
        productData.put("category", "men's clothing");
        productData.put("image", "Taka No Me");
        productData.put("rating.rate", 4.9);
        productData.put("count" , 300);

        Response createDataProduct = helper.createProduct(productData);

        System.out.println(createDataProduct.asPrettyString());


                createDataProduct.then()
                        .statusCode(200)
                        .body("title", org.hamcrest.Matchers.equalTo("Nike t-shirt"))
                        .body("price", org.hamcrest.Matchers.equalTo(29.95f))
                        .body("description", org.hamcrest.Matchers.equalTo("Perfect t-shirt classic graph"))
                        .body("category", org.hamcrest.Matchers.containsString("men's clothing"))
                        .body("image", org.hamcrest.Matchers.equalTo("Taka No Me"));

    }


    @Test
    void createBulkProducts(){
        for (int i = 5; i < 5; i++) {

            Map<String, Object> productData = new HashMap<>();
            productData.put("title", "Nocta AWR");
            productData.put("price", 100.95);
            productData.put("description", "Free your mood and goals");
            productData.put("category", "men's clothing");
            productData.put("image", "Drake at 3 PM");

            Response response = helper.createProduct(productData);

            response.then()
                    .statusCode(200)
                    .body("title", org.hamcrest.Matchers.equalTo("Nocta AWR"))
                    .body("price", org.hamcrest.Matchers.equalTo(100.95f))
                    .body("description", org.hamcrest.Matchers.equalTo("Free your mood and goals"))
                    .body("category", org.hamcrest.Matchers.containsString("men's clothing"))
                    .body("image", org.hamcrest.Matchers.equalTo("Drake at 3 PM"));

            System.out.println("product created: " + i);
            System.out.println("result: " + response.asPrettyString());
        }
    }




    @Test
    void testUpdate() {
//  Step 3: inisiasi id
        int idProduct = 1;

// Step 4: Get product sebelum update/cetak hasil get "default"
        Response beforeUpdate = helper.getDataById(idProduct);
        System.out.println("===Before Update===");
        System.out.println(beforeUpdate.asPrettyString());

//  Step 5: mempersiapkan update data
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("title", "tas gamara");

//  Step 6: kirim update request dan respon API
        Response updateResponse= helper.updateData(idProduct, updateData);
        System.out.println("===Update Response===");
        System.out.println(updateResponse.asPrettyString());

//  Step 7: get product after update
        Response afterUpdate = helper.getDataById(idProduct);
        System.out.println("===after update===");
        System.out.println(afterUpdate.asPrettyString());

//  Step 8: Assertion comparison
//        assertThat(beforeUpdate.path("title")).isNotEqualTo(afterUpdate.path("title"));
//        assertThat(afterUpdate.path("title")).isEqualTo("tas gamara");
    }

    @Test
    void getBulkProducts(){
        int [] ids = id;
        Response getBulkData = helper.getBulk(ids);
        System.out.println(getBulkData.asPrettyString());

    }
}
