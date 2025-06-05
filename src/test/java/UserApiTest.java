import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.com.utilities.Utility;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;


public class UserApiTest {

    private static String BASE_URL;
    private static String API_KEY;



    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        Thread.sleep(3000);
//  properties digunakan untuk menyimpan informasi tanpa perlu merubah kode atau compile
//  dengan objek = objek harus string, berpasangan

        Properties props = new Properties();
        props.load(new FileInputStream("src/main/resources/option.properties")); //file yang dituju menyimpan string

//  attribute base_url memiliki objek file config.properties
        BASE_URL = props.getProperty("base.url");
        API_KEY = props.getProperty("api.key");

//
        RestAssured.baseURI = BASE_URL;
    }

    @DataProvider(name = "cities")
    public Object[][] cities() {
        return new Object[][]{
                {"paris"},
                {"Jakarta,ID"},
                {"Bandung,ID"},
                {"Manchester,GB"}
        };
    }

    @Test(dataProvider = "cities")
    public void testCurrentWeather(String city) throws InterruptedException {
        RestAssured.baseURI = BASE_URL;
        Response response = given()
                .queryParam("q", city)
                .queryParam("appid", API_KEY)
                .when()
                .get("/weather")
                .then()
                .statusCode(200)
                .extract().response();

//  ini adalah untuk mengambil data JSON body secara spesifik
        float temperature = response.path("main.temp");
        String weather = response.path("weather[0].main");
//        JsonPath json = response.jsonPath();
        String id = response.path("sys.country");
        float lon = response.path("coord.lon");
        float lat = response.path("coord.lat");

//        if (id != null){
//            int ID = id.intValue();
//            System.out.println("ID :" + ID);
//        }else {
//            System.out.println("tidak ditemuka ID kota " + city);
//        }





        System.out.println("\nresponse: " + response.statusCode() + "\nCity: " + city +  "\nWeather: " + weather + "\nid: " + id +
                "\nlongtitude : " + lon + "\nlatitude: "+ lat + "\n");

        System.out.println("Body: " + response.asString());
        System.out.println("Thread ID: " + Thread.currentThread().getId());



//        String description = response.path("weather[0].description");
//        System.out.println("\ndescription: " + description);

        Assert.assertNotNull(id, "Negara tidak boleh null");
        Assert.assertNotNull(weather, "Deskripsi cuaca tidak boleh kosong");
//        Assert.assertTrue(lon < -0000.0000  && lat > -0000.0000 , "number coordinate");

    }
}
