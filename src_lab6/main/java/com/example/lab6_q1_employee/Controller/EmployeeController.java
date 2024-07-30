package com.example.lab6_q1_employee.Controller;

import com.example.lab6_q1_employee.ApiResponse.ApiResponse;
import com.example.lab6_q1_employee.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity getEmployees() {
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/add")
    public ResponseEntity addEmployee(@Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee added successfully"));
    }

    @PutMapping("/update/{index}")
    public ResponseEntity updateEmployee(@PathVariable int index, @Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employees.set(index,employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee updated successfully"));
    }
    @DeleteMapping("/delete/{index}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int index) {
        if (index < 0 || index >= employees.size()) {
            return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
        }
        employees.remove(index);
        return ResponseEntity.status(200).body(new ApiResponse("Employee deleted successfully"));
    }

    @GetMapping("/search/{position}")
    public ResponseEntity searchPosition(@PathVariable String position) {
        ArrayList<Employee> searchedPosition  = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getPosition().equals(position)) {
                searchedPosition.add(employee);
            }
        }
        if (!position.equalsIgnoreCase("supervisor") && !position.equalsIgnoreCase("coordinator"))
            return ResponseEntity.status(404).body(new ApiResponse("invalid position"));
        return ResponseEntity.status(200).body(searchedPosition);
    }

    @GetMapping("/range/{min}/{max}")
    public ResponseEntity searchRange(@PathVariable int min, @PathVariable int max) {
        ArrayList<Employee> searchedRange  = new ArrayList<>();
        for (Employee employee : employees) {
            if(employee.getAge() >= min && employee.getAge() <= max){
                searchedRange.add(employee);
            }
        }
        if(min < 18 || max < 70)
            return ResponseEntity.status(200).body(searchedRange);
        return ResponseEntity.status(404).body(new ApiResponse("invalid range"));
    }

    @GetMapping("/vacation/{id}")
    public ResponseEntity vacation(@PathVariable int id) {
      for (Employee employee:employees){
          if(employee.getId()==id){
              if(employee.isOnLeave()) {
                  return ResponseEntity.status(404).body(new ApiResponse("Employee is already on-leave"));
              } else if(employee.getAnnualLeave() < 1) {
                  return ResponseEntity.status(404).body(new ApiResponse("Employee used his annual leave days"));
              }else{
                  employee.setOnLeave(true);
                  employee.setAnnualLeave(employee.getAnnualLeave() - 1);
                  return ResponseEntity.status(200).body(new ApiResponse("Employee is on-leave"));
              }
          }
      }
      return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }

    public ResponseEntity getNoLeaveEmployees(){
        ArrayList<Employee> noLeaveEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            if(employee.getAnnualLeave() == 0) {
                noLeaveEmployees.add(employee);
            }
        }
        return ResponseEntity.status(200).body(noLeaveEmployees);
    }
    @GetMapping("/promote/{requesterId}/{id}")
    public ResponseEntity promoteEmployee(@PathVariable int id, @PathVariable int requesterId) {
        Employee requester = null;
        Employee nominee = null;

        for (Employee employee : employees) {
            if (employee.getId() == id) {
                nominee = employee;
            }
            if (employee.getId() == requesterId) {
                requester = employee;
            }
        }

        if (nominee == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Nominee not found"));
        }

        if (requester == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Requester not found"));
        }

        if (!requester.getPosition().equals("supervisor")) {
            return ResponseEntity.status(403).body(new ApiResponse("Requester is not a supervisor"));
        }

        if (!nominee.getPosition().equals("coordinator")) {
            return ResponseEntity.status(400).body(new ApiResponse("Nominee is not a coordinator"));
        }

        if (nominee.isOnLeave()) {
            return ResponseEntity.status(400).body(new ApiResponse("Nominee is currently on leave"));
        }

        if (nominee.getAge() < 30) {
            return ResponseEntity.status(400).body(new ApiResponse("Nominee is not old enough for the supervisor position"));
        }

        nominee.setPosition("supervisor");
        return ResponseEntity.status(200).body(new ApiResponse("Congratulations! The employee has been promoted to supervisor"));
    }
}
