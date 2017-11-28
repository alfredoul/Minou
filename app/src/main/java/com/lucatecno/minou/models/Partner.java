package com.lucatecno.minou.models;

/**
 * Created by alfredoul on 21/11/17.
 */

public class Partner {

    private String id;
    private String email;
    private Long timestamp;

    public Partner(String uid) {
        this.id = id;
    }

    public Partner(String id, String email, Long timestamp) {
        this.id = id;
        this.email = email;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
