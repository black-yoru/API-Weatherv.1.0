package apiWeather.negative;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.consumer.RecordedStackTrace;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class WeatherCurrentNeg {
// buat attribute yang berisikan untuk key di file option.properties untuk Link, token dan akan akses file tsb
    private String URL;
    private String TOKEN;

// tujuannya untuk RestAssured dan BASEURI secara private dan hanya dikelas ini untuk less error
    private static RequestSpecification reqWeatherCurrentNeg;

//untuk merespon API dan API memberikan balasannya
    io.restassured.response.Response response;

    @BeforeMethod
    void setUp() throws IOException {

//  inisiasi lokasi file yang berisikan link API dan token ini hanya sebatas load belum akses isi file
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/option.properties"));

        URL = properties.getProperty("base.url"); //akses link tinggal panggil attribute URL
        TOKEN = properties.getProperty("api.key"); //akses token tinggal panggil attribute TOKEN

//  inisasi kode untuk RestAssured dan Base.URI
        reqWeatherCurrentNeg = RestAssured
                .given()
                .baseUri(URL) //.baseUri tujuannya tidak perlu nulis ulang endpoint di @Test
                .log().all(); //dua pasangan ini untuk mencetak hasil permintaan atau selama proses eksekusi
    }

// negatif test kota Jakarta menjadi Jakarte

    @DataProvider(name = "cities")
    public Object [][] city(){
        return new Object[][]{
                {"Paris"},
                {"Jakarte"}
        };
    }

    @Test
    void getRequestNeg(){
        response = reqWeatherCurrentNeg
                .given()
                .queryParam("q", "Jakarte")
                .queryParam("appid", TOKEN)
                .when()
                .get("/weather")
                .then()
                .statusCode(404)
                .extract()
                .response();

        System.out.println("hasilnya: " + response.asString());

    }

    @Test
    void getApiNeg(){
  /*scenario test token API;
  * actual: a931a5f6710b91e8bba3e61182e88729
  * negative: a931a5f6710b91e8bba3e61182e8872*/
        response = reqWeatherCurrentNeg
                .queryParam("q", "London")
                .queryParam("appid", "a931a5f6710b91e8bba3e61182e8872")
                .when()
                .get("/weather")
                .then()
                .statusCode(401)
                .extract()
                .response();
        System.out.println("hasilnya: " + response.asString());
    }


    @Test
    void getBadRequest(){
        /*scenario test token API;
         * actual: a931a5f6710b91e8bba3e61182e88729
         * negative: a931a5f6710b91e8bba3e61182e8872*/
        response = reqWeatherCurrentNeg
                .queryParam("q", "London")
                .queryParam("appid", "")
                .when()
                .get("/weather")
                .then()
                .statusCode(401)
                .extract()
                .response();
        System.out.println("hasilnya: " + response.asString());
    }

    @Test
    void getBoundaryValues(){
/*  negative testnya huruf abjad mandarin artinya kota Tianjin dalam bahasa latin*/
        String City = "天津";

        response = reqWeatherCurrentNeg
                .given()
                .queryParam("q", City)
                .queryParam("appid", "a931a5f6710b91e8bba3e61182e88729")
                .when()
                .get("/weather")
                .then()
                .statusCode(404)
                .extract()
                .response();
        System.out.println("hasilnya: " + response.asString());


        String resCity = response.path("Tianjin");
//        Assert.assertEquals(resCity, City, "天津 karakter abjad china tidak sesuai dan seharusnya Tianjin");

        if (City == resCity) {
        System.out.println("nama kota sesuai");
        } else {
        System.out.println("berbeda abjad dan tidak bisa termasuk kategori boundary values");
        }
    }
}

