package thebook.fshop.mapper;

import org.mapstruct.Mapper;

import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toCartResponse(CartItem cartItem);
}
