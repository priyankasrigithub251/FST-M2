package LiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Headers
    Map<String, String> reqHeaders  = new HashMap<>();
    //Resource path
    String resourcepath = "/api/users";
    //create the contract
    @Pact(consumer = "UserConsumer" , provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        //set the headers
        reqHeaders.put("Content-Type", "application/json");

        //create the JSNON body
        DslPart reqResBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");

        return builder.given("Request to create a User")
                .uponReceiving("Request to create a user")
                .method("POST")
                .headers(reqHeaders)
                .path(resourcepath)
                .body(reqResBody)
                .willRespondWith()
                .status(201)
                .body(reqResBody)
                .toPact();
    }
    @Test
    @PactTestFor(providerName = "UserProvider", port="8282")
    public void consumerSideTest() {
        //set baseURI
        String baseURI = "http://localhost:8282";

        //RequestBody
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 123);
        reqBody.put("firstName", "Priyanka");
        reqBody.put("lastName", "Srivastava");
        reqBody.put("email", "priyankasrivastava@test.com");

        //Generate Response
        given().headers(reqHeaders).body(reqBody).    //Request specification
                when().post(baseURI + resourcepath).   //post request
                then().log().all().statusCode(201);  //Assertions

    }

}
