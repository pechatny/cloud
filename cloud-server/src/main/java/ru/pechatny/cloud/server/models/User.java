package ru.pechatny.cloud.server.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "cloud")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SYSTEM_SEQUENCE_13452776_E4D7_42D4_A0F6_27F103787FF1")
    private long id;

    public String login;

    public String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
