package ignore;

import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

//@Component
class CustomBackendIdConverter implements BackendIdConverter {


    @Override
    public Serializable fromRequestId(String s, Class<?> aClass) {
        return null;
    }

    @Override
    public String toRequestId(Serializable serializable, Class<?> aClass) {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
