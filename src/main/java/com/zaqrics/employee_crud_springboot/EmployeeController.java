package com.zaqrics.employee_crud_springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    // Create Employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        if (employee.getName() == null || employee.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Employee savedEmployee = (Employee) rabbitTemplate.convertSendAndReceive("employeeQueue", employee);
        if (savedEmployee != null) {
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get All Employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete Employee by ID and return updated list
    @DeleteMapping("/{id}")
    public ResponseEntity<List<Employee>> deleteEmployeeById(@PathVariable Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id); // Delete the employee
            List<Employee> updatedList = employeeRepository.findAll(); // Fetch updated list
            return new ResponseEntity<>(updatedList, HttpStatus.OK); // Return updated list
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Employee not found
        }
    }

    @GetMapping("/log-example")
    public String logExample() {
        log.info("This is an INFO log message.");
        log.debug("This is a DEBUG log message.");
        log.warn("This is a WARN log message.");
        log.error("This is an ERROR log message.");
        return "Check the logs!";
    }

}
