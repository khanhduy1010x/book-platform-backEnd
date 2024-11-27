package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thebook.fshop.DTO.Response.OrderDetailResponse;
import thebook.fshop.DTO.Response.OrderResponse;
import thebook.fshop.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "firstBook", ignore = true )
    OrderResponse toResponse(Order order);

    @Mapping(target = "listBook", ignore = true )
    OrderDetailResponse toOrderDetailResponse(Order order);
}
