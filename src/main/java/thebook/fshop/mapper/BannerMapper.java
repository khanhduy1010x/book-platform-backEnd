package thebook.fshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thebook.fshop.DTO.Response.BannerResponse;
import thebook.fshop.entity.Banner;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    @Mapping(source = "book.ID", target = "bookId")
    BannerResponse toResponse(Banner banner);
}
