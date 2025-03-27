package io.github.alishahidi.sbcore.search;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

public class SearchParamsArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SearchParams.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        SearchParams annotation = parameter.getParameterAnnotation(SearchParams.class);
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        int page = getIntParam(request, "page", annotation.defaultPage());
        int size = getIntParam(request, "size", annotation.defaultSize());
        String[] sort = request.getParameterValues("sort");
        if (sort == null || sort.length == 0) {
            sort = annotation.defaultSort();
        }

        MultiValueMap<String, String> filters = new LinkedMultiValueMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (!"page".equals(key) && !"size".equals(key) && !"sort".equals(key)) {
                filters.addAll(key, Arrays.asList(values));
            }
        });

        return new SearchParamsWrapper(page, size, sort, filters);
    }

    private int getIntParam(HttpServletRequest request, String paramName, int defaultValue) {
        String paramValue = request.getParameter(paramName);
        if (paramValue != null) {
            try {
                return Integer.parseInt(paramValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid " + paramName + " parameter");
            }
        }
        return defaultValue;
    }
}