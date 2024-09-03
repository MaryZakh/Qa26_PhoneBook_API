package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDTO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class UpdateExistsContactRA {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyYUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcyNTk4MDgyMywiaWF0IjoxNzI1MzgwODIzfQ.l1qyipzvwTOQI1ci8vg1IAbp1DqI3ZifvZNz1L5nuuY";


    ContactDTO contactDTO = ContactDTO.builder()
            .name("Donna")
            .lastName("Doww")
            .email("donna@gmail.com")
            .phone("12345665555")
            .address("Tel Aviv")
            .description("Donna")
            .build();

    String id;


    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

       String message =  given()
                .header("Authorization",token)
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");
        System.out.println(message);
        String[]all = message.split(": ");
        id = all[1];
    }


    @Test
    public void updateExistsContactSuccess(){
        String name = contactDTO.getName();
        contactDTO.setId(id);
        contactDTO.setName("wwwwww");


        given()
                .body(contactDTO)
                .header("Authorization",token)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", containsString("Contact was updated"));
    }

}
