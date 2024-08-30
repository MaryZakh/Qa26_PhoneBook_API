package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.MessageDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIdOkhttp {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyYUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcyNTYwMjY1NSwiaWF0IjoxNzI1MDAyNjU1fQ.axa4WEVBc3oqcNQ08okzfy2ZUgf20gYkcFkxXWF3lpg";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    String id;

    @BeforeMethod
    public void preCondition() throws IOException {
        //create contact
        int i = new Random().nextInt(1000) + 1000;

        ContactDTO contactDTO = ContactDTO.builder()
                .name("Maya")
                .lastName("Dow")
                .email("maya" + i + "@gmail.com")
                .phone("12345645" + i)
                .address("Rehovot")
                .description("Bee")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contactDTO), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),200);
        MessageDTO messageDTO = gson.fromJson(response.body().string(), MessageDTO.class);
        String message = messageDTO.getMessage();//"Contact was added! ID: e91db8af-c8a7-4f1d-92b0-a7fa0b164f19"
        //get id from  "message": "Contact was added! ID: e91db8af-c8a7-4f1d-92b0-a7fa0b164f19"
        String[] all = message.split(": ");
        //id
        id=all[1];
        System.out.println(id);


    }


    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 200);

        MessageDTO dto = gson.fromJson(response.body().string(), MessageDTO.class);
        System.out.println(dto.getMessage());
        Assert.assertEquals(dto.getMessage(), "Contact was deleted!");
    }

    @Test
    public void deleteContactByIdWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/2ada22f9-3dd8-449b-9f7b-6df81db505f1")
                .delete()
                .addHeader("Authorization", "ghjsdf")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);

        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Unauthorized");
    }

    @Test
    public void deleteContactByIdNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+123)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);

        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        System.out.println(errorDTO.getMessage());
        Assert.assertEquals(errorDTO.getMessage(), "Contact with id: 123 not found in your contacts!");
    }

}


//8df59d47-fcae-455a-88ae-97f2355e72f2
//a@a
//=========================
//d31e8659-b040-4cea-9f90-7b858ffe4f5f
//a@a
//=========================
//        2ada22f9-3dd8-449b-9f7b-6df81db505f1
//d@f
//=========================