package ${basePackage}.${entityName?lower_case};

import io.github.alishahidi.sbcore.response.ApiResponse;
import ${basePackage}.${entityName?lower_case}.dto.*;
import io.github.alishahidi.sbcore.search.SearchParams;
import io.github.alishahidi.sbcore.search.SearchParamsWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/${entityName?lower_case}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "${entityName} Management", description = "Endpoints for managing ${entityName} entities")
public class ${entityName}Controller {
    
    ${entityName}Service ${entityName?uncap_first}Service;

    @PostMapping
    @Operation(summary = "Create a new ${entityName}")
    public ResponseEntity<ApiResponse<${entityName}LoadDto>> create(
            @Valid @RequestBody ${entityName}CreateDto createDto) {
        return ResponseEntity.ok(ApiResponse.success(${entityName?uncap_first}Service.create(createDto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing ${entityName}")
    public ResponseEntity<ApiResponse<${entityName}LoadDto>> update(
            @PathVariable Long id, 
            @Valid @RequestBody ${entityName}UpdateDto updateDto) {
        return ResponseEntity.ok(ApiResponse.success(${entityName?uncap_first}Service.update(id, updateDto)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a ${entityName} by ID")
    public ResponseEntity<ApiResponse<${entityName}LoadDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(${entityName?uncap_first}Service.findById(id)));
    }

    @GetMapping
    @Operation(summary = "Search ${entityName} entities")
    public ResponseEntity<ApiResponse<List<${entityName}LoadDto>>> search(
            @SearchParams SearchParamsWrapper params) {
        return ResponseEntity.ok(ApiResponse.success(${entityName?uncap_first}Service.search(params)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ${entityName}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ${entityName?uncap_first}Service.deleteLogical(id);
        return ResponseEntity.noContent().build();
    }
}
