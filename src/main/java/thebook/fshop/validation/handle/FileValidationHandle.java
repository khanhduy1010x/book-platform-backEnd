package thebook.fshop.validation.handle;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.validation.FileValidation;


import java.util.Arrays;
import java.util.List;

@Slf4j
public class FileValidationHandle implements ConstraintValidator<FileValidation, MultipartFile> {

    private final List<String> allowedExtensions = Arrays.asList("jpg", "png", "webp");
    @Override
    public void initialize(FileValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        // Check if the file is null or empty, and throw an exception if it is
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_NULL); // Throw exception for empty file
        }

        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            String extension = getFileExtension(fileName);
            if (!allowedExtensions.contains(extension.toLowerCase())) {
                // Throw custom exception for unsupported file types
                throw new AppException(ErrorCode.INVALID_FILE);
            }
            return true; // Valid if extension is allowed
        }

        return false; // Invalid if no extension is found
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1);
    }
}
