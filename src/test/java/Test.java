import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        String userID = "cbab1f71-0b89-4f1c-a463-c9ee14c762cc";
        String userName = "Tester";
        String password = "Test@123456";
        String baseUrl = "https://bookstore.toolsqa.com";

        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        //Step 1: Generate token
        JSONObject requestParams = new JSONObject();
        requestParams.put("userName", userName);
        requestParams.put("password", password);

        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        Response response = request.post("/Account/v1/GenerateToken");
        Assert.assertEquals(response.getStatusCode(), 200);
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("token"));
        String token = JsonPath.from(responseBody).get("token");
        System.out.println("token: "+token);


        //Step 2 Get the books from library
        response = request.get("/BookStore/v1/Books");
        Assert.assertEquals(response.getStatusCode(), 200);
        responseBody = response.getBody().asString();
        List<Map<String, String>> books = JsonPath.from(responseBody).get("books");
        Assert.assertTrue(books.size() > 0);
        String bookId = books.get(0).get("isbn");

        //Step 3 Add a book from the list to the user
//        requestParams.clear();
//        JSONArray collectionOfIsbns = new JSONArray();
//        JSONObject isbnItem = new JSONObject();
//        isbnItem.put("isbn", bookId);
//        collectionOfIsbns.add(isbnItem);
//        requestParams.put("userId", userID);
//        requestParams.put("collectionOfIsbns", collectionOfIsbns);
//
//
//        request.header("Authorization", "Bearer " + token)
//                .header("Content-Type", "application/json");
//
//        request.body(requestParams.toJSONString());
//        response = request.post("/BookStore/v1/Books");
//        Assert.assertEquals(response.getStatusCode(), 201);

        //Step 4 Delete a book
        requestParams.clear();
        requestParams.put("isbn", bookId);
        requestParams.put("userId", userID);

        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        request.body(requestParams.toJSONString());
        response = request.delete("/BookStore/v1/Book");
        Assert.assertEquals(response.getStatusCode(), 204);

        //Step 5 Confirm if the book is removed
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        response = request.get("/Account/v1/User/"+userID);
        responseBody = response.getBody().asString();
        Assert.assertEquals(response.getStatusCode(), 200);
        List<Map<String, String>> booksOfUser = JsonPath.from(responseBody).get("books");
        Assert.assertEquals(booksOfUser.size(), 0);








    }
}
