package fakeStore;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.awaitility.Awaitility;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.setDefaultPollInterval;

public class HashFakeStore {

    private String URL;
    private String ALLPRODUCTS;
    private int addNumber = 1;


    private io.restassured.response.Response response;
    private RequestSpecification request;



    @BeforeClass
    void setUp() throws IOException {

//  load baca antar file berdasarkan alamat property
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/config.properties"));

//  baca value di dalam file string
        URL = properties.getProperty("fake.url");
        ALLPRODUCTS = properties.getProperty("getAllProducts");

//  untuk respon link API dan tidak perlu inisiasi di test lain (langsung endpoint) selama satu
        request = RestAssured
                .given()
                .baseUri(URL)
                .log().all();
    }


    @Test
    void get(){
        response = request
                .when()
                .get("/products");
        System.out.println(response.asPrettyString());
    }

    @Test
    void addData(){
//  untuk merubah data gunakan HashMap mirip seperti List
//  struktur kode-nya;
//  Map<String, Object> "nama attribute" = new HashMap<>();
//  "nama attribute".put("String/key path", "value path");


        Map<String, Object> productData = new HashMap<>();
        productData.put("id", "1");
        productData.put("title", "vans t-shirt");
        productData.put("price", 29.95);
        productData.put("description", "Perfect t-shirt classic graph");
        productData.put("category", "men's clothing");
        productData.put("image", "Taka No Me");
        productData.put("rating", 4.9);
        productData.put("count" , 300);

        response = request
                .header("Content-Type", "application/json")
                .body(productData)
                .when()
                .put("/products/" + "1");

        response.then()
                .log().all()
                .statusCode(200)
                .body("id", org.hamcrest.Matchers.notNullValue())
                .body("title", org.hamcrest.Matchers.equalTo("vans t-shirt"));



        System.out.println("hasilnya: " + productData);
        System.out.println(response.asPrettyString());

    }

    @Test
    void getTitle() throws InterruptedException {
        response = request
                .given()
                .when()
                .get("products")
                ;

        List<String> title = response.jsonPath().getList("title");
        System.out.println("title: ");

        if (title != null)
        for (String Title : title){
        System.out.println(addNumber + ". " + Title);
        addNumber++;
        } else {
            System.out.println("data tidak ditemukan");
        }
    }

    @Test
    void getPrice(){
        await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    response = request
                            .when()
                            .get(ALLPRODUCTS);
                    List<Number> price = response.jsonPath().getList("price");
//                            return price != null && !price.isEmpty();


                    if (price != null) {
                        System.out.println("price ditemukan");
                        for (Number Price : price){
                            System.out.println(addNumber + ". " + Price);
                            addNumber++;
                        }
                    }

                });
    }

    @Test
    void getDescription(){

        await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    response = request
                            .when()
                            .get(ALLPRODUCTS);

                    List<String> description = response.jsonPath().getList("description");
                    if (description != null)
                        System.out.println("Here Description Product");
                        for (String Desc : description){
                            System.out.println(addNumber + ". " + Desc);
                            addNumber++;
                        }
                });

    }

    @Test
    void getCategory(){
        response = request
                .when()
                .get(ALLPRODUCTS);

        List<String> category = response.jsonPath().getList("category");
        if (category != null)
            System.out.println("Here Category List Product");
            for (String Category : category){
                System.out.println(addNumber + ". " + Category);
                addNumber++;
            }
    }

    @Test
    void getImage(){
        response = request
                .when()
                .get(ALLPRODUCTS);

        List<String> image = response.jsonPath().getList("image");
        if (image != null)
            System.out.println("Here image Products");
        for (String Image : image){
            System.out.println(addNumber + ". " + Image);
            addNumber++;
        }
    }

    @Test
    void getRate(){
        response = request
                .when()
                .get(ALLPRODUCTS);

        List<Float> rate = response.jsonPath().getList("rating.rate", Float.class);
        if (rate != null)
            System.out.println("Here rate products");
        for (Float Rate : rate){
            System.out.println(addNumber + ". " + Rate);
            addNumber++;
        }
    }

    @Test
    void getCount(){
        response = request
                .when()
                .get(ALLPRODUCTS);

        List<Integer> count = response.jsonPath().getList("rating.count");
        if (count != null)
            System.out.println("Here count products");
        for (Integer Count : count){
            System.out.println(addNumber + ". " + Count);
            addNumber++;
        }
    }
}
