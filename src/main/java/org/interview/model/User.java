package org.interview.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by sony on 4/13/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty
    private BigInteger id;

    @JsonProperty("created_at")
    private Date created;

    @JsonProperty
    private String name;

    @JsonProperty("screen_name")
    private String screenName;

    public BigInteger getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (created != null ? !created.equals(user.created) : user.created != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        return screenName != null ? screenName.equals(user.screenName) : user.screenName == null;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", created=" + created +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                '}';
    }
}
