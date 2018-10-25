package ru.pechatny.cloud.server.dao;

import ru.pechatny.cloud.server.models.User;

import java.util.Optional;

public interface UserDao {
    public User get(int id);

    public Optional get(String id);

    public boolean save(String login, String password);
}
