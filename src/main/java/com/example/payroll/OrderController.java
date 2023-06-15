package com.example.payroll;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * All the controller methods return one of Spring HATEOAS’s RepresentationModel subclasses
 * to properly render hypermedia (or a wrapper around such a type).
 */
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository repository;

    private final OrderModelAssembler assembler;

    /**
     * handles the aggregate root
     * @return orders
     */
    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> all() {

        List<EntityModel<Order>> orders = repository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    /**
     * handles single item: Order resource request
     * @param id id of the item
     * @return  If a corresponding item exists, it's returned; else, it throws {@link OrderNotFoundException}.
     */
    @GetMapping("/orders/{id}")
    public EntityModel<Order> one(@PathVariable Long id) {

        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    /**
     * handles creating new orders, by starting them in the IN_PROGRESS state.
     * @param order new item order
     * @return new order
     */
    @PostMapping("/orders")
    public ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {

        order.setStatus(Status.IN_PROGRESS); // set order-status to be in-progress
        Order newOrder = repository.save(order); // and then save

        EntityModel<Order> entityModel = assembler.toModel(newOrder);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
