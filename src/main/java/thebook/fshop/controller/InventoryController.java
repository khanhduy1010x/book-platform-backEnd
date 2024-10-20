package thebook.fshop.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import thebook.fshop.DTO.Request.AddInventoryRequest;
import thebook.fshop.DTO.Request.SearchInventoryRequest;
import thebook.fshop.DTO.Response.InventoryResponse;
import thebook.fshop.DTO.Request.UpdateInventoryRequest;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Inventory;
import thebook.fshop.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    // Thêm sản phẩm vào kho
    @PostMapping("/add")
    public ApiResponse<?> addProductToInventory(@RequestBody AddInventoryRequest request) {
        inventoryService.addProductToInventory(request); // Gọi phương thức addProductToInventory từ service
        return ApiResponse.builder().build();
    }

    // Xóa sản phẩm khỏi kho bằng ID
    @DeleteMapping("/delete/{inventoryID}")
    public ApiResponse<?> deleteProductFromInventory(@PathVariable Integer inventoryID) {
        inventoryService.deleteProductFromInventory(inventoryID); // Gọi phương thức deleteProductFromInventory từ service
        return ApiResponse.builder().build();
    }

    // Cập nhật số lượng sản phẩm trong kho
    @PutMapping("/update")
    public ApiResponse<?> updateInventory(@RequestBody UpdateInventoryRequest request) {
        inventoryService.updateInventory(request);
        return ApiResponse.builder().build();
    }

    //     Tìm kiếm sản phẩm trong kho dựa trên bookID
    @PostMapping("/search")
    public ApiResponse<List<Inventory>> searchInventory(@RequestBody SearchInventoryRequest request) {
    log.info("Search : {} ", request.getQuery());

        return ApiResponse.<List<Inventory>>builder()
                .result(inventoryService.searchInventory(request))


                .build();
    }
// Lấy danh sách toàn bộ sản phẩm trong kho
    @GetMapping("/view")
    public ApiResponse<List<Inventory>> viewInventory() {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        return ApiResponse.<List<Inventory>>builder()
                .result(inventoryList)
                .build();
    }



}
