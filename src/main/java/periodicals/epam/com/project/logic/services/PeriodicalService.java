package periodicals.epam.com.project.logic.services;

import lombok.RequiredArgsConstructor;
import periodicals.epam.com.project.logic.dao.PeriodicalDAO;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Prepayment;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PeriodicalService {
    private final PeriodicalDAO periodicalDAO;
    private static final int PERIODICALS_PER_PAGE = 5;

    public int getCountOfHrefPeriodical() {
        double countOfRows = periodicalDAO.getCountOfRowsPeriodical();
        return (int) Math.ceil(countOfRows / PERIODICALS_PER_PAGE);
    }

    public int getCountOfHrefTopic(String topic) {
        double countOfRows = periodicalDAO.getCountOfRowsTopic(topic);
        return (int) Math.ceil(countOfRows / PERIODICALS_PER_PAGE);
    }

    public int getCountOfHrefName(String name) {
        double countOfRows = periodicalDAO.getCountOfRowsName(name);
        return (int) Math.ceil(countOfRows / PERIODICALS_PER_PAGE);
    }

    public List<Periodical> getAllPeriodicals(int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getAllPeriodicals(index);
    }

    public List<Periodical> getPeriodicalsByTopic(String topic, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalsByTopic(topic, index);
    }

    public List<Periodical> getPeriodicalByName(String name, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalByName(name, index);
    }

    public List<Periodical> sortPeriodicalsByCost(int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getAllPeriodicals(index).stream().sorted(Comparator.comparing(Periodical::getCost)).collect(Collectors.toList());
    }

    public List<Periodical> reversedSortPeriodicalsByCost(int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getAllPeriodicals(index).stream().sorted(Comparator.comparing(Periodical::getCost).reversed()).collect(Collectors.toList());
    }

    public List<Periodical> sortPeriodicalsByName(int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getAllPeriodicals(index).stream().sorted(Comparator.comparing(Periodical::getName)).collect(Collectors.toList());
    }

    public List<Periodical> reversedSortPeriodicalsByName(int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getAllPeriodicals(index).stream().sorted(Comparator.comparing(Periodical::getName).reversed()).collect(Collectors.toList());
    }

    public List<Periodical> sortPeriodicalsByCostByTopic(String topic, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalsByTopic(topic, index).stream().sorted(Comparator.comparing(Periodical::getCost)).collect(Collectors.toList());
    }

    public List<Periodical> reversedSortPeriodicalsByCostByTopic(String topic, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalsByTopic(topic, index).stream().sorted(Comparator.comparing(Periodical::getCost).reversed()).collect(Collectors.toList());
    }

    public List<Periodical> sortPeriodicalsByNameByTopic(String topic, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalsByTopic(topic, index).stream().sorted(Comparator.comparing(Periodical::getName)).collect(Collectors.toList());
    }

    public List<Periodical> reversedSortPeriodicalsByNameByTopic(String topic, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalsByTopic(topic, index).stream().sorted(Comparator.comparing(Periodical::getName).reversed()).collect(Collectors.toList());
    }

    public List<Periodical> sortPeriodicalsByCostByName(String name, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalByName(name, index).stream().sorted(Comparator.comparing(Periodical::getCost)).collect(Collectors.toList());
    }

    public List<Periodical> reversedSortPeriodicalsByCostByName(String name, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalByName(name, index).stream().sorted(Comparator.comparing(Periodical::getCost).reversed()).collect(Collectors.toList());
    }

    public List<Periodical> sortPeriodicalsByNameByName(String name, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalByName(name, index).stream().sorted(Comparator.comparing(Periodical::getName)).collect(Collectors.toList());
    }

    public List<Periodical> reversedSortPeriodicalsByNameByName(String name, int page) {
        int index = (page - 1) * PERIODICALS_PER_PAGE;
        return periodicalDAO.getPeriodicalByName(name, index).stream().sorted(Comparator.comparing(Periodical::getName).reversed()).collect(Collectors.toList());
    }

    public List<Periodical> getPeriodicalsByReaderId(Long readerId) {
        return periodicalDAO.getPeriodicalsByReaderId(readerId);
    }

    public Map<Periodical, Prepayment> getPeriodicalsByTopicByReaderId(String topic, Long readerId) {
        return periodicalDAO.getPeriodicalsByTopicByReaderId(topic, readerId);
    }

    public Map<Periodical, Prepayment> findPeriodicalsByNameByReaderId(String name, Long readerId) {
        return periodicalDAO.findPeriodicalsByNameByReaderId(name, readerId);
    }

    public List<Prepayment> getPrepaymentsByReaderId(Long readerId) {
        return periodicalDAO.getPrepaymentsByReaderId(readerId);
    }

    public List<Periodical> getPeriodicalsForSubscribing(Long readerId) {
        List<Long> periodicalIdByReaderId = periodicalDAO.getPeriodicalIdByReaderId(readerId);
        return periodicalDAO.getPeriodicalsForSubscribing(periodicalIdByReaderId);
    }

    public List<Periodical> getPeriodicalsForSubscribingByTopicByReaderId(String topic, Long readerId) {
        List<Long> periodicalIdByReaderId = periodicalDAO.getPeriodicalIdByReaderId(readerId);
        return periodicalDAO.getPeriodicalsForSubscribing(periodicalIdByReaderId)
                .stream()
                .filter(periodical -> periodical.getTopic().equals(topic))
                .collect(Collectors.toList());
    }

    public List<Periodical> findPeriodicalsForSubscribingByNameByReaderId(String name, Long readerId) {
        List<Long> periodicalIdByReaderId = periodicalDAO.getPeriodicalIdByReaderId(readerId);
        return periodicalDAO.findPeriodicalsForSubscribingByName(periodicalIdByReaderId, name);
    }

}


