package thebook.fshop.validation.handle;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import thebook.fshop.validation.FileValidation;

@Slf4j
public class FileValidationHandle implements ConstraintValidator<FileValidation, MultipartFile> {

    private final List<String> allowedExtensions = Arrays.asList("jpg", "png", "webp");

    @Override
    public void initialize(FileValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) return false;
        String extension = getFileExtension(fileName);
        if (!allowedExtensions.contains(extension.toLowerCase())) return false;
        return true;
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1);
    }
}
