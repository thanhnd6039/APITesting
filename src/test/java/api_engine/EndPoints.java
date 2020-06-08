package api_engine;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.requests.AddBooksRequest;
import model.requests.AuthorizationRequest;
import model.requests.RemoveBookRequest;
import model.response.Books;
import model.response.Token;
import model.response.UserAccount;

public class EndPoints {
    private static final String BASE_URL = "https://bookstore.toolsqa.com";

    public static IRestResponse<Token> authenticateUser(AuthorizationRequest authRequest){
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        Response response = request.body(authRequest).post(Route.generateToken());
        return new RestResponse(Token.class, response);
    }

    public static IRestResponse<Books> getBooks(){
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        Response response = request.get(Route.books());
        return new RestResponse(Books.class, response);
    }

    public static IRestResponse<UserAccount> addBook(AddBooksRequest addBooksRequest, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        Response response = request.body(addBooksRequest).post(Route.books());
        return new RestResponse(UserAccount.class, response);
    }

    public static Response removeBook(RemoveBookRequest removeBookRequest, String token) {

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        Response response = request.body(removeBookRequest).delete(Route.book());
        return response;
    }

    public static IRestResponse<UserAccount> getUserAccount(String userId, String token) {

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        Response response = request.get(Route.userAccount(userId));
        return new RestResponse(UserAccount.class, response);
    }
}
