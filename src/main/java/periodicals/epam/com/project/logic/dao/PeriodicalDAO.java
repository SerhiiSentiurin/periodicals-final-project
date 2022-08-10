package periodicals.epam.com.project.logic.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Prepayment;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;


@RequiredArgsConstructor
public class PeriodicalDAO {

    private final DataSource dataSource;

    @SneakyThrows
    public double getCountOfRowsPeriodical() {
        String query = "SELECT COUNT(*) FROM periodical";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        }
        return 0;
    }

    @SneakyThrows
    public double getCountOfRowsTopic(String topic) {
        String query = "SELECT COUNT(*) FROM periodical WHERE topic = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, topic);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
            }
            return 0;
        }
    }

    @SneakyThrows
    public double getCountOfRowsName(String name) {
        String query = "SELECT COUNT(*) FROM periodical WHERE name LIKE ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
            }
            return 0;
        }
    }

    @SneakyThrows
    public List<Periodical> getAllPeriodicals(int index) {
        String getAllPeriodicals = "SELECT * FROM periodical ORDER BY id LIMIT ?, 5";
        List<Periodical> listOfPeriodicals = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(getAllPeriodicals);
            preparedStatement.setInt(1, index);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
            }
            return listOfPeriodicals;
        }
    }

    @SneakyThrows
    public List<Periodical> getPeriodicalsByTopic(String topic, int index) {
        String selectPeriodicalsByTopic = "SELECT * FROM periodical WHERE topic = ? LIMIT ?, 5";
        List<Periodical> periodicals = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectPeriodicalsByTopic)) {
            preparedStatement.setString(1, topic);
            preparedStatement.setInt(2, index);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    double cost = resultSet.getDouble("cost");
                    String description = resultSet.getString("description");
                    boolean isDeleted = resultSet.getBoolean("isDeleted");
                    Periodical periodical = new Periodical(id, name, topic, cost, description, isDeleted);
                    periodicals.add(periodical);
                }
            }
        }
        return periodicals;
    }

    @SneakyThrows
    public List<Periodical> getPeriodicalByName(String name, int index) {
        String selectPeriodicalsByName = "SELECT * FROM periodical WHERE name LIKE ? LIMIT ?, 5";
        List<Periodical> periodicals = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectPeriodicalsByName)) {
            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.setInt(2, index);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String periodicalName = resultSet.getString("name");
                    String topic = resultSet.getString("topic");
                    double cost = resultSet.getDouble("cost");
                    String description = resultSet.getString("description");
                    boolean isDeleted = resultSet.getBoolean("isDeleted");
                    Periodical periodical = new Periodical(id, periodicalName, topic, cost, description, isDeleted);
                    periodicals.add(periodical);
                }
            }
        }
        return periodicals;
    }

    @SneakyThrows
    public List<Periodical> getPeriodicalsByReaderId(Long readerId) {
        String selectPeriodicalsByReaderId = "SELECT * FROM periodical JOIN periodicals ON periodical.id = periodicals.periodical_id WHERE reader_id =? ORDER BY periodical.id";
        List<Periodical> periodicals = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectPeriodicalsByReaderId)) {
            preparedStatement.setLong(1, readerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long periodicalId = resultSet.getLong("periodical.id");
                    String name = resultSet.getString("name");
                    String topic = resultSet.getString("topic");
                    double cost = resultSet.getDouble("cost");
                    String description = resultSet.getString("description");
                    boolean isDeleted = resultSet.getBoolean("isDeleted");
                    Periodical periodical = new Periodical(periodicalId, name, topic, cost, description, isDeleted);
                    periodicals.add(periodical);
                }
            }
        }
        return periodicals;
    }

    @SneakyThrows
    public Map<Periodical, Prepayment> getPeriodicalsByTopicByReaderId(String topic, Long readerId) {
        String getPeriodicalByTopicByReaderId = "SELECT * FROM periodical JOIN prepayment ON periodical.id = prepayment.periodical_id WHERE reader_id =? AND topic =? ORDER BY periodical.id";
        Map<Periodical, Prepayment> info = new HashMap<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getPeriodicalByTopicByReaderId)) {
            preparedStatement.setLong(1, readerId);
            preparedStatement.setString(2, topic);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long periodicalId = resultSet.getLong("periodical.id");
                    String name = resultSet.getString("name");
                    double cost = resultSet.getDouble("cost");
                    String description = resultSet.getString("description");
                    long prepaymentId = resultSet.getLong("prepayment.id");
                    String startDate = resultSet.getString("start_date");
                    String dueDate = resultSet.getString("due_date");
                    boolean isDeleted = resultSet.getBoolean("isDeleted");
                    Periodical periodical = new Periodical(periodicalId, name, topic, cost, description, isDeleted);
                    Prepayment prepayment = new Prepayment(prepaymentId, startDate, dueDate, periodicalId, readerId);
                    info.put(periodical, prepayment);
                }
            }
        }
        return info;
    }

    @SneakyThrows
    public List<Prepayment> getPrepaymentsByReaderId(Long readerId) {
        String selectPeriodicalsByReaderId = "SELECT prepayment.id, start_date, due_date, periodical_id FROM periodical JOIN prepayment ON periodical.id = prepayment.periodical_id WHERE reader_id =? ORDER BY periodical_id";
        List<Prepayment> prepayments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectPeriodicalsByReaderId)) {
            preparedStatement.setLong(1, readerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long prepaymentId = resultSet.getLong("id");
                    String startDate = resultSet.getString("start_date");
                    String dueDate = resultSet.getString("due_date");
                    long periodicalId = resultSet.getLong("periodical_id");
                    Prepayment prepayment = new Prepayment(prepaymentId, startDate, dueDate, periodicalId, readerId);
                    prepayments.add(prepayment);
                }
            }
        }
        return prepayments;
    }

    @SneakyThrows
    public Map<Periodical, Prepayment> findPeriodicalsByNameByReaderId(String name, Long readerId) {
        String findPeriodicalsByNameByReaderId = "SELECT * FROM periodical JOIN prepayment ON periodical.id = prepayment.periodical_id WHERE reader_id =? AND name LIKE ? ORDER BY periodical.id";
        Map<Periodical, Prepayment> info = new HashMap<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findPeriodicalsByNameByReaderId)) {
            preparedStatement.setLong(1, readerId);
            preparedStatement.setString(2, "%" + name + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long periodicalId = resultSet.getLong("periodical.id");
                    String periodicalName = resultSet.getString("name");
                    String topic = resultSet.getString("topic");
                    double cost = resultSet.getDouble("cost");
                    String description = resultSet.getString("description");
                    long prepaymentId = resultSet.getLong("prepayment.id");
                    String startDate = resultSet.getString("start_date");
                    String dueDate = resultSet.getString("due_date");
                    boolean isDeleted = resultSet.getBoolean("isDeleted");
                    Periodical periodical = new Periodical(periodicalId, periodicalName, topic, cost, description, isDeleted);
                    Prepayment prepayment = new Prepayment(prepaymentId, startDate, dueDate, periodicalId, readerId);
                    info.put(periodical, prepayment);
                }
            }
        }
        return info;
    }

    @SneakyThrows
    public List<Periodical> getPeriodicalsForSubscribing(List<Long> listPeriodicalId) {
        List<Periodical> periodicalsForSubscribe = new ArrayList<>();
        String sqlToGetPeriodicalsForSubscribing = buildSqlToGetPeriodicalsForSubscribing(listPeriodicalId);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sqlToGetPeriodicalsForSubscribing)) {
                while (resultSet.next()) {
                    long perIdForSubs = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String topic = resultSet.getString("topic");
                    double cost = resultSet.getDouble("cost");
                    String description = resultSet.getString("description");
                    boolean isDeleted = resultSet.getBoolean("isDeleted");
                    Periodical periodical = new Periodical(perIdForSubs, name, topic, cost, description, isDeleted);
                    periodicalsForSubscribe.add(periodical);
                }
            }
        }
        return periodicalsForSubscribe;
    }

    private String buildSqlToGetPeriodicalsForSubscribing(List<Long> listPeriodicalId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT distinct id, name, topic, cost, description, isDeleted FROM periodical");
        if (!listPeriodicalId.isEmpty()) {
            sqlBuilder.append(" left join periodicals ON periodicals.periodical_id = periodical.id WHERE (reader_id IS NULL OR ");
            Iterator<Long> iterator = listPeriodicalId.iterator();
            while (iterator.hasNext()) {
                sqlBuilder.append("NOT periodical_id = ").append(iterator.next());
                if (iterator.hasNext()) {
                    sqlBuilder.append(" and ");
                } else {
                    sqlBuilder.append(") and isDeleted = false");
                }
            }
        } else {
            sqlBuilder.append(" left join periodicals ON periodicals.periodical_id = periodical.id WHERE isDeleted = false");
        }
        return sqlBuilder.toString();
    }

    @SneakyThrows
    public List<Periodical> findPeriodicalsForSubscribingByName(List<Long> listPeriodicalId, String name) {
        List<Periodical> periodicalsForSubscribe = new ArrayList<>();
        String sqlToGetPeriodicalsForSubscribing = buildSqlToFindPeriodicalsForSubscribingByName(listPeriodicalId);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlToGetPeriodicalsForSubscribing)) {
            preparedStatement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long perIdForSubs = resultSet.getLong("id");
                    String periodicalName = resultSet.getString("name");
                    String topic = resultSet.getString("topic");
                    double cost = resultSet.getDouble("cost");
                    String description = resultSet.getString("description");
                    boolean isDeleted = resultSet.getBoolean("isDeleted");
                    Periodical periodical = new Periodical(perIdForSubs, periodicalName, topic, cost, description, isDeleted);
                    periodicalsForSubscribe.add(periodical);
                }
            }
        }
        return periodicalsForSubscribe;
    }

    private String buildSqlToFindPeriodicalsForSubscribingByName(List<Long> listPeriodicalId) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT distinct id, name, topic, cost, description, isDeleted FROM periodical");
        if (!listPeriodicalId.isEmpty()) {
            sqlBuilder.append(" left join periodicals ON periodicals.periodical_id = periodical.id WHERE (reader_id IS NULL OR ");
            Iterator<Long> iterator = listPeriodicalId.iterator();
            while (iterator.hasNext()) {
                sqlBuilder.append("NOT periodical_id = ").append(iterator.next());
                if (iterator.hasNext()) {
                    sqlBuilder.append(" AND ");
                } else {
                    sqlBuilder.append(") AND isDeleted = false AND name LIKE ?");
                }
            }
        } else {
            sqlBuilder.append(" LEFT JOIN periodicals ON periodicals.periodical_id = periodical.id WHERE name LIKE ? AND isDeleted = false");
        }
        return sqlBuilder.toString();
    }

    @SneakyThrows
    public List<Long> getPeriodicalIdByReaderId(Long readerId) {
        String getPeriodicalIdByReaderId = "SELECT periodical_id FROM periodicals WHERE reader_id = ? order by periodical_id";
        List<Long> listOfPeriodicalId = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getPeriodicalIdByReaderId)) {
            preparedStatement.setLong(1, readerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long periodicalId = resultSet.getLong("periodical_id");
                    listOfPeriodicalId.add(periodicalId);
                }
            }
        }
        return listOfPeriodicalId;
    }
}



