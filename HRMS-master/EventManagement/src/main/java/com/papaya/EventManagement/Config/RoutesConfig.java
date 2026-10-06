package com.papaya.EventManagement.Config;


import com.papaya.EventManagement.DTO.RouteDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RoutesConfig {

    @Autowired
    private ApplicationContext applicationContext;

    private List<RouteDto> routes = new ArrayList<>();

    @PostConstruct
    public void init() {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, ?> handlerMethods = handlerMapping.getHandlerMethods();

        for (RequestMappingInfo info : handlerMethods.keySet()) {
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            Set<String> patterns = info.getPathPatternsCondition().getPatternValues();

            for (RequestMethod method : methods) {
                for (String path : patterns) {
                    routes.add(new RouteDto(method.name(), path));
                }
            }
        }
    }

    public List<RouteDto> getRoutes() {
        return routes;
    }
}

