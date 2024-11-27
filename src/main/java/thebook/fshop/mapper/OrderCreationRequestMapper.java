package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import thebook.fshop.DTO.Request.OrderCreationRequest;
import thebook.fshop.helper.TempAddress;

@Mapper(componentModel = "spring")
public interface OrderCreationRequestMapper {
   OrderCreationRequest toOrderCreationRequest(TempAddress tempAddress);
}
