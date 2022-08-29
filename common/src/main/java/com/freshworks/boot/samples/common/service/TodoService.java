package com.freshworks.boot.samples.common.service;

import java.util.List;

import com.freshworks.boot.common.context.account.AccountContext;
import com.freshworks.boot.common.persistence.NotFoundException;
import com.freshworks.boot.samples.common.model.Account;
import com.freshworks.boot.samples.common.model.Todo;
import com.freshworks.boot.samples.common.repository.TodoRepository;
import org.springframework.stereotype.Component;

@Component
public class TodoService {
    private final TodoRepository todoRepository;
    private AccountContext<Account> accountContext;

    public TodoService(TodoRepository todoRepository, AccountContext<Account> accountContext) {
        this.todoRepository = todoRepository;
        this.accountContext = accountContext;
    }

    public List<Todo> listAllTodos() {
        return todoRepository.findByAccountId(getAccountID());
    }

    public Todo addTodo(Todo todo) {
        todo.setAccountId(getAccountID());
        return todoRepository.save(todo);
    }

    public Todo getTodo(long todoId) {
        return todoRepository.findByAccountIdAndId(getAccountID(), todoId).orElseThrow(
                () -> new NotFoundException("todo_id"));
    }

    public Todo updateTodo(Todo updateTodo) {
        getTodo(updateTodo.getId());
        updateTodo.setAccountId(getAccountID());
        return todoRepository.save(updateTodo);
    }

    public void deleteTodo(long todoId) {
        Todo todo = getTodo(todoId);
        todoRepository.delete(todo);
    }

    private long getAccountID() {
        return accountContext.get().getAccountId();
    }
}
