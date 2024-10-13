package thebook.fshop.validation.handle;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import thebook.fshop.validation.FileSizeValidation;

public class FileSizeValidationHandle implements ConstraintValidator<FileSizeValidation, MultipartFile> {
    private long fileSize;

    @Override
    public void initialize(FileSizeValidation constraintAnnotation) {
        this.fileSize = constraintAnnotation.maxFileSize();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        return fileSize >= multipartFile.getSize();
    }
}
