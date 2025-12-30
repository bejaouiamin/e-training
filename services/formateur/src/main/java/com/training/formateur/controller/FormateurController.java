package com.training.formateur.controller;

import com.training.formateur.Request.*;
import com.training.formateur.Response.FormateurResponse;
import com.training.formateur.service.FormateurService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/formateurs")
@AllArgsConstructor
public class FormateurController {
    private final FormateurService service;

    @PostMapping("/add")
    public ResponseEntity<String> createFormateur(@RequestBody @Valid FormateurRequest request) {
        return ResponseEntity.ok(this.service.createFormateur(request));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid FormateurRequest request) {
        this.service.updateFormateur(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<FormateurResponse>> findAll() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(this.service.findById(id).toString());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        this.service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping("/themes")
    public ResponseEntity<String> createTheme(@RequestBody ThemeRequest request) {
        service.createTheme(
                request.getKeycloakId(),
                request.getTitle(),
                request.getDescription(),
                request.getCategoryId(),
                request.getDureeHeures()
        );
        return ResponseEntity.ok("Theme creation event published");
    }
    @PostMapping(value = "/lessons", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createLesson(@ModelAttribute LessonRequest request) {
        service.createLesson(request.getKeycloakId(), request);
        return ResponseEntity.ok("Lesson creation event published");
    }




    @PostMapping("/quizzes")
    public ResponseEntity<String> createQuiz(@RequestBody QuizRequest request){
        service.createQuiz(
                request.getKeycloakId(),
                request.getTitle(),
                request.getUrl(),
                request.getResourceType(),
                request.getPassingScore(),
                request.getLessonId()
        );
        return ResponseEntity.ok("Quiz creation event published");
    }



}
