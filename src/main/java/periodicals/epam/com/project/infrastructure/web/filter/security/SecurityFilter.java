package periodicals.epam.com.project.infrastructure.web.filter.security;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import periodicals.epam.com.project.logic.entity.User;
import periodicals.epam.com.project.logic.entity.UserRole;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecurityFilter implements Filter {

    private List<RequestMatcher> pathMatchers;

    @Override
    public void init(FilterConfig filterConfig) {
        List<RequestMatcher> pathMatchers = new ArrayList<>();

        pathMatchers.add(new RequestMatcher("/admin/adminHome.jsp", UserRole.ADMIN));
        pathMatchers.add(new RequestMatcher("/admin/editPeriodical.jsp", UserRole.ADMIN));
        pathMatchers.add(new RequestMatcher("/admin/managePeriodical.jsp", UserRole.ADMIN));
        pathMatchers.add(new RequestMatcher("/admin/manageReaders.jsp", UserRole.ADMIN));
        pathMatchers.add(new RequestMatcher("/periodicals/admin/managePeriodicals", UserRole.ADMIN));
        pathMatchers.add(new RequestMatcher("/periodicals/admin/manageReaders", UserRole.ADMIN));
        pathMatchers.add(new RequestMatcher("/periodicals/admin/getPeriodicalForEdit", UserRole.ADMIN));
        pathMatchers.add(new RequestMatcher("/reader/readerHome.jsp", UserRole.READER));
        pathMatchers.add(new RequestMatcher("/periodicals/periodical/readerSubscriptions", UserRole.READER));
        pathMatchers.add(new RequestMatcher("/periodicals/periodical/periodicalsForSubscribing", UserRole.READER));
        pathMatchers.add(new RequestMatcher("/periodicals/account/getAccountInfo", UserRole.READER));
        pathMatchers.add(new RequestMatcher("/periodicals/periodical/getByTopicReaderSubscriptions", UserRole.READER));
        pathMatchers.add(new RequestMatcher("/periodicals/periodical/findByNameReaderSubscriptions", UserRole.READER));
        pathMatchers.add(new RequestMatcher("/periodicals/periodical/getByTopicPeriodicalsForSubscribing", UserRole.READER));
        pathMatchers.add(new RequestMatcher("/periodicals/periodical/findByNamePeriodicalsForSubscribing", UserRole.READER));
        pathMatchers.add(new RequestMatcher("/periodical/periodicalsForSubscribing.jsp", UserRole.READER));
        pathMatchers.add(new RequestMatcher("/periodical/watchSubscriptions.jsp", UserRole.READER));

        this.pathMatchers = pathMatchers;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String pathWithoutContext = getPathWithoutContext(request);

        Boolean hasAccess = pathMatchers.stream()
                .filter(authorizationPathMatcher -> authorizationPathMatcher.pathMatch(pathWithoutContext))
                .findFirst()
                .map(authorizationPathMatcher -> hasRole(authorizationPathMatcher, request))
                .orElse(true);

        if (hasAccess) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/error/forbidden.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    public void destroy() {
        pathMatchers.clear();
    }

    private String getPathWithoutContext(HttpServletRequest httpServletRequest) {
        int contextPathLength = httpServletRequest.getContextPath().length();
        return httpServletRequest.getRequestURI().substring(contextPathLength);
    }

    private boolean hasRole(RequestMatcher authorizationMatcher, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && authorizationMatcher.hasRole((User) session.getAttribute("user"));
    }
}
