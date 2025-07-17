package com.example.task.repository;


import com.example.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
    SELECT t FROM Task t
    WHERE (:date IS NULL OR t.date = :date)
      AND (:completed IS NULL OR t.completed = :completed)
      AND (t.user.id = :userid)
""")
    List<Task> findByDateAndCompletedOptional(
            @Param("date") Date date,
            @Param("completed") Boolean completed,
            @Param("userid") Long userId
    );



}
