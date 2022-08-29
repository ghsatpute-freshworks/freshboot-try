package com.freshworks.boot.samples.api.v1.controller;

import com.freshworks.boot.test.api.JwtUtil;
import com.freshworks.boot.samples.common.model.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ComponentScan(basePackages = {"com.freshworks.boot.samples"})
@EntityScan(basePackages = {"com.freshworks.boot.samples"})
@ActiveProfiles("test")
@Transactional
class TodoControllerTest {
    private static final String KEY_ACCOUNT_ID = "account_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_X_FWI_CLIENT_TOKEN = "x-fwi-client-token";
    private static final String KEY_X_FWI_CLIENT_ID = "x-fwi-client-id";

    private static final boolean COMPLETED = true;
    private static final boolean COMPLETED_NOT = true;
    private static final String TITLE = "Buy milk";
    private static final String TITLE_2 = "Fill gas";
    private static final String JWT;
    private static final String CLIENT_CREDENTIALS;
    private static final String CLIENT_ID = JwtUtil.SOURCE;

    static {
        try {
            JWT = JwtUtil.generateJwtString(Map.of());
            CLIENT_CREDENTIALS = JwtUtil.generateClientJwtString(Map.of());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception occurred", e);
        }
    }

    private static final String JWT_INVALID = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImR1bW15In0.eyJhY2NvdW50X2lkIjoiMSIsInVzZXJfaWQiOiIxIiwicHJvZHVjdCI6ImZyZXNoZGVzayIsIm9yZ19pZCI6IjEiLCJhY2NvdW50X2RvbWFpbiI6ImRvbWFpbiIsInByaXZpbGVnZXMiOiIwIn0.nGCjKEhLLxezXVPDnnhmbdxsITgE4kmJfhhBJxeyR4NmEqWlv0dS_E3ezvLeinHhE0EV8IPwemEkxGI456mGqcyvnnH-PC62Tlgt6-iq-qWdskIyKSVBvOKXjtxPC7x94rATrL8vwPWqGuGVK1eLD2mKhcs0CvxM3HWMlwesVxQTEw-zFO1XLK-UJEsxicvIdYioht_z12dR3CYYNCtpIF1YiB9cuU0jM_UZYWZbvXMhKvPghxYeb3aLRYo-aBqJoRfvw4DS-wZKrlrvF5JjCR-edF5Oe-vlLlnaHAgyaeg5dxR84jI2YbQRfKblJAlfWERnZV_Ff-H85G8eyORXhQ";
    private static final String HOST = "host";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mvc;

    private final HttpHeaders httpHeaders = getHttpHeaders();

    @Test
    public void listTodos_givenNoTodos_whenCalled_thenReturnsEmptyList() throws Exception {
        mvc
                .perform(
                        get("/api/v1/todos")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(todoList()));
    }


    @Test
    public void listTodos_givenTodosPresent_whenCalled_thenReturnsThem() throws Exception {
        final Long id1 = addTodo(Map.of());
        final Long id2 = addTodo(Map.of(KEY_TITLE, TITLE_2, KEY_COMPLETED, COMPLETED_NOT));

        mvc
                .perform(
                        get("/api/v1/todos")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(todoList(todoWithId(id1, TITLE, COMPLETED), todoWithId(id2, TITLE_2, COMPLETED_NOT))));
    }

    @Test
    public void addTodo_whenCalledWithValidValues_thenSavesTodo() throws Exception {
        mvc
                .perform(
                        post("/api/v1/todos/")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                                .content(todo(TITLE, COMPLETED))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(todo(TITLE, COMPLETED)))
                .andExpect(jsonPath("$.id").isNumber());

        @SuppressWarnings("unchecked")
        List<Todo> todos = (List<Todo>) entityManager.createQuery("select t from Todo t").getResultList();
        assertThat(todos.size()).isEqualTo(1);
        Todo todo = todos.get(0);
        assertThat(todo.getTitle()).isEqualTo(TITLE);
        assertThat(todo.isCompleted()).isEqualTo(COMPLETED);
    }

    @Test
    public void getTodo_whenCalledOnExistingEntity_thenReturnsIt() throws Exception {
        final Long todoId = addTodo(Map.of());

        mvc
                .perform(
                        get("/api/v1/todos/" + todoId)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(todoWithId(todoId, TITLE, COMPLETED)));
    }

    @Test
    public void getTodo_whenCalledOnNonExistingEntity_thenReturns404() throws Exception {
        mvc
                .perform(
                        get("/api/v1/todos/1")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'message':'The resource is not found','errors':[{'field':'todo_id','code':'not_found','message':'The resource is not found'}],'error_type':'not_found'}"));
    }

    @Test
    public void updateTodo_whenCalledOnExistingEntity_thenUpdatesProperties() throws Exception {
        final Long todoId = addTodo(Map.of());

        mvc
                .perform(
                        put("/api/v1/todos/" + todoId)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                                .content(todo(TITLE_2, COMPLETED_NOT))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(todoWithId(todoId, TITLE_2, COMPLETED_NOT)));

        final Todo todo = getTodo(todoId);
        assertThat(todo.getTitle()).isEqualTo(TITLE_2);
        assertThat(todo.isCompleted()).isEqualTo(COMPLETED_NOT);
    }

    @Test
    public void updateTodo_whenCalledOnNonExistingEntity_thenReturns404() throws Exception {
        mvc
                .perform(
                        put("/api/v1/todos/1")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                                .content(todo(TITLE_2, COMPLETED_NOT))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'message':'The resource is not found','errors':[{'field':'todo_id','code':'not_found','message':'The resource is not found'}],'error_type':'not_found'}"));
    }

    @Test
    public void deleteTodo_whenCalledOnExistingEntity_thenDeletesIt() throws Exception {
        final Long todoId = addTodo(Map.of());

        mvc
                .perform(
                        delete("/api/v1/todos/" + todoId)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                )
                .andExpect(status().isNoContent());
        final Todo todo = getTodo(todoId);
        assertThat(todo).isNull();
    }

    @Test
    public void deleteTodo_whenCalledOnNonExistingEntity_thenReturns404() throws Exception {
        mvc
                .perform(
                        delete("/api/v1/todos/1")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .headers(httpHeaders)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'message':'The resource is not found','errors':[{'field':'todo_id','code':'not_found','message':'The resource is not found'}],'error_type':'not_found'}"));
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + JWT);
        httpHeaders.add(KEY_X_FWI_CLIENT_TOKEN, "Bearer " + CLIENT_CREDENTIALS);
        httpHeaders.add(KEY_X_FWI_CLIENT_ID, CLIENT_ID);
        httpHeaders.add(HOST, JwtUtil.ACCOUNT_DOMAIN);
        return httpHeaders;
    }

    private Long addTodo(Map<String, Object> overrides) {
        final Todo entity = new Todo();
        entity.setAccountId((long) overrides.getOrDefault(KEY_ACCOUNT_ID, JwtUtil.ACCOUNT_ID));
        entity.setTitle((String) overrides.getOrDefault(KEY_TITLE, TITLE));
        entity.setCompleted((Boolean) overrides.getOrDefault(KEY_COMPLETED, COMPLETED));
        entityManager.persist(entity);
        return entity.getId();
    }

    private Todo getTodo(Long todoId) {
        return entityManager.find(Todo.class, todoId);
    }

    private String todo(String title, boolean completed) {
        return String.format("{\"title\": \"%s\",\"completed\": %b}", title, completed);
    }

    private String todoWithId(Long id, String title, boolean completed) {
        return String.format("{\"id\": %d, \"title\": \"%s\",\"completed\": %b}", id, title, completed);
    }

    private String todoList(String... todos) {
        return String.format("{\"todos\":[%s],\"meta\":{\"total_items\":%d}}", String.join(",", todos), todos.length);
    }
}