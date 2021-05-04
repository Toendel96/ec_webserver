package no.dnb.reskill.ec_webserver.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "User")
public class User {
    private Long id;
    private String username;
    private String password;
    private String user_type;
    private String token;

    public User(Long id, String username, String password, String user_type) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.user_type = user_type;
    }

    @DynamoDBHashKey(attributeName="id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @DynamoDBAttribute
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBAttribute
    public String getUser_type() {
        return user_type;
    }
    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @DynamoDBIgnore
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
