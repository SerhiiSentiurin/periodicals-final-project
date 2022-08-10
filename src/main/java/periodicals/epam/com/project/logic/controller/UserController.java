package periodicals.epam.com.project.logic.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import periodicals.epam.com.project.infrastructure.web.ModelAndView;
import periodicals.epam.com.project.infrastructure.web.QueryParameterHandler;
import periodicals.epam.com.project.logic.entity.User;
import periodicals.epam.com.project.logic.entity.UserRole;
import periodicals.epam.com.project.logic.entity.dto.UserDTO;
import periodicals.epam.com.project.logic.services.UserService;

import java.util.Locale;
import java.util.Map;


@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final QueryParameterHandler queryParameterHandler;
    private final Map<UserRole, String> mapView;

    public ModelAndView login(HttpServletRequest request) {
        UserDTO userDto = queryParameterHandler.handleRequest(request, UserDTO.class);
        User userByLogin = userService.getUserByLogin(userDto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(mapView.get(userByLogin.getUserRole()));
        HttpSession session = request.getSession(true);
        session.setAttribute("user", userByLogin);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/index.jsp");
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView changeLocale(HttpServletRequest request) {
        String selectedLocale = request.getParameter("selectedLocale");
        String view = request.getParameter("view");
        Locale locale = new Locale(selectedLocale);
        HttpSession session = request.getSession(false);
        session.setAttribute("selectedLocale", locale);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(view);
        modelAndView.setRedirect(true);
        return modelAndView;
    }
}
