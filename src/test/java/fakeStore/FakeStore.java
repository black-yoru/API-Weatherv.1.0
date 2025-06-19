package fakeStore;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class FakeStore {

// Attribute Link dan Endpoint
    private String LINK;
    private String TOKEN;
    private String ALLPRODUCTS;


    private int addNumber = 1;


//  ini adalah RestAssured dengan spesifikasi hanya dapat dikases class ini saja dan objeknya reqFakeStore
    private static RequestSpecification reqFakeStore;

// mengembalikan response API seperti 200, 201, 401, 404 dan lainnya
    private io.restassured.response.Response response;

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
//  Dalam method ini ada beberapa langkah dibawah ini;
//  1. load file URL sebagai endpoint

        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/config.properties")); //load file yang dituju

        LINK = properties.getProperty("fake.url");//didalam file cari value mengandung string
        TOKEN = properties.getProperty("token");
        ALLPRODUCTS = properties.getProperty("getAllProducts");



         //tetapkan alamat api dan baseURI untuk endpoint setiap test berikan object address
        reqFakeStore = RestAssured
                        .given()
                        .baseUri(LINK)
                        .log().all();
    }

    @Test
    public void getRequest() throws InterruptedException {

//  Awaitility merupakan library untuk menunggu untuk testNg dala API testing
/*  Struktur kode awaitility
*   await()
*       .atMost("timeout:" 5, TimeUnit.SECONDS) Menunggu 5 detik
*       .pollInterval("pollInterval:" 1, TimeUnit.SECONDS) setiap 1 detik cek kondisi kode lambda java sampai true
*       .until(() -> {
           kode yang akan di cek
* )*/



        await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    io.restassured.response.Response response = reqFakeStore
                            .when()
                            .get(ALLPRODUCTS);
                    System.out.println("\nini adalah test Fake Store API" + "\nStatus code: " + response.getStatusCode());
                    System.out.println("Response body:");
                    System.out.println(response.asPrettyString());
                                });



        System.out.println("Thread ID: " + Thread.currentThread().getId());

//        List<String> title = response.jsonPath().getList("title");
//        System.out.println("title: ");
//        addNumber = 1;
//        if (title != null)
//        for (String Title : title){
//        System.out.println(addNumber + ". " + Title);
//        addNumber++;
//        } else {
//            System.out.println("data tidak ditemukan");
//        }
//
//        response.then().statusCode(200);


//        List<Map<String, Object>> users = response.jsonPath().getList("data");

//  membaca objek nilai setiap index email di dalam array

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


//        System.out.println("\nuserID: " + userId);

    }


    @Test
    void getSingleProduct(){
//  response API akan mengembalikan dari website yang sudah di inisasi di awal yaitu "fakestoreAPI"
        response = reqFakeStore
                .when()
                .get("products/2");

        System.out.println("hasilnya: " + response.getStatusCode() +"\n"
                            + response.prettyPrint());
    }


    @Test
    void createProduct(){
//  definisikan produk yang akan ditambahkan
//  membuat attribute yang berisikan data struktur JSON
        String requestBody = "{\n" +
                "       \"id\": 21,\n" +
                "        \"title\": \"vans t-shirt\",\n" +
                "        \"price\": 29.95,\n" +
                "        \"description\": \"Perfect t-shirt classic graph\",\n" +
                "        \"category\": \"men's clothing\",\n" +
                "        \"image\": \"Taka No Me\"\n" +
                "        }";
//        Map<String, Object> productData = new HashMap<>();
//        productData.put("id", "21");
//        productData.put("title", "vans t-shirt");
//        productData.put("price", 29.95);
//        productData.put("description", "Perfect t-shirt classic graph");
//        productData.put("category", "men's clothing");
//        productData.put("image", "Taka No Me");
//        productData.put("rating", 4.9);
//        productData.put("count" , 300);

//  panggil alamat dan AP
        response = reqFakeStore
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/products");

        response.then()
                .log().all()
                .statusCode(200)
                .body("id", org.hamcrest.Matchers.notNullValue())
                .body("title", org.hamcrest.Matchers.equalTo("vans t-shirt"));

//        if (response != null){
//            int statusCode = response.getStatusCode();
//            System.out.println("Status Code: " + statusCode);
//
//            if (statusCode == 201) {
//                System.out.println("Berhasil - Response not found 201");
//            } else {
//                System.out.println("Gagal - Response Code: " + statusCode);
//            }
//        } else {
//            System.out.println("Ada problem dalam aksesnya");
//        }

        System.out.println(response.getStatusCode() + "hasilnya: " + response.asString());


    }


    @Test
    void updateData(){
        String requestBody = "{\n" +
                "       \"id\": 21,\n" +
                "        \"title\": \"vans t-shirt\",\n" +
                "        \"price\": 29.95,\n" +
                "        \"description\": \"Perfect t-shirt classic graph\",\n" +
                "        \"category\": \"men's clothing\",\n" +
                "        \"image\": \"Taka No Me\"\n" +
                "        }";

//  untuk update mirip dengan create dan membedakan di put dan masukan id (string)
        response = reqFakeStore
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put("/products/" + "21");

        response.then()
                .log().all()
                .statusCode(200)
                .body("id", org.hamcrest.Matchers.notNullValue())
                .body("title", org.hamcrest.Matchers.equalTo("vans t-shirt"));

        System.out.println(response.getStatusCode() + "hasilnya: " + response.asString());

    }

    @Test
    void getPrice(){
        response = reqFakeStore
                .when()
                .get("products");

//  library Awaitility untuk menunggu sampai respon path price tidak kosong
        await()
                .atMost(5, TimeUnit.SECONDS) //waktu tunggu selama 5 detik
                .pollInterval(1, TimeUnit.SECONDS) //cek setiap 1 detik
                .until(() -> { //lambda expression kode akan mencari atau menunggu elemen sampai ada

//  List attribute price akan merespon data json pada dan ekstrak data dari json pada data kunci price dan data
//  path "price" merupakan kata kunci (tidak tahu jenis tipe data) maka di meminta json untuk menapilkan ke
//  tipe data double (bukan konversi).
                    List<Double> price = response.jsonPath().getList("price", double.class);
//  kembalikan ke objek price dicek apakah ada kata kunci price dan di dalam kunci apakah nilainya
                    return price != null && !price.isEmpty();
                });

        List<Double> price = response.jsonPath().getList("price", double.class);
        System.out.println("price: ");
        addNumber = 1;
        if (price != null)
        for (Double Price : price){
        System.out.println(addNumber + ". " + Price);
        addNumber++;
        } else {
            System.out.println("data tidak ditemukan");
        }
    }

    @Test
    void deleteUser(){
        response = reqFakeStore
                .when()
                .delete("products/21");

        System.out.println("hasilnya: " + response.asString());

    }
//    public void getCity() throws InterruptedException {
//        Thread.sleep(3000);
//
//
//        Response response = RestAssured
//                .given()
//                .when()
//                .get("users")
//                ;
//
//        List<String> cities = response.jsonPath().getList("address.city");
//        System.out.println("city: ");
////  untuk memberikan nomor urut dan dimulai dengan angka 1
//        addNumber = 1;
////  loop ini untuk setiap elemen
//        if (cities != null)
//        for (String City : cities){
//            System.out.println(addNumber + ". " + City + " city");
//            addNumber++; //angka 1 akan berlanjut sampai berbapun sebanyak data array
//        } else {
//            System.out.println("\ndata tidak ditemukan atau format salah");
//        }
//        System.out.println("Thread ID: " + Thread.currentThread().getId());
//    }
}
