package api_engine;

import io.restassured.response.Response;

public class RestResponse<T> implements IRestResponse<T> {

    private Response response;
    private T data;
    private Exception e;

    public RestResponse(Class<T> t, Response response){
        this.response = response;
        try{
            this.data = t.newInstance();
        }catch (Exception e){
            throw new RuntimeException("There should be a default constructor in the Response POJO");
        }
    }
    @Override
    public T getBody() {
        try {
            data = (T) response.getBody().as(data.getClass());
        }catch (Exception e) {
            this.e=e;
        }
        return data;
    }

    @Override
    public String getContent() {
        return response.getBody().asString();
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public boolean isSuccessful() {
        int code = response.getStatusCode();
        if( code == 200 || code == 201 || code == 202 || code == 203 || code == 204 || code == 205)
            return true;
        return false;
    }

    @Override
    public String getStatusDescription() {
        return response.getStatusLine();
    }

    @Override
    public Response getResponse() {
        return response;
    }

    @Override
    public Exception getException() {
        return e;
    }
}
