package com.monitoratec.victor.configs;

import com.monitoratec.victor.models.IVersioned;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class VersionDTOAdvice implements IVersionAdvice, ResponseBodyAdvice<IVersioned> {
    @Override
    public boolean supports(MethodParameter param, Class<? extends HttpMessageConverter<?>> clazz) {
        if (param.getGenericParameterType() instanceof Class) {
            Class<?> cls = (Class<?>) param.getGenericParameterType();
            return this.hasVersionedInterface(cls);
        }

        return false;
    }

    @Override
    public IVersioned beforeBodyWrite(
            IVersioned model,
            MethodParameter parm,
            MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass,
            ServerHttpRequest httpRequest,
            ServerHttpResponse httpResponse
    ) {
        int version = this.getVersion(httpRequest.getURI().getPath());
        return model.toVersion(version);
    }
}
