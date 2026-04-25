package com.esoft.api.controller;

import com.esoft.api.dto.module.ModuleRequest;
import com.esoft.api.dto.module.ModuleResponse;
import com.esoft.api.service.ModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping
    public ResponseEntity<ModuleResponse> create(@Valid @RequestBody ModuleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ModuleResponse>> getAll() {
        return ResponseEntity.ok(moduleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(moduleService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleResponse> update(@PathVariable UUID id, @Valid @RequestBody ModuleRequest request) {
        return ResponseEntity.ok(moduleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        moduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
