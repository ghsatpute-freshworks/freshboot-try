package com.freshworks.boot.samples.common.repository;

import java.util.List;
import java.util.Optional;

import com.freshworks.boot.samples.common.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository
        extends JpaRepository<Todo, Long> {
    List<Todo> findByAccountId(long accountId);

    Optional<Todo> findByAccountIdAndId(long accountId, long todoId);
}
