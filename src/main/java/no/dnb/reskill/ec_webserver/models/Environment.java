package no.dnb.reskill.ec_webserver.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Environment")
public class Environment {
    private Long id;
    private String short_name;
    private String description;

    @DynamoDBHashKey(attributeName="id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @DynamoDBAttribute
    public String getShort_name() {
        return short_name;
    }
    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Environment) {
            Environment e = (Environment) other;
            result = this.id == e.id;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }
}
