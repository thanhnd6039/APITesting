package step_definition;



import api_engine.EndPoints;
import api_engine.IRestResponse;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.requests.AddBooksRequest;
import model.requests.AuthorizationRequest;
import model.requests.ISBN;
import model.requests.RemoveBookRequest;
import model.response.Book;
import model.response.Books;
import model.response.Token;
import model.response.UserAccount;
import org.testng.Assert;



public class Steps {
    private static final String USER_ID = "cbab1f71-0b89-4f1c-a463-c9ee14c762cc";
    private static final String USERNAME = "Tester";
    private static final String PASSWORD = "Test@123456";



    private static Response response;
    private static Token tokenResponse;
    private static Book book;
    private static IRestResponse<UserAccount> userAccountResponse;

    @Given("^I am an authorized user$")
    public void i_am_an_authorized_user(){
        AuthorizationRequest authRequest = new AuthorizationRequest(USERNAME, PASSWORD);
        tokenResponse = EndPoints.authenticateUser(authRequest).getBody();
    }

    @Given("^A list of books are available$")
    public void a_list_of_books_are_available(){
        IRestResponse<Books> booksResponse = EndPoints.getBooks();
        book = booksResponse.getBody().books.get(0);
    }

    @When("^I add a book to my reading list$")
    public void i_add_a_book_to_my_reading_list(){
        ISBN isbn = new ISBN(book.isbn);
        AddBooksRequest addBooksRequest = new AddBooksRequest(USER_ID, isbn);
        userAccountResponse = EndPoints.addBook(addBooksRequest, tokenResponse.token);
    }

    @Then("^The book is added$")
    public void the_book_is_added(){
        Assert.assertTrue(userAccountResponse.isSuccessful());
        Assert.assertEquals(userAccountResponse.getStatusCode(), 201);
        Assert.assertEquals(userAccountResponse.getBody().userID, USER_ID);
        Assert.assertEquals(userAccountResponse.getBody().books.get(0).isbn, book.isbn);
    }

    @When("^I remove a book from my reading list$")
    public void i_remove_a_book_from_my_reading_list(){
        RemoveBookRequest removeBookRequest = new RemoveBookRequest(book.isbn, USER_ID);
        response = EndPoints.removeBook(removeBookRequest, tokenResponse.token);
    }

    @Then("^The book is removed$")
    public void the_book_is_removed(){
        Assert.assertEquals(response.getStatusCode(), 204);
        userAccountResponse = EndPoints.getUserAccount(USER_ID, tokenResponse.token);
        Assert.assertEquals(userAccountResponse.getStatusCode(), 200);
        Assert.assertEquals(userAccountResponse.getBody().books.size(), 0);
    }
}
