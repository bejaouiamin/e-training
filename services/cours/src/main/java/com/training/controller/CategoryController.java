package com.training.controller;
import com.training.Dtos.CategoryDto;
import com.training.Dtos.ThemeDto;
import com.training.exeception.BadRequestException;
import com.training.service.CategoryService;
import com.training.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @PostMapping(path = "/add-with-icon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createWithIcon(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestPart("icon") MultipartFile iconFile) {
        try {
            if (iconFile == null || iconFile.isEmpty()) {
                throw new BadRequestException("Icon file is required");
            }

            String contentType = iconFile.getContentType() != null ? iconFile.getContentType() : "";
            if (!contentType.startsWith("image/") && !contentType.equals("image/png+xml")) {
                throw new BadRequestException("Unsupported file type. Only images allowed.");
            }

            Path uploadRoot = Paths.get("uploads", "categories").toAbsolutePath();
            Files.createDirectories(uploadRoot);

            String ext = getExtension(iconFile.getOriginalFilename());
            String filename = UUID.randomUUID().toString() + (ext.isEmpty() ? "" : ("." + ext));
            Path target = uploadRoot.resolve(filename);

            // copy file
            try {
                Files.copy(iconFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file", e);
            }

            CategoryDto dto = CategoryDto.builder()
                    .name(name)
                    .description(description)
                    .icon(filename)
                    .build();

            CategoryDto created = categoryService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    private String getExtension(String original) {
        if (original == null) return "";
        int i = original.lastIndexOf('.');
        if (i < 0) return "";
        return original.substring(i + 1);
    }
//    @PostMapping("/add")
//    public ResponseEntity<Object> create(@RequestBody CategoryDto dto) {
//        try {
//            CategoryDto created = categoryService.create(dto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(created);
//        }catch (BadRequestException ex) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
//        }
//    }

    @GetMapping("/all")
    public List<CategoryDto> allcategory() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id, @RequestBody CategoryDto dto) {
        return categoryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @GetMapping("/icons/{filename}")
    public ResponseEntity<Resource> getIcon(@PathVariable String filename) {
        try {
            Path iconPath = Paths.get("uploads", "categories", filename).toAbsolutePath();
            Resource resource = new UrlResource(iconPath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Determine content type from extension
            String contentType = MediaType.IMAGE_PNG_VALUE;
            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                contentType = MediaType.IMAGE_JPEG_VALUE;
            } else if (filename.endsWith(".svg")) {
                contentType = "image/svg+xml";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
