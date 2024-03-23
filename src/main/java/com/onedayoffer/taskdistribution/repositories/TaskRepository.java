package com.onedayoffer.taskdistribution.repositories;

import java.util.List;
import java.util.Optional;

import com.onedayoffer.taskdistribution.repositories.entities.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByEmployeeId(Integer id);

}
