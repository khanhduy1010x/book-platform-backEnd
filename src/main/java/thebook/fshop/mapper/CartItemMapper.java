package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import thebook.fshop.DTO.Response.CartItemResponse;
import thebook.fshop.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemResponse toResponse(CartItem cartItem);
}
