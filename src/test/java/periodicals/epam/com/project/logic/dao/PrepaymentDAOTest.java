package periodicals.epam.com.project.logic.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.entity.Account;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.dto.PrepaymentDTO;
import periodicals.epam.com.project.logic.logicExeption.ReaderException;

import javax.sql.DataSource;
import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrepaymentDAOTest {
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private PreparedStatement preparedStatement1;
    @Mock
    private PreparedStatement preparedStatement2;
    @Mock
    private PreparedStatement preparedStatement3;
    @Mock
    private Statement statement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private PrepaymentDAO dao;

    private static final Long READER_ID = 1L;
    private static final Long PERIODICAL_ID = 1L;
    private static final Long ACCOUNT_ID = 1L;
    private static final PrepaymentDTO dto = new PrepaymentDTO(30, READER_ID,PERIODICAL_ID,50d);
    private static final String ADD_SUBSCRIPTION = "INSERT INTO periodicals (reader_id, periodical_id) VALUES (?,?)";
    private static final String UPDATE_ACCOUNT = "UPDATE account INNER JOIN reader ON account.id = reader.account_id SET "+
            "account.amount = ? WHERE reader.id = " + dto.getReaderId();
    private static final String UPDATE_PREPAYMENT = "INSERT INTO prepayment (start_date, due_date, periodical_id, reader_id) VALUES "+
            "(curdate(), adddate(start_date, INTERVAL " + dto.getDurationOfSubscription() +" DAY), " + dto.getPeriodicalId() + ", " + dto.getReaderId() + ");";
    private static final String DELETE_FROM_PERIODICALS = "DELETE FROM periodicals WHERE reader_id = ? AND periodical_id = ?";
    private static final String DELETE_FROM_PREPAYMENTS = "DELETE FROM prepayment WHERE reader_id = ? AND periodical_id = ?";
    private static final String GET_PERIODICAL_COST = "SELECT cost FROM periodical WHERE id = ?";
    private static final String GET_AMOUNT_FROM_ACCOUNT = "SELECT account.id, amount FROM account JOIN reader ON reader.account_id = account.id WHERE reader.id = ?";

    @Before
    public void setConnection() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void addSubscriptionWhenTransactionCommitTest()throws SQLException {
        when(connection.prepareStatement(ADD_SUBSCRIPTION)).thenReturn(preparedStatement);
        when(connection.prepareStatement(UPDATE_ACCOUNT)).thenReturn(preparedStatement1);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.execute(UPDATE_PREPAYMENT)).thenReturn(true);

        Periodical expectedPeriodical = new Periodical();
        expectedPeriodical.setCost(50d);
        when(connection.prepareStatement(GET_PERIODICAL_COST)).thenReturn(preparedStatement2);
        when(preparedStatement2.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble("cost")).thenReturn(expectedPeriodical.getCost());
        Double resultCost = dao.getPeriodicalCost(PERIODICAL_ID);
        assertEquals(expectedPeriodical.getCost(),resultCost);
        verify(preparedStatement2).setLong(1,PERIODICAL_ID);

        Account expectedAccount = new Account();
        expectedAccount.setId(ACCOUNT_ID);
        expectedAccount.setAmountOfMoney(50d);
        when(connection.prepareStatement(GET_AMOUNT_FROM_ACCOUNT)).thenReturn(preparedStatement3);
        when(preparedStatement3.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ACCOUNT_ID);
        when(resultSet.getDouble("amount")).thenReturn(expectedAccount.getAmountOfMoney());
        Double resultAmount = dao.getAmountFromAccount(READER_ID);
        assertEquals(expectedAccount.getAmountOfMoney(),resultAmount);
        verify(preparedStatement3).setLong(1,READER_ID);

        dto.setDurationOfSubscription(30);
        dao.checkAccountAmount(dto);

        PrepaymentDTO resultDto = dao.addSubscription(dto);
        assertNotNull(resultDto);
        assertEquals(dto,resultDto);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setLong(1,dto.getReaderId());
        verify(preparedStatement).setLong(2,dto.getPeriodicalId());
        verify(preparedStatement).execute();
        verify(preparedStatement1).setDouble(1,dto.getAmountOfMoney());
        verify(preparedStatement1).execute();
        verify(statement).execute(UPDATE_PREPAYMENT);
        verify(connection).commit();
        verify(preparedStatement).close();
    }

    @Test(expected = ReaderException.class)
    public void addSubscriptionWhenTransactionRollbackTest()throws SQLException{
        when(connection.prepareStatement(ADD_SUBSCRIPTION)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenThrow(ReaderException.class);

        Periodical expectedPeriodical = new Periodical();
        expectedPeriodical.setCost(50d);
        when(connection.prepareStatement(GET_PERIODICAL_COST)).thenReturn(preparedStatement2);
        when(preparedStatement2.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble("cost")).thenReturn(expectedPeriodical.getCost());
        Double resultCost = dao.getPeriodicalCost(PERIODICAL_ID);
        assertEquals(expectedPeriodical.getCost(),resultCost);
        verify(preparedStatement2).setLong(1,PERIODICAL_ID);

        Account expectedAccount = new Account();
        expectedAccount.setId(ACCOUNT_ID);
        expectedAccount.setAmountOfMoney(50d);
        when(connection.prepareStatement(GET_AMOUNT_FROM_ACCOUNT)).thenReturn(preparedStatement3);
        when(preparedStatement3.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ACCOUNT_ID);
        when(resultSet.getDouble("amount")).thenReturn(expectedAccount.getAmountOfMoney());
        Double resultAmount = dao.getAmountFromAccount(READER_ID);
        assertEquals(expectedAccount.getAmountOfMoney(),resultAmount);
        verify(preparedStatement3).setLong(1,READER_ID);

        dao.checkAccountAmount(dto);
        dao.addSubscription(dto);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setLong(1,dto.getReaderId());
        verify(preparedStatement).setLong(2,dto.getPeriodicalId());
        verify(preparedStatement).execute();
        verify(preparedStatement).close();
        verify(connection).close();
        verify(connection).rollback();
    }

    @Test
    public void checkAccountAmount()throws SQLException{
        Periodical expectedPeriodical = new Periodical();
        expectedPeriodical.setCost(50d);
        when(connection.prepareStatement(GET_PERIODICAL_COST)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble("cost")).thenReturn(expectedPeriodical.getCost());
        Double resultCost = dao.getPeriodicalCost(PERIODICAL_ID);
        assertEquals(expectedPeriodical.getCost(),resultCost);
        verify(preparedStatement).setLong(1,PERIODICAL_ID);

        Account expectedAccount = new Account();
        expectedAccount.setId(ACCOUNT_ID);
        expectedAccount.setAmountOfMoney(50d);
        when(connection.prepareStatement(GET_AMOUNT_FROM_ACCOUNT)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ACCOUNT_ID);
        when(resultSet.getDouble("amount")).thenReturn(expectedAccount.getAmountOfMoney());
        Double resultAmount = dao.getAmountFromAccount(READER_ID);
        assertEquals(expectedAccount.getAmountOfMoney(),resultAmount);
        verify(preparedStatement1).setLong(1,READER_ID);

        dao.checkAccountAmount(dto);
    }

    @Test(expected = ReaderException.class)
    public void checkAccountAmountWhenNotEnoughMoney()throws SQLException{
        Periodical expectedPeriodical = new Periodical();
        expectedPeriodical.setCost(50d);
        when(connection.prepareStatement(GET_PERIODICAL_COST)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble("cost")).thenReturn(expectedPeriodical.getCost());
        Double resultCost = dao.getPeriodicalCost(PERIODICAL_ID);
        assertEquals(expectedPeriodical.getCost(),resultCost);
        verify(preparedStatement).setLong(1,PERIODICAL_ID);

        Account expectedAccount = new Account();
        expectedAccount.setId(ACCOUNT_ID);
        expectedAccount.setAmountOfMoney(10d);
        when(connection.prepareStatement(GET_AMOUNT_FROM_ACCOUNT)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ACCOUNT_ID);
        when(resultSet.getDouble("amount")).thenReturn(expectedAccount.getAmountOfMoney());
        Double resultAmount = dao.getAmountFromAccount(READER_ID);
        assertEquals(expectedAccount.getAmountOfMoney(),resultAmount);
        verify(preparedStatement1).setLong(1,READER_ID);

        dao.checkAccountAmount(dto);
    }

    @Test(expected = ReaderException.class)
    public void checkAccountAmountWhenNotEnoughMoneyForYearSubscription()throws SQLException{
        Periodical expectedPeriodical = new Periodical();
        expectedPeriodical.setCost(50d);
        Double yearCost = expectedPeriodical.getCost()*12*0.9;
        when(connection.prepareStatement(GET_PERIODICAL_COST)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble("cost")).thenReturn(yearCost);
        Double resultCost = dao.getPeriodicalCost(PERIODICAL_ID);
        assertEquals(yearCost,resultCost);
        verify(preparedStatement).setLong(1,PERIODICAL_ID);

        Account expectedAccount = new Account();
        expectedAccount.setId(ACCOUNT_ID);
        expectedAccount.setAmountOfMoney(50d);
        when(connection.prepareStatement(GET_AMOUNT_FROM_ACCOUNT)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ACCOUNT_ID);
        when(resultSet.getDouble("amount")).thenReturn(expectedAccount.getAmountOfMoney());
        Double resultAmount = dao.getAmountFromAccount(READER_ID);
        assertEquals(expectedAccount.getAmountOfMoney(),resultAmount);
        verify(preparedStatement1).setLong(1,READER_ID);

        dto.setDurationOfSubscription(365);
        dao.checkAccountAmount(dto);
    }

    @Test
    public void getPeriodicalsCostTest()throws SQLException{
        Periodical expectedPeriodical = new Periodical();
        expectedPeriodical.setCost(50d);

        when(connection.prepareStatement(GET_PERIODICAL_COST)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble("cost")).thenReturn(expectedPeriodical.getCost());

        Double resultCost = dao.getPeriodicalCost(PERIODICAL_ID);
        assertEquals(expectedPeriodical.getCost(),resultCost);

        verify(preparedStatement).setLong(1,PERIODICAL_ID);
    }

    @Test
    public void getAmountFromAccountTest()throws SQLException{
        Account expectedAccount = new Account();
        expectedAccount.setId(ACCOUNT_ID);
        expectedAccount.setAmountOfMoney(50d);

        when(connection.prepareStatement(GET_AMOUNT_FROM_ACCOUNT)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ACCOUNT_ID);
        when(resultSet.getDouble("amount")).thenReturn(expectedAccount.getAmountOfMoney());

        Double resultAmount = dao.getAmountFromAccount(READER_ID);
        assertEquals(expectedAccount.getAmountOfMoney(),resultAmount);

        verify(preparedStatement).setLong(1,READER_ID);
    }

    @Test
    public void deleteSubscriptionWhenTransactionCommitTest()throws SQLException{
        when(connection.prepareStatement(DELETE_FROM_PERIODICALS)).thenReturn(preparedStatement);
        when(connection.prepareStatement(DELETE_FROM_PREPAYMENTS)).thenReturn(preparedStatement1);

        boolean result = dao.deleteSubscription(READER_ID,PERIODICAL_ID);
        assertTrue(result);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setLong(1,READER_ID);
        verify(preparedStatement).setLong(2,PERIODICAL_ID);
        verify(preparedStatement).execute();
        verify(preparedStatement1).setLong(1,READER_ID);
        verify(preparedStatement1).setLong(2,PERIODICAL_ID);
        verify(preparedStatement1).execute();
        verify(connection).commit();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test(expected = ReaderException.class)
    public void deleteSubscriptionWhenTransactionRollbackTest()throws SQLException{
        when(connection.prepareStatement(DELETE_FROM_PERIODICALS)).thenReturn(preparedStatement);
        when(preparedStatement.execute()).thenThrow(SQLException.class);
        dao.deleteSubscription(READER_ID,PERIODICAL_ID);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setLong(1,READER_ID);
        verify(preparedStatement).setLong(2,PERIODICAL_ID);
        verify(preparedStatement).execute();
        verify(preparedStatement).close();
        verify(connection).close();
        verify(connection).rollback();
    }
}
