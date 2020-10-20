import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class DataBaseInteraction {
    Connection conn = null;
    Statement stmt = null;

    SoftAssertions softAssert = new SoftAssertions();
    static final String DB_URL = "jdbc:mysql://192.168.14.73:3306/challange";
    static final String USER = "root";
    static final String PASS = "root";

    static final String CREATE_TABLE = " CREATE TABLE if not exists milin (\n" +
            "id int,\n" +
            "name varchar(255),\n" +
            "surname varchar(255),\n" +
            "age int,\n" +
            "birthdate date\n" +
            ");";

    static final String JSON_PATH = "C:\\Work\\autotest\\CinimexAutoTests\\src\\main\\resources\\DataBasePersons.json";
    static final String SELECT = "SELECT * FROM milin";

    ArrayList<Person> personsList = new ArrayList<>();

    @Test
    public void lllll() throws IOException, SQLException {
        dataBaseConnection(DB_URL, USER, PASS);
        createTable(CREATE_TABLE);
        readPersons(JSON_PATH);
        getResults(SELECT);
        compareAttributes(personsList.get(0),personsList.get(1));
        softAssert.assertAll();
    }


    public void dataBaseConnection(String url, String user, String pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void createTable(String sqlCreateTable) throws SQLException {
        stmt.execute(sqlCreateTable);
    }

    public void readPersons(String jsonPath) throws SQLException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new FileInputStream(new File(jsonPath)));
        JsonNode childNode = rootNode.get("data");
        Iterator<JsonNode> rootElements = childNode.elements();

       while (rootElements.hasNext()){
           ArrayList<String> keys = new ArrayList<>();
           ArrayList<JsonNode> values = new ArrayList<>();
           JsonNode ul = rootElements.next();
           Iterator<Map.Entry<String, JsonNode>> fields = ul.fields();
           while (fields.hasNext()) {
               Map.Entry<String, JsonNode> entry = fields.next();
               keys.add(entry.getKey());
               values.add(entry.getValue());
           }
           insertPersons(keys,values);
       }
    }
    public void insertPersons(ArrayList keys, ArrayList values) throws SQLException {
        String insertStatement = "INSERT INTO milin (";
        for(int i = 0; i < keys.size()-1; i++){
            insertStatement = insertStatement + keys.get(i) + ",";
        }
        insertStatement = insertStatement + keys.get(keys.size()-1);
        insertStatement = insertStatement + ") VALUES (";
        for (int i = 0; i < values.size()-1; i++){
            insertStatement = insertStatement + values.get(i) + ",";
        }
        insertStatement = insertStatement + values.get(values.size()-1);
        insertStatement =  insertStatement + ")";
        stmt.execute(insertStatement);
    }

    public void getResults(String sqlSelect) throws SQLException, IOException {
        ResultSet rs = stmt.executeQuery(sqlSelect);

        while (rs.next()) {
            Person myPerson = new Person();
            Integer id = rs.getInt(1);
            String name = rs.getString(2);
            String surname = rs.getString(3);
            Integer age = rs.getInt(4);
            String birthdate = rs.getString(5);

            myPerson.setId(id);
            myPerson.setName(name);
            myPerson.setSurname(surname);
            myPerson.setAge(age);
            myPerson.setBirthdate(birthdate);
            personsList.add(myPerson);

        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("target/persons1.json"), personsList);

        rs.close();
        stmt.close();
        conn.close();
    }
    public void compareAttributes(Person person1, Person person2) {

        softAssert.assertThat(person1.getId()).as("ID совпадает")
                .isNotEqualTo(person2.getId());

        softAssert.assertThat(person1.getName()).as("Name совпадает")
                .isNotEqualTo(person2.getName());

        softAssert.assertThat(person1.getSurname()).as("Surname совпадает")
                .isNotEqualTo(person2.getSurname());

        softAssert.assertThat(person1.getAge()).as("Age совпадает")
                .isNotEqualTo(person2.getAge());

        softAssert.assertThat(person1.getBirthdate()).as("Birthdate совпадает")
                .isNotEqualTo(person2.getBirthdate());

    }
}
