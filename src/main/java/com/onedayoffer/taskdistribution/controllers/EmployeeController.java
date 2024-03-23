package com.onedayoffer.taskdistribution.controllers;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import com.onedayoffer.taskdistribution.DTO.TaskStatus;
import com.onedayoffer.taskdistribution.services.EmployeeService;
import com.onedayoffer.taskdistribution.services.NotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDTO> getEmployees(@RequestParam(name = "sort", required = false) String sortDirection) {
        Sort sort = parseSort(sortDirection);
        return employeeService.getEmployees(sort);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO getOneEmployee(@PathVariable Integer id) {
        return employeeService.getOneEmployee(id);
    }

    @GetMapping("{id}/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getTasksByEmployeeId(@PathVariable Integer id) {
        return employeeService.getTasksByEmployeeId(id);
    }

    @PatchMapping("{id}/tasks/{taskId}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeTaskStatus(@PathVariable(name = "id") Integer ignored, @PathVariable Integer taskId, @RequestParam(name = "newStatus") String newStatus) {
        employeeService.changeTaskStatus(taskId, TaskStatus.valueOf(newStatus));
        //TaskStatus status = TaskStatus.valueOf(newStatus);
        //employeeService.changeTaskStatus ...
    }

    @PostMapping("{id}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public void postNewTask(@PathVariable Integer id, @RequestBody TaskDTO taskDTO) {
        employeeService.postNewTask(id, taskDTO);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public void handle(IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public void handle(NotFoundException exception) {
        log.error(exception.getMessage(), exception);
    }

    private Sort parseSort(String direction) {
        if ("ASC".equals(direction)) {
            return Sort.by(Sort.Direction.ASC, "fio");
        } else if ("DESC".equals(direction)) {
            return Sort.by(Sort.Direction.DESC, "fio");
        }
        return null;
    }

}