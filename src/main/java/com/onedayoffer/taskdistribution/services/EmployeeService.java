package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import com.onedayoffer.taskdistribution.DTO.TaskStatus;
import com.onedayoffer.taskdistribution.repositories.EmployeeRepository;
import com.onedayoffer.taskdistribution.repositories.TaskRepository;
import com.onedayoffer.taskdistribution.repositories.entities.Employee;
import com.onedayoffer.taskdistribution.repositories.entities.Task;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public List<EmployeeDTO> getEmployees(@Nullable Sort sort) {
        List<Employee> employees;
        if (Objects.isNull(sort)) {
            employees = employeeRepository.findAll();
        } else {
            employees = employeeRepository.findAllAndSort(sort);
        }
        return employees.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).toList();
    }

    @Transactional
    public EmployeeDTO getOneEmployee(Integer id) {
        return employeeRepository.findById(id)
                                 .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                                 .orElseThrow(
                                         () -> new NotFoundException(String.format("Employee '%s' not found", id)));
    }

    public List<TaskDTO> getTasksByEmployeeId(Integer id) {
        return taskRepository.findAllByEmployeeId(id)
                             .stream()
                             .map(task -> modelMapper.map(task, TaskDTO.class))
                             .toList();
    }

    @Transactional
    public void changeTaskStatus(Integer taskId, TaskStatus status) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        optionalTask.ifPresent(task -> task.setStatus(status));
    }

    @Transactional
    public void postNewTask(Integer employeeId, TaskDTO newTask) {
        Task task = modelMapper.map(newTask, Task.class);
        Employee employee = employeeRepository.findById(employeeId)
                                              .orElseThrow(() -> new NotFoundException(
                                                      String.format("Employee '%s' not found", employeeId)));
        task.setEmployee(employee);
        taskRepository.save(task);
    }
}
