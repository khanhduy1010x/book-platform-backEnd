package thebook.fshop.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "outOfStock",target = "outOfStock")
    CartResponse toCartResponse(CartItem cartItem);
}
