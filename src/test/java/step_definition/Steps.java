package step_definition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;

public class Steps {
    private static final String USER_ID = "cbab1f71-0b89-4f1c-a463-c9ee14c762cc";
    private static final String USERNAME = "Tester";
    private static final String PASSWORD = "Test@123456";
    private static final String BASE_URL = "https://bookstore.toolsqa.com";

    private static String token;
    private static Response response;
    private static String responseBody;
    private static String bookId;

    @Given("^I am an authorized user$")
    public void i_am_an_authorized_user(){
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("userName", USERNAME);
        requestParams.put("password", PASSWORD);

        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        response = request.post("/Account/v1/GenerateToken");
        responseBody = response.getBody().asString();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(responseBody.contains("token"));
        token = JsonPath.from(responseBody).get("token");
    }

    @Given("^A list of books are available$")
    public void a_list_of_books_are_available(){

    }

    @When("^I add a book to my reading list$")
    public void i_add_a_book_to_my_reading_list(){

    }

    @Then("^The book is added$")
    public void the_book_is_added(){

    }

    @When("^I remove a book from my reading list$")
    public void i_remove_a_book_from_my_reading_list(){

    }

    @Then("^The book is removed$")
    public void the_book_is_removed(){

    }
}
