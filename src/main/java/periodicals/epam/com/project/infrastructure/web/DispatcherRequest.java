package periodicals.epam.com.project.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import periodicals.epam.com.project.infrastructure.web.exception.ApplicationException;


import java.util.List;

@RequiredArgsConstructor
public class DispatcherRequest {
    private final List<Placeholder> placeholders;

    public ModelAndView processRequest(HttpServletRequest request) {
        return placeholders.stream()
                .filter(placeholder -> placeholder.getMethod().equals(request.getMethod()))
                .filter(placeholder -> placeholder.getAction().equals(request.getHttpServletMapping().getMatchValue()))
                .findFirst()
                .map(Placeholder::getFunction)
                .map(function -> function.apply(request))
                .orElseThrow(() -> new ApplicationException("page not found: " + request.getRequestURL()));
    }
}
