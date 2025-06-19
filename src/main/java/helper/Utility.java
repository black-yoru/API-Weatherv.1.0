package helper;



import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class Utility {

    private final RequestSpecification requestSpecification;

    public Utility(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }




// Helper untuk getDataProduct
    public Response getData(){
        return requestSpecification
                .when()
                .get("/products");
    }



// Helper untuk getData secara spesifik id dengan parameter
    public Response getDataById(int id) {

        return requestSpecification //method di dalam class dapat digunakan di luar class dengan return
                .when()
                .get("/products/" + id);

    }


// Helper untuk create data
    public Response createProduct(Map<String, Object> payload){
        io.restassured.response.Response response;
        return requestSpecification
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/products/");
    }


// Helper untuk Update Data
    public Response updateData(int id, Map<String, Object> payload){
        return requestSpecification
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .put("/products/" + id);
    }



    public Response getBulk(int[] ids){
        return requestSpecification
                .when()
                .get("/products/" + ids);
    }
}
