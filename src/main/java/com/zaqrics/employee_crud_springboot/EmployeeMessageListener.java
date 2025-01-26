package com.zaqrics.employee_crud_springboot;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class EmployeeMessageListener {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Async
    @RabbitListener(queues = "employeeQueue")
    public CompletableFuture<Employee> handleMessage(Employee employee) {
        // Process the message and update DB
        Employee savedEmployee = employeeRepository.save(employee);
        System.out.println("Employee saved from queue: " + employee);

        return CompletableFuture.completedFuture(savedEmployee);
    }
}

