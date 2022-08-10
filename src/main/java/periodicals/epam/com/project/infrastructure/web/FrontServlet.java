package periodicals.epam.com.project.infrastructure.web;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import periodicals.epam.com.project.infrastructure.web.exception.ExceptionHandler;


import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class FrontServlet extends HttpServlet {
    @Getter
    private final String servletName;
    private final DispatcherRequest dispatcherRequest;
    private final ExceptionHandler exceptionHandler;
    private final ProcessorModelAndView processorModelAndView;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("start processing request");
        ModelAndView modelAndView;
        try {
            modelAndView = dispatcherRequest.processRequest(req);
        } catch (Exception exception) {
            modelAndView = exceptionHandler.handle(exception);
            log.info("Set view for handled exception: " + modelAndView.getView());
        }
        processorModelAndView.processModelAndView(req, resp, modelAndView, this);
    }
}
