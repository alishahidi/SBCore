package net.alishahidi.sbcore.helloworld;

import net.alishahidi.sbcore.core.entity.BaseController;
import net.alishahidi.sbcore.core.entity.BaseService;
import net.alishahidi.sbcore.helloworld.dto.HelloWorldCreateDto;
import net.alishahidi.sbcore.helloworld.dto.HelloWorldLoadDto;
import net.alishahidi.sbcore.helloworld.dto.HelloWorldUpdateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello-world")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HelloWorldController extends BaseController<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> {

    HelloWorldService helloWorldService;

    @Override
    protected BaseService<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> getService() {
        return helloWorldService;
    }
}
