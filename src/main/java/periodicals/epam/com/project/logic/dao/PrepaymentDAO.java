package periodicals.epam.com.project.logic.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import periodicals.epam.com.project.logic.entity.Account;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.dto.PrepaymentDTO;
import periodicals.epam.com.project.logic.logicExeption.ReaderException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Log4j2
@RequiredArgsConstructor
public class PrepaymentDAO {
    private final DataSource dataSource;
    private static final Integer MONTHS_IN_YEAR = 12;
    private static final Integer DAYS_IN_MONTH = 30;
    private static final Double DISCOUNT = 0.9d;

    public PrepaymentDTO addSubscription(PrepaymentDTO dto) {
        String addSubscription = "INSERT INTO periodicals (reader_id, periodical_id) VALUES (?,?)";
        String updateAccount = "UPDATE account INNER JOIN reader ON account.id = reader.account_id SET account.amount = ? WHERE reader.id = " + dto.getReaderId();
        String updatePrepayment = "INSERT INTO prepayment (start_date, due_date, periodical_id, reader_id) VALUES (curdate(), adddate(start_date, INTERVAL " + dto.getDurationOfSubscription() + " DAY), " + dto.getPeriodicalId() + ", " + dto.getReaderId() + ");";
        checkAccountAmount(dto);
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(addSubscription);
            preparedStatement.setLong(1, dto.getReaderId());
            preparedStatement.setLong(2, dto.getPeriodicalId());
            preparedStatement.execute();
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(updateAccount)) {
                preparedStatement1.setDouble(1, dto.getAmountOfMoney());
                preparedStatement1.execute();
            }
            try (Statement statement = connection.createStatement()) {
                statement.execute(updatePrepayment);
            }
            connection.commit();
            return dto;
        } catch (Exception e) {
            rollback(connection);
            log.error(e.getMessage());
            throw new ReaderException("Transaction failed with add subscription");
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    protected void checkAccountAmount(PrepaymentDTO dto) {
        Double costPeerMonth = getPeriodicalCost(dto.getPeriodicalId());
        Double costPeerYear = (getPeriodicalCost(dto.getPeriodicalId()) * MONTHS_IN_YEAR) * DISCOUNT; // with 10% discount
        Double accountBalance = getAmountFromAccount(dto.getReaderId());

        if ((costPeerMonth > accountBalance) && (dto.getDurationOfSubscription() <= DAYS_IN_MONTH)) {
            log.error("reader have not enough money on the account");
            throw new ReaderException("Not enough money on the account");
        } else if ((costPeerYear > accountBalance) && (dto.getDurationOfSubscription() > DAYS_IN_MONTH)) {
            log.error("reader have not enough money on the account");
            throw new ReaderException("Not enough money on the account, try to get month subscription or top up your account!");
        } else if (dto.getDurationOfSubscription() > 30) {
            dto.setAmountOfMoney(accountBalance - costPeerYear);
        } else {
            dto.setAmountOfMoney(accountBalance - costPeerMonth);
        }
    }

    @SneakyThrows
    public boolean deleteSubscription(Long readerId, Long periodicalId) {
        String deleteFromPeriodicals = "DELETE FROM periodicals WHERE reader_id = ? AND periodical_id = ?";
        String deleteFromPrepayments = "DELETE FROM prepayment WHERE reader_id = ? AND periodical_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(deleteFromPeriodicals);
            preparedStatement.setLong(1, readerId);
            preparedStatement.setLong(2, periodicalId);
            preparedStatement.execute();
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(deleteFromPrepayments)) {
                preparedStatement1.setLong(1, readerId);
                preparedStatement1.setLong(2, periodicalId);
                preparedStatement1.execute();
            }
            connection.commit();
            return true;
        } catch (Exception e) {
            rollback(connection);
            log.error(e.getMessage());
            throw new ReaderException("Transaction failed with delete subscription");
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @SneakyThrows
    protected Double getPeriodicalCost(Long periodicalId) {
        String getPeriodicalCost = "SELECT cost FROM periodical WHERE id = ?";
        Periodical periodical = new Periodical();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getPeriodicalCost)) {
            preparedStatement.setLong(1, periodicalId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double cost = resultSet.getDouble("cost");
                periodical.setCost(cost);
            }
        }
        return periodical.getCost();
    }

    @SneakyThrows
    protected Double getAmountFromAccount(Long readerId) {
        String selectAccountAmount = "SELECT account.id, amount FROM account JOIN reader ON reader.account_id = account.id WHERE reader.id = ?";
        Account readerAccount = new Account();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectAccountAmount)) {
            preparedStatement.setLong(1, readerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long accId = resultSet.getLong("id");
                double amount = resultSet.getDouble("amount");
                readerAccount.setId(accId);
                readerAccount.setAmountOfMoney(amount);
            }
        }
        return readerAccount.getAmountOfMoney();
    }

    @SneakyThrows
    private void rollback(Connection connection) {
        if (connection != null)
            connection.rollback();
    }

    @SneakyThrows
    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }
}
