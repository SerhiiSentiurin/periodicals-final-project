package periodicals.epam.com.project.logic.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.entity.dto.AccountDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountDAOTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AccountDAO dao;

    private static final String TOP_UP_ACCOUNT_AMOUNT = "UPDATE account INNER JOIN reader ON account.id = reader.account_id " +
            "SET account.amount = ? WHERE reader.id = ?";
    private static final String GET_AMOUNT_OF_MONEY_BY_READER_ID = "SELECT account.id, amount FROM account JOIN reader ON " +
            "reader.account_id = account.id WHERE reader.id = ?";
    private static final Double AMOUNT_OF_MONEY = 50d;
    private static final Long READER_ID = 2L;
    private static final Long PERIODICAL_ID = 5L;
    private static final Long ACCOUNT_ID = 1L;

    @Before
    public void setConnection() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void getAmountOfMoneyByReaderIdTest() throws SQLException {
        AccountDTO dto = new AccountDTO(AMOUNT_OF_MONEY, READER_ID, PERIODICAL_ID);

        when(connection.prepareStatement(GET_AMOUNT_OF_MONEY_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ACCOUNT_ID);
        when(resultSet.getDouble("amount")).thenReturn(AMOUNT_OF_MONEY);

        Double amountOfMoneyByReaderId = dao.getAmountOfMoneyByReaderId(dto);
        assertEquals(AMOUNT_OF_MONEY, amountOfMoneyByReaderId);

        verify(preparedStatement).setLong(anyInt(), anyLong());
    }

    @Test
    public void getAmountOfMoneyByReaderIdWhenResultIsEmptyTest() throws SQLException {
        AccountDTO dto = new AccountDTO(0d,READER_ID, PERIODICAL_ID);

        when(connection.prepareStatement(GET_AMOUNT_OF_MONEY_BY_READER_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Double amountOfMoneyByReaderId = dao.getAmountOfMoneyByReaderId(dto);
        assertNull(amountOfMoneyByReaderId);

        verify(preparedStatement).setLong(1, dto.getReaderId());
    }

    @Test
    public void TopUpAccountAmountTest() throws SQLException {
        AccountDTO dto = new AccountDTO(AMOUNT_OF_MONEY, READER_ID, PERIODICAL_ID);
        when(connection.prepareStatement(TOP_UP_ACCOUNT_AMOUNT)).thenReturn(preparedStatement);
        when(connection.prepareStatement(GET_AMOUNT_OF_MONEY_BY_READER_ID)).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ACCOUNT_ID);
        when(resultSet.getDouble("amount")).thenReturn(AMOUNT_OF_MONEY);
        when(preparedStatement.execute()).thenReturn(true);

        Double amountOfMoneyByReaderId = dao.getAmountOfMoneyByReaderId(dto);
        assertEquals(AMOUNT_OF_MONEY, amountOfMoneyByReaderId);
        AccountDTO resultDto = dao.topUpAccountAmount(dto);
        assertEquals(resultDto, dto);
    }
}