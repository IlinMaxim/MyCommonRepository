import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static io.restassured.RestAssured.given;

public class New {
    ArrayList<String> users = new ArrayList<>();
    String name = "Maxim";
    String job = "QA master";
    String user1 = "george.bluth@reqres.in";
    String user2 = "emma.wong@reqres.in";

    @Test
    public void taskOne() throws JsonProcessingException {
        Response response = given()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .assertThat().statusCode(200)
                .contentType(ContentType.JSON).extract().response();

        String jsonString = response.getBody().asString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonString);
        JsonNode childNode = rootNode.get("data");
        Iterator<JsonNode> rootElements = childNode.elements();

        //Проверяем кол-во пользователей
        Assertions.assertEquals(childNode.size(), 6);

        //Поиск пользователей
        for (JsonNode element : childNode) {
            users.add(element.get("email").asText());
        }
        Assertions.assertTrue(users.contains(user1), "Пользователь " + user1 + " не найден");
        Assertions.assertTrue(users.contains(user2), "Пользователь " + user2 + " не найден");
    }

    @Test
    public void taskTwo() throws ParseException {

        RequestSpecification request = given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", name);
        requestParams.put("job", job);

        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());
        Response response = request.post("https://reqres.in/api/users");

        //Вывожу id в консоль
        String someValue = response.jsonPath().get("id").toString();
        System.out.println(someValue);

        //Парсю дату как умею
        String creationDate = response.jsonPath().get("createdAt").toString();
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        creationDate = creationDate.substring(0, creationDate.length() - 14);
        Date date = oldDateFormat.parse(creationDate);
        String correctDate = newDateFormat.format(date);

        //Вывожу дату в консоль
        System.out.println(correctDate);


    }

}

