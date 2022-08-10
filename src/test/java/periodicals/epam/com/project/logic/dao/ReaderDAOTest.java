package periodicals.epam.com.project.logic.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.entity.Account;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.dto.ReaderCreateDTO;
import periodicals.epam.com.project.logic.logicExeption.ReaderException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReaderDAOTest {
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private PreparedStatement preparedStatement1;
    @Mock
    private ResultSet resultSet;
    @Mock
    private ResultSet resultSet1;
    @Mock
    private Statement statement;

    @InjectMocks
    private ReaderDAO dao;

    private static final Long READER_ID = 1L;
    private static final Long ACCOUNT_ID = 1L;
    private static final String GET_READER_BY_ID = "SELECT user.login, reader.account_id, reader.lock, account.amount FROM "+
            "user JOIN reader ON user.id = reader.id JOIN account ON reader.account_id = account.id WHERE user.id = ?";
    private static final String INSERT_INTO_USER = "INSERT INTO user (login, password, role) VALUES (?,?,?)";
    private static final String INSERT_INTO_ACCOUNT = "INSERT INTO account (amount) VALUE (0)";
    private static final String INSERT_INTO_READER = "INSERT INTO READER (id,account_id) values (?,?)";

    @Before
    public void setConnection() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void getReaderByIdTest() throws SQLException {
        Account expectedAccount = new Account(ACCOUNT_ID,50d);
        Reader expectedReader = new Reader();
        expectedReader.setId(READER_ID);
        expectedReader.setLogin("login");
        expectedReader.setLock(false);
        expectedReader.setAccount(expectedAccount);
        Optional<Reader> expectedOptional = Optional.of(expectedReader);

        when(connection.prepareStatement(GET_READER_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("login")).thenReturn("login");
        when(resultSet.getBoolean("lock")).thenReturn(false);
        when(resultSet.getLong("account_id")).thenReturn(ACCOUNT_ID);
        when(resultSet.getDouble("amount")).thenReturn(50d);

        Optional<Reader> resultOptional = dao.getReaderById(READER_ID);
        assertEquals(expectedOptional,resultOptional);

        verify(preparedStatement).setLong(1,READER_ID);
    }

    @Test
    public void getReaderByIdEmptyTest() throws SQLException {
        Optional<Reader> expectedOptional = Optional.empty();

        when(connection.prepareStatement(GET_READER_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Reader> resultOptional = dao.getReaderById(READER_ID);
        assertEquals(expectedOptional,resultOptional);
    }

    @Test
    public void insertReaderWhenTransactionCommit()throws SQLException{
        ReaderCreateDTO expectedDto = new ReaderCreateDTO(READER_ID,"login","password");
        when(connection.prepareStatement(INSERT_INTO_USER,Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(READER_ID);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getLong(1)).thenReturn(READER_ID);
        when(connection.prepareStatement(INSERT_INTO_READER)).thenReturn(preparedStatement1);

        ReaderCreateDTO resultDto = dao.insertReader(expectedDto);
        assertEquals(expectedDto,resultDto);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setString(1,expectedDto.getLogin());
        verify(preparedStatement).setString(2,expectedDto.getPassword());
        verify(preparedStatement).setString(3,expectedDto.getUserRole().toString());
        verify(preparedStatement).execute();
        verify(statement).execute(INSERT_INTO_ACCOUNT,Statement.RETURN_GENERATED_KEYS);
        verify(resultSet).next();
        verify(preparedStatement1).setLong(1,READER_ID);
        verify(preparedStatement1).setLong(2,ACCOUNT_ID);
        verify(preparedStatement1).execute();
        verify(connection).commit();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test(expected = ReaderException.class)
    public void insertReaderWhenTransactionRollbackTest()throws SQLException{
        ReaderCreateDTO expectedDto = new ReaderCreateDTO(READER_ID,"login","password");
        when(connection.prepareStatement(INSERT_INTO_USER,Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenThrow(ReaderException.class);
        dao.insertReader(expectedDto);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setString(1,expectedDto.getLogin());
        verify(preparedStatement).setString(2,expectedDto.getPassword());
        verify(preparedStatement).setString(3,expectedDto.getUserRole().toString());
        verify(preparedStatement).execute();
        verify(preparedStatement).close();
        verify(connection).close();
        verify(connection).rollback();

    }
}
