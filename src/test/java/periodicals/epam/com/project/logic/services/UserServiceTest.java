package periodicals.epam.com.project.logic.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.infrastructure.web.exception.ApplicationException;
import periodicals.epam.com.project.logic.dao.UserDAO;
import periodicals.epam.com.project.logic.entity.Admin;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.User;
import periodicals.epam.com.project.logic.entity.dto.UserDTO;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserDAO dao;

    @InjectMocks
    private UserService userService;

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final UserDTO dto = new UserDTO(LOGIN,PASSWORD);

    @Test
    public void getUserByLoginWhenUserIsReaderTest(){
        User expectedReader = new Reader();
        expectedReader.setLogin(LOGIN);
        expectedReader.setPassword(PASSWORD);

        when(dao.getUserByLogin(LOGIN)).thenReturn(Optional.of(expectedReader));

        User resultReader = userService.getUserByLogin(dto);
        assertEquals(expectedReader,resultReader);
    }

    @Test
    public void getUserByLoginWhenUserIsAdminTest(){
        User expectedAdmin = new Admin();
        expectedAdmin.setLogin(LOGIN);
        expectedAdmin.setPassword(PASSWORD);

        when(dao.getUserByLogin(LOGIN)).thenReturn(Optional.of(expectedAdmin));

        User resultAdmin = userService.getUserByLogin(dto);
        assertEquals(expectedAdmin,resultAdmin);
    }

    @Test(expected = ApplicationException.class)
    public void getUserByLoginWhenUserNotFound(){
        when(dao.getUserByLogin(LOGIN)).thenReturn(Optional.empty());
        userService.getUserByLogin(dto);
    }

    @Test(expected = ApplicationException.class)
    public void getUserBuLoginWhenPasswordIncorrect(){
        User user = new Reader();
        user.setLogin(LOGIN);
        user.setPassword("asdf");

        when(dao.getUserByLogin(LOGIN)).thenReturn(Optional.of(user));
        userService.getUserByLogin(dto);
    }
}
