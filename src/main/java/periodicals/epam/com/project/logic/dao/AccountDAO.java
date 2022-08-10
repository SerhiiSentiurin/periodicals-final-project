package periodicals.epam.com.project.logic.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import periodicals.epam.com.project.logic.entity.Account;
import periodicals.epam.com.project.logic.entity.dto.AccountDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Log4j2
@RequiredArgsConstructor
public class AccountDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public AccountDTO topUpAccountAmount(AccountDTO dto) {
        String sql = "UPDATE account INNER JOIN reader ON account.id = reader.account_id SET account.amount = ? WHERE reader.id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, dto.getAmountOfMoney() + getAmountOfMoneyByReaderId(dto));
            preparedStatement.setLong(2, dto.getReaderId());
            preparedStatement.execute();
        }
        return dto;
    }

    @SneakyThrows
    public Double getAmountOfMoneyByReaderId(AccountDTO dto) {
        String selectAccountAmount = "SELECT account.id, amount FROM account JOIN reader ON reader.account_id = account.id WHERE reader.id = ?";
        Account readerAccount = new Account();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectAccountAmount)) {
            preparedStatement.setLong(1, dto.getReaderId());
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
}
