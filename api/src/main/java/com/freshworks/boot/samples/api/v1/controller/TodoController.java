package com.freshworks.boot.samples.api.v1.controller;

import java.util.List;
import javax.validation.Valid;

import com.freshworks.boot.samples.api.v1.dto.MetaDto;
import com.freshworks.boot.samples.api.v1.dto.TodoCreateDto;
import com.freshworks.boot.samples.api.v1.dto.TodoDto;
import com.freshworks.boot.samples.api.v1.dto.TodoListResponseDto;
import com.freshworks.boot.samples.api.v1.mapper.TodoMapper;
import com.freshworks.boot.samples.common.model.Todo;
import com.freshworks.boot.samples.common.service.TodoService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/api/v1/todos")
public class TodoController {
    private final TodoService todoService;
    private TodoMapper todoMapper;
    private MeterRegistry meterRegistry;
    private Counter todoCounter;

    public TodoController(TodoService todoService, TodoMapper todoMapper, MeterRegistry meterRegistry) {
        this.todoService = todoService;
        this.meterRegistry = meterRegistry;
        this.todoMapper = todoMapper;
        this.todoCounter = this.meterRegistry.counter("todo.created");
    }

    @GetMapping
    @PreAuthorize("hasAuthority('/todo/list')")
    public TodoListResponseDto listTodos() {
        List<Todo> todos = todoService.listAllTodos();
        return new TodoListResponseDto().todos(todoMapper.convert(todos))
                .meta(new MetaDto()
                        .totalItems((long) todos.size()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('/todo/create')")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDto addTodo(@RequestBody @Valid TodoCreateDto newTodoDto) {
        Todo todo = todoMapper.convert(newTodoDto);
        todoCounter.increment(1.0);
        return todoMapper.convert(todoService.addTodo(todo));
    }

    @PutMapping("/{todoId}")
    @PreAuthorize("hasAuthority('/todo/update')")
    public TodoDto updateTodo(@PathVariable long todoId, @RequestBody @Valid TodoCreateDto updateTodoDto) {
        Todo todo = todoMapper.convert(updateTodoDto);
        todo.setId(todoId);
        return todoMapper.convert(todoService.updateTodo(todo));
    }

    @GetMapping("/{todoId}")
    @PreAuthorize("hasAuthority('/todo/get')")
    public TodoDto getTodo(@PathVariable long todoId) {
        return todoMapper.convert(todoService.getTodo(todoId));
    }

    @DeleteMapping("/{todoId}")
    @PreAuthorize("hasAuthority('/todo/delete')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable long todoId) {
        todoService.deleteTodo(todoId);
    }
}
