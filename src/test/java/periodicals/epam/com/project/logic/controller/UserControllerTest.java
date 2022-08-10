package periodicals.epam.com.project.logic.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.infrastructure.web.ModelAndView;
import periodicals.epam.com.project.infrastructure.web.QueryParameterHandler;
import periodicals.epam.com.project.logic.entity.Admin;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.User;
import periodicals.epam.com.project.logic.entity.UserRole;
import periodicals.epam.com.project.logic.entity.dto.UserDTO;
import periodicals.epam.com.project.logic.services.UserService;

import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private QueryParameterHandler queryParameterHandler;
    @Mock
    private Map<UserRole,String> mapView;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @InjectMocks
    private UserController userController;

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String VIEW = "/index.jsp";
    private static final String LOCALE = "selectedLocale";
    private static final UserDTO userDto = new UserDTO(LOGIN,PASSWORD);

    @Test
    public void loginForReaderTest(){
        User expectedReader = Mockito.mock(Reader.class);
        when(queryParameterHandler.handleRequest(request,UserDTO.class)).thenReturn(userDto);
        when(userService.getUserByLogin(userDto)).thenReturn(expectedReader);
        when(mapView.get(expectedReader.getUserRole())).thenReturn("/reader/readerHome.jsp");
        when(request.getSession(true)).thenReturn(session);

        ModelAndView modelAndView = userController.login(request);
        assertNotNull(modelAndView);
        assertEquals("/reader/readerHome.jsp",modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request).getSession(true);
        verify(session).setAttribute("user",expectedReader);
    }

    @Test
    public void loginForAdminTest(){
        User expectedAdmin = Mockito.mock(Admin.class);
        when(queryParameterHandler.handleRequest(request,UserDTO.class)).thenReturn(userDto);
        when(userService.getUserByLogin(userDto)).thenReturn(expectedAdmin);
        when(mapView.get(expectedAdmin.getUserRole())).thenReturn("/admin/adminHome.jsp");
        when(request.getSession(true)).thenReturn(session);

        ModelAndView modelAndView = userController.login(request);
        assertNotNull(modelAndView);
        assertEquals("/admin/adminHome.jsp",modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request).getSession(true);
        verify(session).setAttribute("user",expectedAdmin);
    }

    @Test
    public void logoutTest(){
        when(request.getSession(false)).thenReturn(session);
        ModelAndView modelAndView = userController.logout(request);
        assertNotNull(modelAndView);
        assertEquals("/index.jsp",modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void changeLocaleTest(){
        when(request.getParameter("selectedLocale")).thenReturn(LOCALE);
        when(request.getParameter("view")).thenReturn(VIEW);
        when(request.getSession(false)).thenReturn(session);
        Locale expectedLocale = new Locale(LOCALE);

        ModelAndView modelAndView = userController.changeLocale(request);
        assertNotNull(modelAndView);
        assertEquals(VIEW,modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(session).setAttribute("selectedLocale",expectedLocale);
        verify(request).getSession(false);
    }
}
