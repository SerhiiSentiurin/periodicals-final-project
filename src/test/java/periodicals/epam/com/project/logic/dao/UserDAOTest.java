package periodicals.epam.com.project.logic.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.entity.Admin;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.User;
import periodicals.epam.com.project.logic.entity.UserRole;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDAOTest {
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private UserDAO dao;

    private static final String GET_USER_BY_LOGIN = "SELECT * FROM user WHERE login=?";
    private static final Long ID = 1L;
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(GET_USER_BY_LOGIN)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getLong("id")).thenReturn(ID);
        when(resultSet.getString("password")).thenReturn(PASSWORD);
    }

    @Test
    public void getUserByLoginWhenReturnReader() throws SQLException {
        User expectedReader = new Reader();
        expectedReader.setId(ID);
        expectedReader.setLogin(LOGIN);
        expectedReader.setPassword(PASSWORD);
        expectedReader.setUserRole(UserRole.READER);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("role")).thenReturn("READER");

        Optional<User> resultReader = dao.getUserByLogin(LOGIN);
        assertNotNull(resultReader);
        assertTrue(resultReader.isPresent());
        assertEquals(expectedReader,resultReader.get());

        verify(preparedStatement).setString(1,LOGIN);
    }

    @Test
    public void getUserByLoginWhenReturnAdmin() throws SQLException {
        User expectedAdmin = new Admin();
        expectedAdmin.setId(ID);
        expectedAdmin.setLogin(LOGIN);
        expectedAdmin.setPassword(PASSWORD);
        expectedAdmin.setUserRole(UserRole.ADMIN);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("role")).thenReturn("ADMIN");

        Optional<User> resultAdmin = dao.getUserByLogin(LOGIN);
        assertNotNull(resultAdmin);
        assertTrue(resultAdmin.isPresent());
        assertEquals(expectedAdmin,resultAdmin.get());

        verify(preparedStatement).setString(1,LOGIN);
    }

    @Test
    public void getGetUserByLoginWhenUserNotFound()throws SQLException{
        when(resultSet.next()).thenReturn(false);

        Optional<User> resultUser = dao.getUserByLogin(LOGIN);
        assertEquals(Optional.empty(),resultUser);
        assertFalse(resultUser.isPresent());
        assertNotNull(resultUser);

        verify(preparedStatement).setString(1,LOGIN);
    }

}
