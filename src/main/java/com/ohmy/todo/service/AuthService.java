package com.ohmy.todo.service;

public interface AuthService {
    // Using boolean as a stand in, we'll have a different class for auth, but not yet.
    public boolean login();
    public boolean logout();
}
