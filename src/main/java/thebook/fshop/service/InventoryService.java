package thebook.fshop.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AddInventoryRequest;
import thebook.fshop.DTO.Request.SearchInventoryRequest;
import thebook.fshop.DTO.Request.UpdateInventoryRequest;
import thebook.fshop.entity.Inventory;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.InventoryRepository;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InventoryService {

    InventoryRepository inventoryRepository;
    BookRepository bookRepository;
//    @PreAuthorize("hasRole('ADMIN')")
    // Thêm sản phẩm vào kho
    public void addProductToInventory(AddInventoryRequest request) {
        if(request.getQuantity()<=0) throw new AppException(ErrorCode.INVALID_QUANTITY);
        var book = bookRepository.findById(request.getBookID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        var existingInventory = inventoryRepository.findByBook(book);

        if (existingInventory != null) {
            existingInventory.setQuantity(existingInventory.getQuantity() + request.getQuantity());
            inventoryRepository.save(existingInventory);
            log.info("Updated product quantity in inventory: {}", existingInventory);
        } else {
            var inventory = Inventory.builder()
                    .book(book)
                    .quantity(request.getQuantity())
                    .build();
            inventoryRepository.save(inventory);
            log.info("Added new product to inventory: {}", inventory);
        }
    }

    public void deleteProductFromInventory(Integer inventoryID) {
        var inventory = inventoryRepository.findById(inventoryID)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        inventoryRepository.deleteById(inventory.getInventoryID()); // Xóa sản phẩm khỏi kho theo ID
        log.info("Product removed from inventory: {}", inventory);
    }


    // Cập nhật số lượng sản phẩm trong kho
    public void updateInventory(UpdateInventoryRequest request) {
        if(request.getNewQuantity()<=0) throw new AppException(ErrorCode.INVALID_QUANTITY);

        var inventory = inventoryRepository.findByBook_ID(request.getBookID())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        inventory.setQuantity(request.getNewQuantity());
        inventoryRepository.save(inventory);
        log.info("Updated inventory quantity: {}", inventory);
    }

    // Tìm kiếm sản phẩm trong kho dựa trên bookID
    public List<Inventory> searchInventory(SearchInventoryRequest request) {
        var  inventory = inventoryRepository.findByBookNameAndAuthorAndMemberType(request.getQuery().toLowerCase());
        if (inventory.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        log.info("Found inventory: {}", inventory);
        return inventory;
    }
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll(); // Trả về danh sách Inventory trực tiếp từ repository
    }



}
