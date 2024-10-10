package thebook.fshop.mapper;

import thebook.fshop.DTO.Response.CartResponse;
import thebook.fshop.entity.Cart;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponse toCartResponse(Cart cart) {
        return CartResponse.builder()
                .cartID(cart.getID())
                .accountID(cart.getAccount().getAccID())
                .accountFullName(cart.getAccount().getFullName())
                .accountMemberType(cart.getAccount().getMemberType())
                .bookID(cart.getBook().getID())
                .bookName(cart.getBook().getBookName())
                .bookAuthor(cart.getBook().getAuthor())
                .bookPrice(cart.getBook().getPrice())
                .bookMemberType(cart.getBook().getMemberType())
                .bookCoverImage(cart.getBook().getCoverImage())
                .quantity(cart.getQuantity())
                .build();
    }

    public static List<CartResponse> toCartResponseList(List<Cart> carts) {
        return carts.stream()
                .map(CartMapper::toCartResponse)
                .collect(Collectors.toList());
    }
}
