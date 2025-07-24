package com.ohmy.todo.service;

import com.ohmy.todo.model.User;

public interface UserService {
    public User add();
    public User get(long id);
    public User getAll();
    public boolean delete();
}
