package com.example.payroll;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository repository;

    private final EmployeeModelAssembler assembler;

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {

        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@PathVariable long id,
                                      @RequestBody Employee newEmployee) {

        AtomicBoolean created = new AtomicBoolean(false); // flag

        // find existing entity by given (id, employee)
        Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                }) // update existing entity
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    created.set(true);
                    return repository.save(newEmployee);
                }); // If not exist, insert the one

        // convert entity to entity-model
        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        if (created.get()) { // when new entity has been inserted
            // response with location header
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } else {
            return ResponseEntity.ok(entityModel);
        }
    }

    // TODO: 2023/06/15 Improve DELETE /employees/{id}
    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
//    @DeleteMapping("/employees/{id}")
//    void deleteEmployee(@PathVariable long id) {
//        repository.deleteById(id);
//    }
}