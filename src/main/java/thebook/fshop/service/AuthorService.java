package thebook.fshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import thebook.fshop.DTO.Request.AddAuthorRequest;
import thebook.fshop.DTO.Request.UpdateAuthorRequest;
import thebook.fshop.DTO.Response.PageAuthorResponse;
import thebook.fshop.entity.Author;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.AuthorRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthorService {
  AuthorRepository authorRepository;
    @NonFinal
    @Value("${upload.path_author}")
    String UPLOAD_PATH;

    @NonFinal
    @Value("${path.author}")
    String PATH_PREFIX;
  public void addAuthor (AddAuthorRequest addAuthorRequest) {
      String imageUrl = saveFile(addAuthorRequest.getMultipartFile(),UPLOAD_PATH, PATH_PREFIX);
      var author = Author.builder()
              .name(addAuthorRequest.getName())
              .imageURL(imageUrl)
              .build();
      authorRepository.save(author);
  }
  public void updateAuthor (UpdateAuthorRequest request ){
      var currentAuthor = authorRepository.findById(request.getId()).get();
      if(request.getMultipartFile()!=null) {
          String imageUrl = saveFile(request.getMultipartFile(),UPLOAD_PATH, PATH_PREFIX);
          currentAuthor.setImageURL(imageUrl);
      }
          currentAuthor.setName(request.getName());

      authorRepository.save(currentAuthor);
  }
  public PageAuthorResponse getAllAuthors(int page, int size){
      Pageable pageable = PageRequest.of(page,size);
      var list = authorRepository.findAll(pageable);
      return PageAuthorResponse.builder()
              .listAuthor(list.getContent())
              .currentPage(list.getNumber())
              .totalPages(list.getTotalPages())
              .build();
  }

    private String saveFile(MultipartFile file, String uploadPath, String pathPrefix) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_NULL);
        }

        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            String uniqueID = UUID.randomUUID().toString();
            String fileName = baseName + "_" + uniqueID + fileExtension;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filePath = uploadPath + File.separator + fileName;
            file.transferTo(new File(filePath));

            return pathPrefix + fileName;
        } catch (IOException e) {
            log.error("Error saving file: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_FILE_NULL);
        }
    }

    public List<Author> searchAuthor(String search) {
      return authorRepository.searchAuthor(search);
    }

    public List<Author> getAllHome() {
      return authorRepository.findAll();
    }

}
