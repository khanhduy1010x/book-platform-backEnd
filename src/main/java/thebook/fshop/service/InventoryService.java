package thebook.fshop.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AddInventoryRequest;
import thebook.fshop.entity.Inventory;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.InventoryRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InventoryService {

    InventoryRepository inventoryRepository;
    BookRepository bookRepository;
    @PreAuthorize("hasRole('ADMIN')")
    // Thêm sản phẩm vào kho
    public void addProductToInventory(AddInventoryRequest request) {
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
    public void updateInventory(Integer inventoryID, int newQuantity) {
        var inventory = inventoryRepository.findById(inventoryID)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
        log.info("Updated inventory quantity: {}", inventory);
    }

    // Tìm kiếm sản phẩm trong kho dựa trên bookID
    public Inventory searchInventory(Integer bookID) {
        var book = bookRepository.findById(bookID)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        var inventory = inventoryRepository.findByBook(book);
        if (inventory == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        log.info("Found inventory: {}", inventory);
        return inventory;
    }
}
