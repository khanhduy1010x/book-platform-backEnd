package thebook.fshop.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import thebook.fshop.entity.Book;
import thebook.fshop.entity.Category;
import thebook.fshop.helper.MemberType;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.CategoryRepository;

@Configuration
public class PublicDomainBookSeeder {
    private record Seed(String title, String author, String category, String epub, String cover, String description) {}

    private static final Seed[] BOOKS = {
        new Seed("Pride and Prejudice", "Jane Austen", "Tiểu thuyết", "/books/1342.epub", "/books/1342.jpg", "Một câu chuyện về định kiến, gia đình và tình yêu trong xã hội Anh thế kỷ XIX."),
        new Seed("The Adventures of Sherlock Holmes", "Arthur Conan Doyle", "Trinh thám", "/books/1661.epub", "/books/1661.jpg", "Mười hai vụ án kinh điển của Holmes và Watson."),
        new Seed("Alice's Adventures in Wonderland", "Lewis Carroll", "Phiêu lưu", "/books/11.epub", "/books/11.jpg", "Theo Alice bước vào thế giới của trí tưởng tượng."),
        new Seed("Frankenstein", "Mary Shelley", "Giả tưởng", "/books/84.epub", "/books/84.jpg", "Tiểu thuyết về sáng tạo và trách nhiệm."),
        new Seed("Little Women", "Louisa May Alcott", "Tiểu thuyết", "/books/514.epub", "/books/514.jpg", "Bốn chị em nhà March lớn lên cùng những lựa chọn và tình thân.")
    };

    @Bean
    ApplicationRunner seedPublicDomainBooks(BookRepository bookRepository, CategoryRepository categoryRepository) {
        return args -> {
            for (Seed seed : BOOKS) {
                Category category = categoryRepository.findByCateName(seed.category())
                    .orElseGet(() -> categoryRepository.save(Category.builder().cateName(seed.category()).build()));
                if (bookRepository.findByUrl(seed.epub()).isEmpty()) {
                    bookRepository.save(Book.builder()
                        .bookName(seed.title())
                        .author(seed.author())
                        .category(category)
                        .memberType(MemberType.NONE)
                        .price(0)
                        .url(seed.epub())
                        .coverImage(seed.cover())
                        .description(seed.description())
                        .build());
                }
            }
        };
    }
}
