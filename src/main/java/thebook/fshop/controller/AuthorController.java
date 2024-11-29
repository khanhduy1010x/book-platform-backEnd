package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import thebook.fshop.DTO.Request.AddAuthorRequest;
import thebook.fshop.DTO.Request.UpdateAuthorRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.PageAuthorResponse;
import thebook.fshop.entity.Author;
import thebook.fshop.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthorController {
    AuthorService authorService;
    @GetMapping("/get-all")
    public ApiResponse<PageAuthorResponse> getAllAuthors(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)  {
       return ApiResponse.<PageAuthorResponse>builder()
                .result(authorService.getAllAuthors(page,size))
                .build();
    }

    @PostMapping("/add-new")
    public ApiResponse<?> addNewAuthor(@ModelAttribute AddAuthorRequest request) {
        authorService.addAuthor(request);
        return ApiResponse.builder().build();
    }
    @PostMapping("/update-author")
    public ApiResponse<?> updateAuthor(@ModelAttribute UpdateAuthorRequest request) {
        authorService.updateAuthor(request);
        return ApiResponse.builder().build();
    }

    @GetMapping("/search-author/{search}")
    public ApiResponse<List<Author>> searchAuthor(@PathVariable String search) {
        return ApiResponse.<List<Author>>builder()
                .result(authorService.searchAuthor(search.toLowerCase()))
                .build();
    }
    @GetMapping("/get-all-home")
    public ApiResponse<List<Author>> getAllHome() {
        return ApiResponse.<List<Author>>builder()
                .result(authorService.getAllHome())
                .build();
    }
}

