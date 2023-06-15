package com.example.payroll;

import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public @NonNull EntityModel<Order> toModel(@NonNull Order entity) {
//        return EntityModel.of(entity,
//                linkTo(methodOn(OrderController.class)))
        return null;
    }
}
