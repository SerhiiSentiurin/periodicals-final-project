package periodicals.epam.com.project.logic.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Prepayment;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeriodicalDAOTest {
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
    PeriodicalDAO dao;

    private static final String GET_COUNT_OF_ROWS_NAME = "SELECT COUNT(*) FROM periodical WHERE name LIKE ?";
    private static final String GET_COUNT_OF_ROWS_TOPIC = "SELECT COUNT(*) FROM periodical WHERE topic = ?";
    private static final String GET_COUNT_OF_ROWS_PERIODICAL = "SELECT COUNT(*) FROM periodical";
    private static final String GET_ALL_PERIODICALS = "SELECT * FROM periodical ORDER BY id LIMIT ?, 5";
    private static final String GET_PERIODICAL_BY_TOPIC = "SELECT * FROM periodical WHERE topic = ? LIMIT ?, 5";
    private static final String GET_PERIODICAL_BY_NAME = "SELECT * FROM periodical WHERE name LIKE ? LIMIT ?, 5";
    private static final String GET_PERIODICALS_BY_READER_ID = "SELECT * FROM periodical JOIN periodicals ON " +
            "periodical.id = periodicals.periodical_id WHERE reader_id =? ORDER BY periodical.id";
    private static final String GET_PERIODICAL_BY_TOPIC_BY_READER_ID = "SELECT * FROM periodical JOIN prepayment ON " +
            "periodical.id = prepayment.periodical_id WHERE reader_id =? AND topic =? ORDER BY periodical.id";
    private static final String GET_PREPAYMENTS_BY_READER_ID = "SELECT prepayment.id, start_date, due_date, periodical_id FROM " +
            "periodical JOIN prepayment ON periodical.id = prepayment.periodical_id WHERE reader_id =? ORDER BY periodical_id";
    private static final String FIND_PERIODICALS_BY_NAME_BY_READER_ID = "SELECT * FROM periodical JOIN prepayment ON " +
            "periodical.id = prepayment.periodical_id WHERE reader_id =? AND name LIKE ? ORDER BY periodical.id";
    private static final String GET_PERIODICALS_FOR_SUBSCRIBING = "SELECT distinct id, name, topic, cost, description, isDeleted FROM " +
            "periodical left join periodicals ON periodicals.periodical_id = periodical.id WHERE (reader_id IS NULL OR NOT periodical_id = 1) and isDeleted = false";
    private static final String GET_PERIODICALS_FOR_SUBSCRIBING_FOR_NEW_READER = "SELECT distinct id, name, topic, cost, description, " +
            "isDeleted FROM periodical left join periodicals ON periodicals.periodical_id = periodical.id WHERE isDeleted = false";
    private static final String FIND_PERIODICALS_FOR_SUBSCRIBING_BY_NAME = "SELECT distinct id, name, topic, cost, description, " +
            "isDeleted FROM periodical left join periodicals ON periodicals.periodical_id = periodical.id WHERE " +
            "(reader_id IS NULL OR NOT periodical_id = 1) AND isDeleted = false AND name LIKE ?";
    private static final String FIND_PERIODICALS_FOR_SUBSCRIBING_BY_NAME_FOR_NEW_READER = "SELECT distinct id, name, topic, " +
            "cost, description, isDeleted FROM periodical LEFT JOIN periodicals ON periodicals.periodical_id = periodical.id WHERE name LIKE ? AND isDeleted = false";
    private static final String GET_PERIODICAL_ID_BY_READER_ID = "SELECT periodical_id FROM periodicals WHERE reader_id = ? order by periodical_id";
    private static final Long PERIODICAL_ID = 1L;
    private static final Long READER_ID = 1L;
    private static final int INDEX = 1;
    private static final double ROWS = 3;
    private static final String TOPIC = "topic";
    private static final String NAME = "name";
    private final Periodical periodical1 = new Periodical(PERIODICAL_ID, "name1", "topic1", 10d, "description1", false);
    private final Periodical periodical2 = new Periodical(2L, "name2", "topic2", 20d, "description2", false);
    private final Prepayment prepayment1 = new Prepayment(1L, "startDate1", "dueDate1", PERIODICAL_ID, READER_ID);
    private final Prepayment prepayment2 = new Prepayment(2L, "startDate2", "dueDate2", 2L, 2L);

    @Before
    public void setConnection() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void getCountOfRowsPeriodicalTest() throws SQLException {
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_PERIODICAL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble(1)).thenReturn(ROWS);

        double result = dao.getCountOfRowsPeriodical();
        assertEquals(ROWS, result, 0d);
    }

    @Test
    public void getCountOfRowsPeriodicalEmptyTest() throws SQLException {
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_PERIODICAL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        double result = dao.getCountOfRowsPeriodical();
        assertEquals(0d, result, 0d);
    }

    @Test
    public void getCountOfRowsTopicTest() throws SQLException {
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_TOPIC)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble(1)).thenReturn(ROWS);

        double result = dao.getCountOfRowsTopic(TOPIC);
        assertEquals(ROWS, result, 0d);

        verify(preparedStatement).setString(1, TOPIC);
    }

    @Test
    public void getCountOfRowsTopicEmptyTest() throws SQLException {
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_TOPIC)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        double result = dao.getCountOfRowsTopic(TOPIC);
        assertEquals(0d, result, 0d);

        verify(preparedStatement).setString(1, TOPIC);
    }

    @Test
    public void getCountOfRowsNameTest() throws SQLException {
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_NAME)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble(1)).thenReturn(ROWS);

        double result = dao.getCountOfRowsName(NAME);
        assertEquals(ROWS, result, 0d);

        verify(preparedStatement).setString(1, "%" + NAME + "%");
    }

    @Test
    public void getCountOfRowsNameEmptyTest() throws SQLException {
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_NAME)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        double result = dao.getCountOfRowsName(NAME);
        assertEquals(0d, result, 0d);

        verify(preparedStatement).setString(1, "%" + NAME + "%");
    }

    @Test
    public void getAllPeriodicalsTest() throws SQLException {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);

        when(connection.prepareStatement(GET_ALL_PERIODICALS)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(PERIODICAL_ID).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name1").thenReturn("name2");
        when(resultSet.getString("topic")).thenReturn("topic1").thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(10d).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description1").thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false).thenReturn(false);

        List<Periodical> resultList = dao.getAllPeriodicals(INDEX);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setInt(1, INDEX);
    }

    @Test
    public void getAllPeriodicalsEmptyTest() throws SQLException {
        List<Periodical> expectedList = Collections.emptyList();

        when(connection.prepareStatement(GET_ALL_PERIODICALS)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Periodical> resultList = dao.getAllPeriodicals(INDEX);
        assertEquals(expectedList, resultList);
        verify(preparedStatement).setInt(1, INDEX);
    }

    @Test
    public void getPeriodicalByTopicTest() throws SQLException {
        List<Periodical> expectedList = new ArrayList<>();
        periodical1.setTopic("topic");
        periodical2.setTopic("topic");
        expectedList.add(periodical1);
        expectedList.add(periodical2);

        when(connection.prepareStatement(GET_PERIODICAL_BY_TOPIC)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(PERIODICAL_ID).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name1").thenReturn("name2");
        when(resultSet.getDouble("cost")).thenReturn(10d).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description1").thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalsByTopic("topic", INDEX);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setString(1, "topic");
        verify(preparedStatement).setInt(2, INDEX);
    }

    @Test
    public void getPeriodicalByTopicEmptyTest() throws SQLException {
        List<Periodical> expectedList = Collections.emptyList();

        when(connection.prepareStatement(GET_PERIODICAL_BY_TOPIC)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalsByTopic("topic", INDEX);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setString(1, "topic");
        verify(preparedStatement).setInt(2, INDEX);
    }

    @Test
    public void getPeriodicalByNameTest() throws SQLException {
        List<Periodical> expectedList = new ArrayList<>();
        periodical1.setName("name");
        periodical2.setName("name");
        expectedList.add(periodical1);
        expectedList.add(periodical2);

        when(connection.prepareStatement(GET_PERIODICAL_BY_NAME)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(PERIODICAL_ID).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name").thenReturn("name");
        when(resultSet.getString("topic")).thenReturn("topic1").thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(10d).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description1").thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalByName("name", INDEX);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setString(1, "%name%");
        verify(preparedStatement).setInt(2, INDEX);
    }

    @Test
    public void getPeriodicalByNameEmptyTest() throws SQLException {
        List<Periodical> expectedList = Collections.emptyList();

        when(connection.prepareStatement(GET_PERIODICAL_BY_NAME)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalByName("name", INDEX);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setString(1, "%name%");
        verify(preparedStatement).setInt(2, INDEX);
    }

    @Test
    public void getPeriodicalsByReaderIdTest() throws SQLException {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);

        when(connection.prepareStatement(GET_PERIODICALS_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("periodical.id")).thenReturn(PERIODICAL_ID).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name1").thenReturn("name2");
        when(resultSet.getString("topic")).thenReturn("topic1").thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(10d).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description1").thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalsByReaderId(READER_ID);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setLong(1, READER_ID);
    }

    @Test
    public void getPeriodicalsByReaderIdEmptyTest() throws SQLException {
        List<Periodical> expectedList = Collections.emptyList();

        when(connection.prepareStatement(GET_PERIODICALS_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalsByReaderId(READER_ID);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setLong(1, READER_ID);
    }

    @Test
    public void getPeriodicalsByTopicByReaderIdTest() throws SQLException {
        Map<Periodical, Prepayment> expectedMap = new HashMap<>();
        periodical1.setTopic("topic");
        periodical2.setTopic("topic");
        prepayment1.setReaderId(READER_ID);
        prepayment2.setReaderId(READER_ID);
        expectedMap.put(periodical1, prepayment1);
        expectedMap.put(periodical2, prepayment2);

        when(connection.prepareStatement(GET_PERIODICAL_BY_TOPIC_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("periodical.id")).thenReturn(PERIODICAL_ID).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name1").thenReturn("name2");
        when(resultSet.getDouble("cost")).thenReturn(10d).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description1").thenReturn("description2");
        when(resultSet.getLong("prepayment.id")).thenReturn(1L).thenReturn(2L);
        when(resultSet.getString("start_date")).thenReturn("startDate1").thenReturn("startDate2");
        when(resultSet.getString("due_date")).thenReturn("dueDate1").thenReturn("dueDate2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false).thenReturn(false);

        Map<Periodical, Prepayment> resultMap = dao.getPeriodicalsByTopicByReaderId("topic", READER_ID);
        assertEquals(expectedMap, resultMap);

        verify(preparedStatement).setLong(1, READER_ID);
        verify(preparedStatement).setString(2, "topic");
    }

    @Test
    public void getPeriodicalsByTopicByReaderIdEmptyTest() throws SQLException {
        Map<Periodical, Prepayment> expectedMap = Collections.emptyMap();

        when(connection.prepareStatement(GET_PERIODICAL_BY_TOPIC_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Map<Periodical, Prepayment> resultMap = dao.getPeriodicalsByTopicByReaderId("topic", READER_ID);
        assertEquals(expectedMap, resultMap);

        verify(preparedStatement).setLong(1, READER_ID);
        verify(preparedStatement).setString(2, "topic");
    }

    @Test
    public void getPrepaymentsByReaderIdTest() throws SQLException {
        List<Prepayment> expectedList = new ArrayList<>();
        prepayment1.setReaderId(READER_ID);
        prepayment2.setReaderId(READER_ID);
        expectedList.add(prepayment1);
        expectedList.add(prepayment2);

        when(connection.prepareStatement(GET_PREPAYMENTS_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(1L).thenReturn(2L);
        when(resultSet.getString("start_date")).thenReturn("startDate1").thenReturn("startDate2");
        when(resultSet.getString("due_date")).thenReturn("dueDate1").thenReturn("dueDate2");
        when(resultSet.getLong("periodical_id")).thenReturn(PERIODICAL_ID).thenReturn(2L);

        List<Prepayment> resultList = dao.getPrepaymentsByReaderId(READER_ID);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setLong(1, READER_ID);
    }

    @Test
    public void getPrepaymentsByReaderIdEmptyTest() throws SQLException {
        List<Prepayment> expectedList = Collections.emptyList();

        when(connection.prepareStatement(GET_PREPAYMENTS_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        List<Prepayment> resultList = dao.getPrepaymentsByReaderId(READER_ID);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setLong(1, READER_ID);
    }

    @Test
    public void findPeriodicalByNameByReaderIdTest() throws SQLException {
        Map<Periodical, Prepayment> expectedMap = new HashMap<>();
        periodical1.setName("name");
        periodical2.setName("name");
        prepayment1.setReaderId(READER_ID);
        prepayment2.setReaderId(READER_ID);
        expectedMap.put(periodical1, prepayment1);
        expectedMap.put(periodical2, prepayment2);

        when(connection.prepareStatement(FIND_PERIODICALS_BY_NAME_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("periodical.id")).thenReturn(PERIODICAL_ID).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name").thenReturn("name");
        when(resultSet.getString("topic")).thenReturn("topic1").thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(10d).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description1").thenReturn("description2");
        when(resultSet.getLong("prepayment.id")).thenReturn(1L).thenReturn(2L);
        when(resultSet.getString("start_date")).thenReturn("startDate1").thenReturn("startDate2");
        when(resultSet.getString("due_date")).thenReturn("dueDate1").thenReturn("dueDate2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false).thenReturn(false);

        Map<Periodical, Prepayment> resultMap = dao.findPeriodicalsByNameByReaderId("name", READER_ID);
        assertEquals(expectedMap, resultMap);

        verify(preparedStatement).setLong(1, READER_ID);
        verify(preparedStatement).setString(2, "%name%");

    }

    @Test
    public void findPeriodicalByNameByReaderIdEmptyTest() throws SQLException {
        Map<Periodical, Prepayment> expectedMap = Collections.emptyMap();

        when(connection.prepareStatement(FIND_PERIODICALS_BY_NAME_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Map<Periodical, Prepayment> resultMap = dao.findPeriodicalsByNameByReaderId("name", READER_ID);
        assertEquals(expectedMap, resultMap);

        verify(preparedStatement).setLong(1, READER_ID);
        verify(preparedStatement).setString(2, "%name%");

    }

    @Test
    public void getPeriodicalsForSubscribingTest() throws SQLException {
        List<Periodical> expectedList = new ArrayList<>();
        List<Long> listOfSubscribedPeriodicals = new ArrayList<>();
        listOfSubscribedPeriodicals.add(PERIODICAL_ID);
        expectedList.add(periodical2);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_PERIODICALS_FOR_SUBSCRIBING)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name2");
        when(resultSet.getString("topic")).thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalsForSubscribing(listOfSubscribedPeriodicals);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getPeriodicalsForSubscribingEmptyTest() throws SQLException {
        List<Periodical> expectedList = Collections.emptyList();
        List<Long> listOfSubscribedPeriodicals = new ArrayList<>();
        listOfSubscribedPeriodicals.add(PERIODICAL_ID);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_PERIODICALS_FOR_SUBSCRIBING)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalsForSubscribing(listOfSubscribedPeriodicals);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getPeriodicalsForSubscribingForNewReaderTest() throws SQLException {
        List<Periodical> expectedList = new ArrayList<>();
        List<Long> listOfSubscribedPeriodicals = Collections.emptyList();
        expectedList.add(periodical2);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_PERIODICALS_FOR_SUBSCRIBING_FOR_NEW_READER)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name2");
        when(resultSet.getString("topic")).thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalsForSubscribing(listOfSubscribedPeriodicals);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getPeriodicalsForSubscribingForNewReaderEmptyTest() throws SQLException {
        List<Periodical> expectedList = Collections.emptyList();
        List<Long> listOfSubscribedPeriodicals = Collections.emptyList();

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_PERIODICALS_FOR_SUBSCRIBING_FOR_NEW_READER)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Periodical> resultList = dao.getPeriodicalsForSubscribing(listOfSubscribedPeriodicals);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void findPeriodicalsForSubscribingByNameTest() throws SQLException {
        List<Periodical> expectedList = new ArrayList<>();
        List<Long> listOfSubscribedPeriodicals = new ArrayList<>();
        listOfSubscribedPeriodicals.add(PERIODICAL_ID);
        periodical2.setName("name");
        expectedList.add(periodical2);

        when(connection.prepareStatement(FIND_PERIODICALS_FOR_SUBSCRIBING_BY_NAME)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name");
        when(resultSet.getString("topic")).thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false);

        List<Periodical> resultList = dao.findPeriodicalsForSubscribingByName(listOfSubscribedPeriodicals, "name");
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setString(1, "%name%");
    }

    @Test
    public void findPeriodicalsForSubscribingByNameEmptyTest() throws SQLException {
        List<Periodical> expectedList = Collections.emptyList();
        List<Long> listOfSubscribedPeriodicals = new ArrayList<>();
        listOfSubscribedPeriodicals.add(PERIODICAL_ID);

        when(connection.prepareStatement(FIND_PERIODICALS_FOR_SUBSCRIBING_BY_NAME)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        List<Periodical> resultList = dao.findPeriodicalsForSubscribingByName(listOfSubscribedPeriodicals, "name");
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setString(1, "%name%");
    }

    @Test
    public void findPeriodicalsForSubscribingByNameForNewReaderTest() throws SQLException {
        List<Periodical> expectedList = new ArrayList<>();
        List<Long> listOfSubscribedPeriodicals = Collections.emptyList();
        periodical2.setName("name");
        expectedList.add(periodical2);

        when(connection.prepareStatement(FIND_PERIODICALS_FOR_SUBSCRIBING_BY_NAME_FOR_NEW_READER)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("name");
        when(resultSet.getString("topic")).thenReturn("topic2");
        when(resultSet.getDouble("cost")).thenReturn(20d);
        when(resultSet.getString("description")).thenReturn("description2");
        when(resultSet.getBoolean("isDeleted")).thenReturn(false);

        List<Periodical> resultList = dao.findPeriodicalsForSubscribingByName(listOfSubscribedPeriodicals, "name");
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setString(1, "%name%");
    }

    @Test
    public void findPeriodicalsForSubscribingByNameForNewReaderEmptyTest() throws SQLException {
        List<Periodical> expectedList = Collections.emptyList();
        List<Long> listOfSubscribedPeriodicals = Collections.emptyList();

        when(connection.prepareStatement(FIND_PERIODICALS_FOR_SUBSCRIBING_BY_NAME_FOR_NEW_READER)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Periodical> resultList = dao.findPeriodicalsForSubscribingByName(listOfSubscribedPeriodicals, "name");
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setString(1, "%name%");
    }

    @Test
    public void getPeriodicalIdByReaderIdTest() throws SQLException {
        List<Long> expectedList = new ArrayList<>();
        expectedList.add(PERIODICAL_ID);
        expectedList.add(2L);

        when(connection.prepareStatement(GET_PERIODICAL_ID_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("periodical_id")).thenReturn(PERIODICAL_ID).thenReturn(2L);

        List<Long> resultList = dao.getPeriodicalIdByReaderId(READER_ID);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setLong(1, READER_ID);
    }

    @Test
    public void getPeriodicalIdByReaderIdEmptyTest() throws SQLException {
        List<Long> expectedList = Collections.emptyList();

        when(connection.prepareStatement(GET_PERIODICAL_ID_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Long> resultList = dao.getPeriodicalIdByReaderId(READER_ID);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setLong(1, READER_ID);
    }
}
