package io.github.alishahidi.sbcore.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/async")
public class AsyncController {

    private final AsyncDynamicConfig asyncDynamicConfig;

    public AsyncController(AsyncDynamicConfig asyncDynamicConfig) {
        this.asyncDynamicConfig = asyncDynamicConfig;
    }

    @PostMapping("/updatePool")
    public ResponseEntity<String> updatePoolSize(@RequestParam int core, @RequestParam int max) {
        asyncDynamicConfig.updatePoolSize(core, max);
        return ResponseEntity.ok("Async Pool Updated");
    }
}
