package periodicals.epam.com.project.logic.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.entity.Account;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.dto.PeriodicalDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminDAOTest {
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private Statement statement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AdminDAO dao;

    private static final String GET_ALL_PERIODICALS = "SELECT * FROM periodical";
    private static final String CREATE_NEW_PERIODICAL = "INSERT INTO periodical (name, topic, cost, description) VALUES (?,?,?,?)";
    private static final String DELETE_PERIODICAL = "DELETE FROM periodical WHERE periodical.id = ?";
    private static final String DELETE_PERIODICAL_FOR_READERS = "UPDATE periodical SET isDeleted = true WHERE id = ?";
    private static final String RESTORE_PERIODICAL_FOR_READERS = "UPDATE periodical SET isDeleted = false WHERE id = ?";
    private static final String GET_PERIODICAL_BY_ID = "SELECT * FROM periodical WHERE id =?";
    private static final String EDIT_PERIODICAL_BY_ID = "UPDATE periodical SET name =?, topic =?, cost =?, description =? WHERE id =?";
    private static final String GET_ALL_READERS = "select user.id, login, account.amount, reader.lock, reader.account_id, "+
            "reader.id, periodical.id, periodical.name, periodical.topic, periodical.cost, periodical.description, periodical.isDeleted "+
            "from user JOIN reader ON user.id = reader.id JOIN account ON reader.account_id = account.id left join periodicals "+
            "on user.id=periodicals.reader_id left join periodical on periodicals.periodical_id = periodical.id where not user.role = 'ADMIN' order by user.id";
    private static final String LOCK_READER = "UPDATE reader SET reader.lock = true WHERE id = ?";
    private static final String UNLOCK_READER = "UPDATE reader SET reader.lock = false WHERE id = ?";
    private static final Long PERIODICAL_ID = 5L;
    private static final Long READER_ID = 1L;

    @Before
    public void setConnection() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void getAllPeriodicalsTest() throws SQLException {
        Periodical periodical1 = new Periodical();
        Periodical periodical2 = new Periodical();
        periodical1.setId(PERIODICAL_ID);
        periodical1.setName("name1");
        periodical1.setTopic("topic1");
        periodical1.setCost(10d);
        periodical1.setDescription("description1");
        periodical1.setIsDeleted(false);
        periodical2.setId(2L);
        periodical2.setName("name2");
        periodical2.setTopic("topic2");
        periodical2.setCost(20d);
        periodical2.setDescription("description2");
        periodical2.setIsDeleted(false);
        List<Periodical> expectedList = Arrays.asList(periodical1, periodical2);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_ALL_PERIODICALS)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(PERIODICAL_ID).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name1").thenReturn("name2");
        when(resultSet.getString("topic")).thenReturn("topic1").thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(10d).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description1").thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false).thenReturn(false);

        List<Periodical> resultList = dao.getAllPeriodicals();
        assertEquals(expectedList, resultList);
    }

    @Test
    public void createNewPeriodicalTest() throws SQLException {
        PeriodicalDTO dto = new PeriodicalDTO("name", "topic", 10d, "description", PERIODICAL_ID);

        when(connection.prepareStatement(CREATE_NEW_PERIODICAL, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        boolean result = dao.createNewPeriodical(dto);
        assertTrue(result);

        verify(preparedStatement).setString(1, dto.getName());
        verify(preparedStatement).setString(2, dto.getTopic());
        verify(preparedStatement).setDouble(3, dto.getCost());
        verify(preparedStatement).setString(4, dto.getDescription());
        verify(preparedStatement).execute();
    }

    @Test
    public void deletePeriodicalByPeriodicalIdTest() throws SQLException {
        when(connection.prepareStatement(DELETE_PERIODICAL)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        boolean result = dao.deletePeriodicalByPeriodicalId(PERIODICAL_ID);
        assertTrue(result);

        verify(preparedStatement).setLong(1, PERIODICAL_ID);
    }

    @Test
    public void deletePeriodicalForReadersTest() throws SQLException {
        when(connection.prepareStatement(DELETE_PERIODICAL_FOR_READERS)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        boolean result = dao.deletePeriodicalForReaders(PERIODICAL_ID);
        assertTrue(result);

        verify(preparedStatement).setLong(1, PERIODICAL_ID);
    }

    @Test
    public void restorePeriodicalForReadersTest() throws SQLException {
        when(connection.prepareStatement(RESTORE_PERIODICAL_FOR_READERS)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        boolean result = dao.restorePeriodicalForReaders(PERIODICAL_ID);
        assertTrue(result);

        verify(preparedStatement).setLong(1, PERIODICAL_ID);
    }

    @Test
    public void getPeriodicalByIdTest() throws SQLException {
        Periodical expectedPeriodical = new Periodical(PERIODICAL_ID, "name", "topic", 50d, "description", false);

        when(connection.prepareStatement(GET_PERIODICAL_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(PERIODICAL_ID);
        when(resultSet.getString("name")).thenReturn("name");
        when(resultSet.getString("topic")).thenReturn("topic");
        when(resultSet.getDouble("cost")).thenReturn(50d);
        when(resultSet.getString("description")).thenReturn("description");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false);

        Periodical resultPeriodical = dao.getPeriodicalById(PERIODICAL_ID);
        assertEquals(expectedPeriodical, resultPeriodical);
        assertNotNull(resultPeriodical);

        verify(preparedStatement).setLong(1, PERIODICAL_ID);
    }

    @Test
    public void editPeriodicalByIdTest() throws SQLException {
        PeriodicalDTO dto = new PeriodicalDTO("name", "topic", 50d, "description", PERIODICAL_ID);
        when(connection.prepareStatement(EDIT_PERIODICAL_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenReturn(true);

        boolean result = dao.editPeriodicalById(dto);
        assertTrue(result);

        verify(preparedStatement).setString(1, dto.getName());
        verify(preparedStatement).setString(2, dto.getTopic());
        verify(preparedStatement).setDouble(3, dto.getCost());
        verify(preparedStatement).setString(4, dto.getDescription());
        verify(preparedStatement).setLong(5, dto.getPeriodicalId());

    }

    @Test
    public void getAllReadersTest() throws SQLException {
        Account account1 = new Account(1L, 50d);
        Account account2 = new Account(2L, 60d);
        Reader reader1 = new Reader();
        Reader reader2 = new Reader();
        reader1.setId(3L);
        reader1.setLogin("login1");
        reader1.setAccount(account1);
        reader1.setLock(false);
        reader2.setId(4L);
        reader2.setLogin("login2");
        reader2.setAccount(account2);
        reader2.setLock(false);
        Periodical periodical1 = new Periodical(1L,"name1","topic1", 50d, "description1", false);
        Periodical periodical2 = new Periodical(2L,"name2","topic2", 50d, "description2", false);
        Map<Reader, Periodical> expectedMap = new LinkedHashMap<>();
        expectedMap.put(reader1,periodical1);
        expectedMap.put(reader2,periodical2);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_ALL_READERS)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("login")).thenReturn("login1").thenReturn("login2");
        when(resultSet.getDouble("amount")).thenReturn(50d).thenReturn(60d);
        when(resultSet.getBoolean("lock")).thenReturn(false).thenReturn(false);
        when(resultSet.getLong("account_id")).thenReturn(1L).thenReturn(2L);
        when(resultSet.getLong("reader.id")).thenReturn(3L).thenReturn(4L);
        when(resultSet.getLong("periodical.id")).thenReturn(1L).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name1").thenReturn("name2");
        when(resultSet.getString("topic")).thenReturn("topic1").thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(50d).thenReturn(50d);
        when(resultSet.getString("description")).thenReturn("description1").thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false).thenReturn(false);

        Map<Reader, Periodical> resultMap = dao.getAllReaders();
        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void lockReaderTest() throws SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(LOCK_READER)).thenReturn(preparedStatement);

        boolean result = dao.lockReader(READER_ID);
        assertTrue(result);

        verify(preparedStatement).setLong(1, READER_ID);
    }

    @Test
    public void unlockReaderTest() throws SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(UNLOCK_READER)).thenReturn(preparedStatement);

        boolean result = dao.unlockReader(READER_ID);
        assertTrue(result);

        verify(preparedStatement).setLong(1, READER_ID);
    }
}
