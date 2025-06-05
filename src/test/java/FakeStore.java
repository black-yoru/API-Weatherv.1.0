import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import java.util.*;
public class FakeStore {

    private static String URL;
    private static String TOKEN;
    static int addNumber = 1;



    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        Thread.sleep(3000);

        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/config.properties")); //load file yang dituju

        URL = properties.getProperty("fake.url");//didalam file cari value mengandung string
        TOKEN = properties.getProperty("token");

        RestAssured.baseURI = URL; //tetapkan alamat api dan baseURI untuk endpoint setiap test berikan object address
    }

    @Test
    public void getRequest() throws InterruptedException {
        Thread.sleep(3000);
        Response response = RestAssured
                .given()
                .when()
                .get("products")
                ;


//        List<Map<String, Object>> users = response.jsonPath().getList("data");

//  membaca objek nilai setiap index email di dalam array
//        List<String> title = response.jsonPath().getList("title");
//        System.out.println("title: ");
//        addNumber = 1;
//        for (String Title : title){
//        System.out.println(addNumber + ". " + Title);
//        addNumber++;
//        }

//  membaca objek nilai setiap index email di dalam array
//        List<Integer> id = response.jsonPath().getList("id");
//        System.out.println("\nid");
//
//        addNumber = 1;
//        for (int Id : id) {
//            System.out.println(addNumber + ". " + Id);
//            addNumber++;
//        }


////        System.out.println("email: " + email);
//        for (Map<String, Object> user : users){
//            System.out.println("email: " + user.get("email") + ", ID: " + user.get("id"));
//        }



//        int userId = response.jsonPath().getInt("id[1]");
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("\nResponse body:");
        System.out.println(response.asPrettyString());
        System.out.println("Thread ID: " + Thread.currentThread().getId());

//        System.out.println("\nuserID: " + userId);

    }

    @Test
    public void getCity() throws InterruptedException {
        Thread.sleep(3000);

        RestAssured.baseURI = URL;

        Response response = RestAssured
                .given()
                .when()
                .get("users")
                ;

        List<String> cities = response.jsonPath().getList("address.city");
        System.out.println("city: ");
//  untuk memberikan nomor urut dan dimulai dengan angka 1
        addNumber = 1;
//  loop ini untuk setiap elemen
        if (cities != null)
        for (String City : cities){
            System.out.println(addNumber + ". " + City + " city");
            addNumber++; //angka 1 akan berlanjut sampai berbapun sebanyak data array
        } else{
            System.out.println("\ndata tidak ditemukan atau format salah");
        }
        System.out.println("Thread ID: " + Thread.currentThread().getId());
    }

    @Test
    public void getZipCode() throws InterruptedException {
        Thread.sleep(3000);

        RestAssured.baseURI = URL;
        Response response = RestAssured
                .given()
                .when()
                .get("users")
                ;

        List<String> zipCode = response.jsonPath().getList("address.zipcode");
        System.out.println("\nzipcode: ");

        addNumber = 1;
        for (String zip : zipCode){
            System.out.println(addNumber + ". " + zip);
            addNumber++;
            System.out.println("Thread ID: " + Thread.currentThread().getId());
        }
    }

}
