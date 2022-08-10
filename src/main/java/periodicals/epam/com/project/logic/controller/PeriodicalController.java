package periodicals.epam.com.project.logic.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import periodicals.epam.com.project.infrastructure.web.ModelAndView;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Prepayment;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.services.PeriodicalService;
import periodicals.epam.com.project.logic.services.ReaderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
public class PeriodicalController {
    private final PeriodicalService periodicalService;
    private final ReaderService readerService;


    public ModelAndView getAllPeriodicals(HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/periodical/watchPeriodical.jsp");
        modelAndView.addAttribute("periodicals", periodicalService.getAllPeriodicals(page));
        modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefPeriodical());
        modelAndView.addAttribute("page", page);
        return modelAndView;
    }

    public ModelAndView getPeriodicalsByTopic(HttpServletRequest request) {
        String topic = request.getParameter("topic");
        int page = Integer.parseInt(request.getParameter("page"));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAttribute("periodicals", periodicalService.getPeriodicalsByTopic(topic, page));
        modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefTopic(topic));
        modelAndView.addAttribute("page", page);
        modelAndView.addAttribute("topic", topic);
        modelAndView.setView("/periodical/watchPeriodical.jsp");
        return modelAndView;
    }

    public ModelAndView getPeriodicalByName(HttpServletRequest request) {
        String name = request.getParameter("name");
        int page = Integer.parseInt(request.getParameter("page"));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAttribute("periodicals", periodicalService.getPeriodicalByName(name, page));
        modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefName(name));
        modelAndView.addAttribute("page", page);
        modelAndView.addAttribute("name", name);
        modelAndView.setView("/periodical/watchPeriodical.jsp");
        return modelAndView;
    }

    public ModelAndView sortPeriodicalsByCost(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        int page = Integer.parseInt(request.getParameter("page"));
        String topic = request.getParameter("topic");
        String name = request.getParameter("name");
        List<Periodical> sortedPeriodicals = null;
        if (topic.isEmpty() && name.isEmpty()) {
            sortedPeriodicals = periodicalService.sortPeriodicalsByCost(page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefPeriodical());
            modelAndView.addAttribute("page", page);
        } else if (name.isEmpty()) {
            sortedPeriodicals = periodicalService.sortPeriodicalsByCostByTopic(topic, page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefTopic(topic));
            modelAndView.addAttribute("page", page);
            modelAndView.addAttribute("topic", topic);
        } else {
            sortedPeriodicals = periodicalService.sortPeriodicalsByCostByName(name, page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefName(name));
            modelAndView.addAttribute("page", page);
            modelAndView.addAttribute("name", name);
        }
        modelAndView.addAttribute("periodicals", sortedPeriodicals);
        modelAndView.setView("/periodical/watchPeriodical.jsp");
        return modelAndView;
    }

    public ModelAndView reversedSortPeriodicalsByCost(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        int page = Integer.parseInt(request.getParameter("page"));
        String topic = request.getParameter("topic");
        String name = request.getParameter("name");
        List<Periodical> sortedPeriodicals;
        if (topic.isEmpty() && name.isEmpty()) {
            sortedPeriodicals = periodicalService.reversedSortPeriodicalsByCost(page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefPeriodical());
            modelAndView.addAttribute("page", page);
        } else if (name.isEmpty()) {
            sortedPeriodicals = periodicalService.reversedSortPeriodicalsByCostByTopic(topic, page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefTopic(topic));
            modelAndView.addAttribute("page", page);
            modelAndView.addAttribute("topic", topic);
        } else {
            sortedPeriodicals = periodicalService.reversedSortPeriodicalsByCostByName(name, page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefName(name));
            modelAndView.addAttribute("page", page);
            modelAndView.addAttribute("name", name);
        }
        modelAndView.addAttribute("periodicals", sortedPeriodicals);
        modelAndView.setView("/periodical/watchPeriodical.jsp");
        return modelAndView;
    }

    public ModelAndView sortPeriodicalsByName(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        int page = Integer.parseInt(request.getParameter("page"));
        String topic = request.getParameter("topic");
        String name = request.getParameter("name");
        List<Periodical> sortedPeriodicals;
        if (topic.isEmpty() && name.isEmpty()) {
            sortedPeriodicals = periodicalService.sortPeriodicalsByName(page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefPeriodical());
            modelAndView.addAttribute("page", page);
        } else if (name.isEmpty()) {
            sortedPeriodicals = periodicalService.sortPeriodicalsByNameByTopic(topic, page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefTopic(topic));
            modelAndView.addAttribute("page", page);
            modelAndView.addAttribute("topic", topic);
        } else {
            sortedPeriodicals = periodicalService.sortPeriodicalsByNameByName(name, page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefName(name));
            modelAndView.addAttribute("page", page);
            modelAndView.addAttribute("name", name);
        }
        modelAndView.addAttribute("periodicals", sortedPeriodicals);
        modelAndView.setView("/periodical/watchPeriodical.jsp");
        return modelAndView;
    }

    public ModelAndView reversedSortPeriodicalsByName(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        int page = Integer.parseInt(request.getParameter("page"));
        String topic = request.getParameter("topic");
        String name = request.getParameter("name");
        List<Periodical> sortedPeriodicals;
        if (topic.isEmpty() && name.isEmpty()) {
            sortedPeriodicals = periodicalService.reversedSortPeriodicalsByName(page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefPeriodical());
            modelAndView.addAttribute("page", page);
        } else if (name.isEmpty()) {
            sortedPeriodicals = periodicalService.reversedSortPeriodicalsByNameByTopic(topic, page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefTopic(topic));
            modelAndView.addAttribute("page", page);
            modelAndView.addAttribute("topic", topic);
        } else {
            sortedPeriodicals = periodicalService.reversedSortPeriodicalsByNameByName(name, page);
            modelAndView.addAttribute("countOfHref", periodicalService.getCountOfHrefName(name));
            modelAndView.addAttribute("page", page);
            modelAndView.addAttribute("name", name);
        }
        modelAndView.addAttribute("periodicals", sortedPeriodicals);
        modelAndView.setView("/periodical/watchPeriodical.jsp");
        return modelAndView;
    }

    public ModelAndView getPeriodicalsByReaderId(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        long readerId = Long.parseLong(request.getParameter("readerId"));
        List<Periodical> subscribedPeriodicals = periodicalService.getPeriodicalsByReaderId(readerId);
        List<Prepayment> prepaymentInfo = periodicalService.getPrepaymentsByReaderId(readerId);
        modelAndView.addAttribute("periodicals", subscribedPeriodicals);
        modelAndView.addAttribute("prepayments", prepaymentInfo);
        modelAndView.setView("/periodical/watchSubscriptions.jsp");
        return modelAndView;
    }

    public ModelAndView getPeriodicalsByTopicByReaderId(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        long readerId = Long.parseLong(request.getParameter("readerId"));
        String topic = request.getParameter("topic");
        Map<Periodical, Prepayment> infoMap = periodicalService.getPeriodicalsByTopicByReaderId(topic, readerId);
        List<Periodical> subscribedPeriodicals = new ArrayList<>(infoMap.keySet());
        List<Prepayment> prepaymentInfo = new ArrayList<>(infoMap.values());
        modelAndView.addAttribute("periodicals", subscribedPeriodicals);
        modelAndView.addAttribute("prepayments", prepaymentInfo);
        modelAndView.addAttribute("topic", topic);
        modelAndView.setView("/periodical/watchSubscriptions.jsp");
        return modelAndView;
    }

    public ModelAndView findPeriodicalsByNameByReaderId(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        long readerId = Long.parseLong(request.getParameter("readerId"));
        String name = request.getParameter("name");
        Map<Periodical, Prepayment> infoList = periodicalService.findPeriodicalsByNameByReaderId(name, readerId);
        List<Periodical> subscribedPeriodicals = new ArrayList<>(infoList.keySet());
        List<Prepayment> prepaymentInfo = new ArrayList<>(infoList.values());

        modelAndView.addAttribute("periodicals", subscribedPeriodicals);
        modelAndView.addAttribute("prepayments", prepaymentInfo);
        modelAndView.addAttribute("name", name);
        modelAndView.setView("/periodical/watchSubscriptions.jsp");
        return modelAndView;
    }

    public ModelAndView getPeriodicalsForSubscribing(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        long readerId = Long.parseLong(request.getParameter("readerId"));
        List<Periodical> periodicalsForSubscribing = periodicalService.getPeriodicalsForSubscribing(readerId);
        Reader reader = readerService.getReaderById(readerId);
        modelAndView.addAttribute("reader", reader);
        modelAndView.addAttribute("periodicals", periodicalsForSubscribing);
        modelAndView.setView("/periodical/periodicalsForSubscribing.jsp");
        return modelAndView;
    }

    public ModelAndView getPeriodicalsForSubscribingByTopicByReaderId(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        long readerId = Long.parseLong(request.getParameter("readerId"));
        String topic = request.getParameter("topic");
        List<Periodical> periodicalsForSubscribing = periodicalService.getPeriodicalsForSubscribingByTopicByReaderId(topic, readerId);
        Reader reader = readerService.getReaderById(readerId);
        modelAndView.addAttribute("reader", reader);
        modelAndView.addAttribute("periodicals", periodicalsForSubscribing);
        modelAndView.addAttribute("topic", topic);
        modelAndView.setView("/periodical/periodicalsForSubscribing.jsp");
        return modelAndView;
    }

    public ModelAndView findPeriodicalsForSubscribingByNameByReaderId(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        long readerId = Long.parseLong(request.getParameter("readerId"));
        String name = request.getParameter("name");
        List<Periodical> periodicalsForSubscribing = periodicalService.findPeriodicalsForSubscribingByNameByReaderId(name, readerId);
        Reader reader = readerService.getReaderById(readerId);
        modelAndView.addAttribute("reader", reader);
        modelAndView.addAttribute("periodicals", periodicalsForSubscribing);
        modelAndView.addAttribute("name", name);
        modelAndView.setView("/periodical/periodicalsForSubscribing.jsp");
        return modelAndView;
    }
}
