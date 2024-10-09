package thebook.fshop.controller;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import thebook.fshop.DTO.Request.AddToFavoriteRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.service.FavoriteService;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // Add a favorite
    @PostMapping("/add")
    public ApiResponse<?> addFavorite(@RequestBody AddToFavoriteRequest request) {
        favoriteService.addFavorite(request); // Call the addFavorite method from the service
        return ApiResponse.builder().build();
    }

    // Delete a favorite by ID
    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteFavorite(@PathVariable Integer id) {
        favoriteService.deleteFavorite(id);
        return ApiResponse.builder().build();
        // Call the deleteFavorite method from the service
    }
}
