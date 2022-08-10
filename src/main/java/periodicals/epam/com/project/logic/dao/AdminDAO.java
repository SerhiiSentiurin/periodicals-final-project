package periodicals.epam.com.project.logic.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import periodicals.epam.com.project.logic.entity.Account;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.dto.PeriodicalDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@RequiredArgsConstructor
public class AdminDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public List<Periodical> getAllPeriodicals() {
        String getAllPeriodicals = "SELECT * FROM periodical";
        List<Periodical> listOfPeriodicals = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getAllPeriodicals)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String topic = resultSet.getString("topic");
                double cost = resultSet.getDouble("cost");
                String description = resultSet.getString("description");
                boolean isDeleted = resultSet.getBoolean("isDeleted");
                Periodical periodical = new Periodical(id, name, topic, cost, description, isDeleted);
                listOfPeriodicals.add(periodical);
            }
            return listOfPeriodicals;
        }
    }

    @SneakyThrows
    public boolean createNewPeriodical(PeriodicalDTO dto) {
        String addNewPeriodical = "INSERT INTO periodical (name, topic, cost, description) VALUES (?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addNewPeriodical, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, dto.getName());
            preparedStatement.setString(2, dto.getTopic());
            preparedStatement.setDouble(3, dto.getCost());
            preparedStatement.setString(4, dto.getDescription());
            return preparedStatement.execute();
        }
    }

    @SneakyThrows
    public boolean deletePeriodicalByPeriodicalId(Long periodicalId) {
        String deletePeriodical = "DELETE FROM periodical WHERE periodical.id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deletePeriodical)) {
            preparedStatement.setLong(1, periodicalId);
            return preparedStatement.execute();
        }
    }

    @SneakyThrows
    public boolean deletePeriodicalForReaders(Long periodicalId) {
        String delPeriodical = "UPDATE periodical SET isDeleted = true WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delPeriodical)) {
            preparedStatement.setLong(1, periodicalId);
            return preparedStatement.execute();
        }
    }

    @SneakyThrows
    public boolean restorePeriodicalForReaders(Long periodicalId) {
        String restorePeriodical = "UPDATE periodical SET isDeleted = false WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(restorePeriodical)) {
            preparedStatement.setLong(1, periodicalId);
            return preparedStatement.execute();
        }
    }

    @SneakyThrows
    public Periodical getPeriodicalById(Long periodicalId) {
        String getPeriodical = "SELECT * FROM periodical WHERE id =?";
        Periodical periodical = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getPeriodical)) {
            preparedStatement.setLong(1, periodicalId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String topic = resultSet.getString("topic");
                Double cost = resultSet.getDouble("cost");
                String description = resultSet.getString("description");
                boolean isDeleted = resultSet.getBoolean("isDeleted");
                periodical = new Periodical(id, name, topic, cost, description, isDeleted);
            }
        }
        return periodical;
    }

    @SneakyThrows
    public boolean editPeriodicalById(PeriodicalDTO dto) {
        String editPeriodical = "UPDATE periodical SET name =?, topic =?, cost =?, description =? WHERE id =?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(editPeriodical)) {
            preparedStatement.setString(1, dto.getName());
            preparedStatement.setString(2, dto.getTopic());
            preparedStatement.setDouble(3, dto.getCost());
            preparedStatement.setString(4, dto.getDescription());
            preparedStatement.setLong(5, dto.getPeriodicalId());
            return preparedStatement.execute();
        }
    }

    @SneakyThrows
    public List<Reader> getAllReaders() {
        String getReaders = "SELECT user.login, account.amount, reader.lock, reader.account_id, reader.id FROM user JOIN reader ON user.id = reader.id JOIN account ON reader.account_id = account.id";
        List<Reader> listOfReaders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getReaders)) {
            while (resultSet.next()) {
                String login = resultSet.getString("login");
                double amount = resultSet.getDouble("amount");
                boolean lock = resultSet.getBoolean("lock");
                long accountId = resultSet.getLong("account_id");
                long readerId = resultSet.getLong("reader.id");
                Account account = new Account(accountId, amount);
                Reader reader = new Reader();
                reader.setId(readerId);
                reader.setLogin(login);
                reader.setAccount(account);
                reader.setLock(lock);
                listOfReaders.add(reader);
            }
        }
        return listOfReaders;
    }

    @SneakyThrows
    public boolean lockReader(Long readerId) {
        String lockReader = "UPDATE reader SET reader.lock = true WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(lockReader)) {
            preparedStatement.setLong(1, readerId);
            return preparedStatement.execute();
        }
    }

    @SneakyThrows
    public boolean unlockReader(Long readerId) {
        String lockReader = "UPDATE reader SET reader.lock = false WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(lockReader)) {
            preparedStatement.setLong(1, readerId);
            return preparedStatement.execute();
        }
    }
}
