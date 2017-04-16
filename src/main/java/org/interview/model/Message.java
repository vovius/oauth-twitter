package org.interview.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Volodymyr_Arseienko on 12.04.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    @JsonProperty
    private BigInteger id;

    @JsonProperty("created_at")
    private Date creationDate;

    @JsonProperty
    private String text;

    @JsonProperty(value = "user")
    private User author;


    public BigInteger getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getText() {
        return text;
    }

    public User getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!id.equals(message.id)) return false;
        if (creationDate != null ? !creationDate.equals(message.creationDate) : message.creationDate != null)
            return false;
        if (text != null ? !text.equals(message.text) : message.text != null) return false;
        return author != null ? author.equals(message.author) : message.author == null;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", text='" + text + '\'' +
                ", author=" + author +
                '}';
    }
}
