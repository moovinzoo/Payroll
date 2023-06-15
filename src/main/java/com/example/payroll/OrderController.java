package com.example.payroll;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository repository;

    private final OrderModelAssembler assembler;

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> all() {

        List<EntityModel<Order>> orders = repository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> one(@PathVariable Long id) {

        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }
}
