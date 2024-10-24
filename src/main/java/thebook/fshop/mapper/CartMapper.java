package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "cart.ID", target = "cartID") // ánh xạ cartID
    @Mapping(source = "cart.account.accID", target = "accountID") // ánh xạ accountID
    @Mapping(source = "outOfStock", target = "outOfStock")
    CartResponse toCartResponse(CartItem cartItem);
}
