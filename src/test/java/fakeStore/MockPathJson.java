package fakeStore;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MockPathJson {

    private String URL;
    private String ALL_PRODUCTS;

//dibuat secara global di dalam class semua method dapat menggunakan
    private io.restassured.response.Response response;
    private RequestSpecification requestSpecification;




    @BeforeClass
    void setUp() throws IOException {
//  load file link API berdasarkan address direktori
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/config.properties"));

//  membaca object di dalam file ("String") dan objek pastikan memiliki value
        URL = properties.getProperty("fake.url");

//  panggil value berisi address dan apakah API meresponnya
        requestSpecification = RestAssured
                .given()
                .baseUri(URL)
                .log().all();
    }

    @Test
    void getUser() throws IOException {
        response = requestSpecification
                .when()
                .get("/products");

        System.out.println("hasilnya" + response.asString());
    }

// edit path key JSon dan merubah value-nya
    @Test
    void editTitle(){
        Map<String, Object> title = new HashMap<>();
        title.put("title", "tas erigo raffi lifestyle");
        title.put("price", 200);

        response = requestSpecification
                .header("Content-Type", "application/json")
                .body(title)
                .when()
                .put("/products/" + "1");

        response.then()
                .log().all()
                .statusCode(200)
//                .body("id", org.hamcrest.Matchers.notNullValue())
                .body("title", org.hamcrest.Matchers.equalTo("tas erigo raffi lifestyle"));

        System.out.println("hasilnya : " + response.asPrettyString());
    }

    @Test
    void editId(){
        Map<String, Object> id = new HashMap<>();
        id.put("id", 0);

        response = requestSpecification
                .header("Content-Type", "application/json")
                .body(id)
                .when()
                .put("/products/" + "1");

        response.then()
                .statusCode(200)
                .body("id", org.hamcrest.Matchers.notNullValue())
                .body("id", org.hamcrest.Matchers.equalTo(1));

        System.out.println("result: " + response.asPrettyString());
    }


    @Test
    void beforeAfterData(){

    }




}
