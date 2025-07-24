package com.ohmy.todo.service;

import com.ohmy.todo.model.Todo;

import java.util.List;

public interface TodoService {
    public Todo add();
    public Todo get(long id);
    public List<Todo> getAll();
    public boolean update();
    public boolean delete();
}