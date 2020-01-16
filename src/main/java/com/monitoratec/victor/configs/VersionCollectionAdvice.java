package com.monitoratec.victor.configs;

import com.monitoratec.victor.models.IVersioned;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.stream.Collectors;

@ControllerAdvice
public class VersionCollectionAdvice implements IVersionAdvice, ResponseBodyAdvice<Collection<IVersioned>> {
    @Override
    public boolean supports(MethodParameter param, Class<? extends HttpMessageConverter<?>> clazz) {
        // TODO: check for Arrays to? Can we merge all advices into one?
        if (param.getGenericParameterType() instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) param.getGenericParameterType();
            Class<?> paramClazz = (Class<?>) type.getActualTypeArguments()[0];
            return this.hasVersionedInterface(paramClazz);
        }

        return false;
    }

    @Override
    public Collection<IVersioned> beforeBodyWrite(
            Collection<IVersioned> model,
            MethodParameter parm,
            MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass,
            ServerHttpRequest httpRequest,
            ServerHttpResponse httpResponse
    ) {
        int version = this.getVersion(httpRequest.getURI().getPath());
        return model.stream()
                .map(m -> m.toVersion(version))
                .collect(Collectors.toList());
    }
}
