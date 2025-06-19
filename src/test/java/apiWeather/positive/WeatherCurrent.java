package apiWeather.positive;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;


public class WeatherCurrent {

    private String BASE_URL;
    private String API_KEY;
    private static RequestSpecification reqWeather;

    private io.restassured.response.Response response;

    private int addNumber;
    private String City = "New York";

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
//  properties digunakan untuk menyimpan informasi tanpa perlu merubah kode atau compile
//  dengan objek = objek harus string, berpasangan

        Properties props = new Properties();
        props.load(new FileInputStream("src/main/resources/option.properties")); //file yang dituju menyimpan string

//  attribute base_url memiliki objek file config.properties
        BASE_URL = props.getProperty("base.url");
        API_KEY = props.getProperty("api.key");

//di meghod ini akan memanggil link API secara private di class ini dan tidak akan menggangu class lain
        reqWeather = RestAssured
                .given()
                .baseUri(BASE_URL)
                .log().all();
    }


//  posditi test: connect link API
    @Test
    void testConnectApi(){
        response = reqWeather
                .given()
                .get("https://api.openweathermap.org/")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("connect successful: " + response.asString());

    }

    @DataProvider(name = "cities")
    public Object[][] cities() {
        return new Object[][]{
                {"Paris, FR"},
                {"Jakarta,ID"},
                {"Bandung,ID"},
                {"Manchester,GB"}
        };
    }

    @Test(dataProvider = "cities")
    public void testCurrentWeather(String city) throws InterruptedException {

        await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    response = reqWeather
                            .queryParam("q", city)
                            .queryParam("appid", API_KEY)
                            .when()
                            .get("/weather")
                            .then()
                            .statusCode(200)
                            .extract().response();

//  ini adalah untuk mengambil data JSON body secara spesifik
//                    float temperature = response.path("main.temp");
//  inisiasi json path untuk memudahkan validasi
                    JsonPath json = response.jsonPath();
                    String weather = json.getString("weather[0].main");
                    if (weather == null) {
                        System.out.println("data tidak ditemukan");
                    }

                    String id = json.getString("sys.country");
                    double lon = json.getDouble("coord.lon");
                    double lat = json.getDouble("coord.lat");


                    System.out.println("\nresponse: " + response.statusCode() + "\nCity: " + city +  "\nWeather: " + weather + "\nid: " + id +
                            "\nlongtitude : " + lon + "\nlatitude: "+ lat + "\n");

                    System.out.println("full JSON Body: \n" + response.asString());
                    System.out.println("Thread ID: " + id);
                    System.out.println("Debug Weather: " + weather);


                    if (id == null || weather == null) {
                        System.out.println("salah data null: " + id + ", Weather: " + weather);
                    }



//        String description = response.path("weather[0].description");
//        System.out.println("\ndescription: " + description);

                    Assert.assertNotNull(id, "Negara tidak boleh null");
                    Assert.assertNotNull(weather, "Deskripsi cuaca tidak boleh kosong");
//        Assert.assertTrue(lon < -0000.0000  && lat > -0000.0000 , "number coordinate");;

                    String description = response.path("weather[0].description");
                    System.out.println("\ndescription: " + description);

                });


//        Response response = given()
//                .queryParam("q", city)
//                .queryParam("appid", API_KEY)
//                .when()
//                .get("/weather")
//                .then()
//                .statusCode(200)
//                .extract().response();

//  ini adalah untuk mengambil data JSON body secara spesifik
//        float temperature = response.path("main.temp");
//        String weather = response.path("weather[0].main");
//        JsonPath json = response.jsonPath();
//        String id = response.path("sys.country");
//        float lon = response.path("coord.lon");
//        float lat = response.path("coord.lat");

//        if (id != null){
//            int ID = id.intValue();
//            System.out.println("ID :" + ID);
//        }else {
//            System.out.println("tidak ditemuka ID kota " + city);
//        }

//        System.out.println("\nresponse: " + response.statusCode() + "\nCity: " + city +  "\nWeather: " + weather + "\nid: " + id +
//                "\nlongtitude : " + lon + "\nlatitude: "+ lat + "\n");
//
//        System.out.println("Body: " + response.asString());
//        System.out.println("Thread ID: " + Thread.currentThread().getId());



//        String description = response.path("weather[0].description");
//        System.out.println("\ndescription: " + description);

//        Assert.assertNotNull(id, "Negara tidak boleh null");
//        Assert.assertNotNull(weather, "Deskripsi cuaca tidak boleh kosong");
//        Assert.assertTrue(lon < -0000.0000  && lat > -0000.0000 , "number coordinate");

    }



    @DataProvider(name = "idProvider")
    public Object[][] providerID(){
        return new Object[][] {
                {"5128581"}
        };
    }

    @Test(dataProvider = "idProvider")
    void getID(String iD){
        response = reqWeather
                .queryParam("id" , iD)
                .queryParam("appid", API_KEY)
                .when()
                .get("/weather")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("id-nya adalah: " + iD);

//  id tipe intger di convert ke String menggunakan parse
        String responseId = response.path("id").toString();
        Assert.assertEquals(responseId, iD, "ID pada respons tidak cocok");

    }

    @Test
    public void timeZone() throws InterruptedException {

        io.restassured.response.Response response = reqWeather
                .queryParams("q", "New York")
                .queryParam("appid", API_KEY)
                .when()
                .get("/weather")
                ;

        JsonPath json = response.jsonPath();
        String respondTimeZone = json.getString("timezone");
        System.out.println("timezone: " + respondTimeZone + " New Yor City");


//        List<String> zipCode = response.jsonPath().getList("address.zipcode");
//        System.out.println("\nzipcode: ");
//
//        addNumber = 1;
//        if (zipCode != null)
//        for (String zip : zipCode){
//            System.out.println(addNumber + ". " + zip);
//            addNumber++;
//            System.out.println("Thread ID: " + Thread.currentThread().getId());
//        } else {
//            System.out.println("data tidak ditemukan");
//        }
    }

    @AfterClass
    void tearDown() {
        System.out.println("Test finished");
        RestAssured.reset();
    }


}
