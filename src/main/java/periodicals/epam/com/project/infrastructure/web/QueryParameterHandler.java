package periodicals.epam.com.project.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.Enumeration;
import java.util.HashMap;

@RequiredArgsConstructor
public class QueryParameterHandler {
    private final ObjectMapper objectMapper;

    public <T> T handleRequest(HttpServletRequest request, Class<T> tClass) {
        HashMap<String, String> parameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String nameOfParameter = parameterNames.nextElement();
            String parameter = request.getParameter(nameOfParameter);
            parameters.put(nameOfParameter, parameter);
        }
        return objectMapper.convertValue(parameters, tClass);
    }
}
