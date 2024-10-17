package thebook.fshop.controller;



import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import thebook.fshop.DTO.Request.AddInventoryRequest;
import thebook.fshop.DTO.Request.InventoryResponse;
import thebook.fshop.DTO.Request.UpdateInventoryRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Thêm sản phẩm vào kho
    @PostMapping("/add")
    public ApiResponse<?> addProductToInventory(@RequestBody AddInventoryRequest request) {
        inventoryService.addProductToInventory(request); // Gọi phương thức addProductToInventory từ service
        return ApiResponse.builder().message("Sản phẩm đã được thêm vào kho thành công.").build();
    }

    // Xóa sản phẩm khỏi kho bằng ID
    @DeleteMapping("   ")
    public ApiResponse<?> deleteProductFromInventory(@PathVariable Integer inventoryID) {
        inventoryService.deleteProductFromInventory(inventoryID); // Gọi phương thức deleteProductFromInventory từ service
        return ApiResponse.builder().message("Sản phẩm đã được xóa khỏi kho thành công.").build();
    }
    // Cập nhật số lượng sản phẩm trong kho
    @PutMapping("/update")
    public ApiResponse<?> updateInventory(@RequestBody UpdateInventoryRequest request) {
        inventoryService.updateInventory(request.getInventoryID(), request.getNewQuantity());
        return ApiResponse.builder().build();
    }

    // Tìm kiếm sản phẩm trong kho dựa trên bookID
//    @GetMapping("/search/{bookID}")
//    public ApiResponse<InventoryResponse> searchInventory(@PathVariable Integer bookID) {
//        var inventory = inventoryService.searchInventory(bookID);
//        var response = InventoryResponse.builder()
//                .inventoryID(inventory.getInventoryID())
//                .bookID(inventory.getBook().getID())
//                .bookTitle(inventory.getBook().getBookName())  // Nếu muốn trả về tên sách
//                .quantity(inventory.getQuantity())
//                .build();
//
//        return ApiResponse.<InventoryResponse>builder().data(response).build();
//    }
}
