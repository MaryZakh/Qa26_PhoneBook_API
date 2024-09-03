package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDTO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RegistrationTestsRA {

    String endpoint = "user/registration/usernamepassword";


    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }


    @Test
    public void registrationSuccess() {
        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder().username("don" + i + "@gmail.com").password("Ddon12345$").build();

        String token = given()
                .body(authRequestDTO)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("token");
        System.out.println(token);
    }

    @Test
    public void registrationWrongEmail() {
        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder().username("dongmail.com").password("Ddon12345$").build();
        given()
                .body(authRequestDTO)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.username", containsString("must be a well-formed email address"));


    }

    @Test
    public void registrationWrongPassword() {
        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder().username("don@gmail.com").password("Ddon12").build();
        given()
                .body(authRequestDTO)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.password", containsString("At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number"));
    }

    @Test
    public void registrationDuplicate(){
        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder().username("mara@gmail.com").password("Mmar12345$").build();
        given()
                .body(authRequestDTO)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(409)
                .assertThat().body("message", containsString("User already exists"));


    }
}
