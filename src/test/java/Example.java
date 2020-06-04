import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Example {

    @Test
    public void getWeatherDetails(){
        RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";

        RequestSpecification httpRequest = RestAssured.given();

        Response response = httpRequest.request(Method.GET, "/Hyderabad");

        JsonPath jsonPathEvaluator = response.jsonPath();
        String city = jsonPathEvaluator.get("WindSpeed");
        System.out.println("City: "+city);


    }
}
