package periodicals.epam.com.project.logic.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.infrastructure.web.ModelAndView;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Prepayment;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.services.PeriodicalService;
import periodicals.epam.com.project.logic.services.ReaderService;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeriodicalControllerTest {
    @Mock
    private PeriodicalService periodicalService;
    @Mock
    private ReaderService readerService;
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PeriodicalController periodicalController;

    private static final Long READER_ID = 1L;
    private static final Long PERIODICAL_ID = 1L;
    private static final String TOPIC = "topic";
    private static final String NAME = "name";
    private static final int COUNT_PAGES = 1;
    private static final int PAGE = 1;
    private final Periodical periodical1 = new Periodical(PERIODICAL_ID, "name3", "topic3", 40d, "description3", false);
    private final Periodical periodical2 = new Periodical(2L, "name1", "topic1", 20d, "description1", false);
    private final Periodical periodical3 = new Periodical(3L, "name2", "topic2", 30d, "description2", false);
    private final Prepayment prepayment1 = new Prepayment(1L, "startDate1", "dueDate1", PERIODICAL_ID, READER_ID);
    private final Prepayment prepayment2 = new Prepayment(2L, "startDate2", "dueDate2", 2L, 2L);

    @Test
    public void getAllPeriodicalsTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.getAllPeriodicals(PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefPeriodical()).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.getAllPeriodicals(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getPeriodicalsByTopicTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);

        when(request.getParameter("topic")).thenReturn(TOPIC);
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.getPeriodicalsByTopic(TOPIC, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefTopic(TOPIC)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.getPeriodicalsByTopic(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertEquals(TOPIC, modelAndView.getAttributes().get("topic"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getPeriodicalByNameTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);

        when(request.getParameter("name")).thenReturn(NAME);
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.getPeriodicalByName(NAME, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefName(NAME)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.getPeriodicalByName(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertEquals(NAME, modelAndView.getAttributes().get("name"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void sortPeriodicalsByCostWhenEmptyTopicAndNameTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getCost)).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn("");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.sortPeriodicalsByCost(PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefPeriodical()).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.sortPeriodicalsByCost(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void sortPeriodicalsByCostWhenEmptyNameTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getCost)).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn(TOPIC);
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.sortPeriodicalsByCostByTopic(TOPIC, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefTopic(TOPIC)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.sortPeriodicalsByCost(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertEquals(TOPIC, modelAndView.getAttributes().get("topic"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void sortPeriodicalsByCostWhenEmptyTopicTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getCost)).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn("");
        when(request.getParameter("name")).thenReturn(NAME);
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.sortPeriodicalsByCostByName(NAME, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefName(NAME)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.sortPeriodicalsByCost(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertEquals(NAME, modelAndView.getAttributes().get("name"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void reversedSortPeriodicalsByCostWhenEmptyTopicAndNameTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getCost).reversed()).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn("");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.reversedSortPeriodicalsByCost(PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefPeriodical()).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.reversedSortPeriodicalsByCost(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void reversedSortPeriodicalsByCostWhenEmptyNameTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getCost).reversed()).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn(TOPIC);
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.reversedSortPeriodicalsByCostByTopic(TOPIC, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefTopic(TOPIC)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.reversedSortPeriodicalsByCost(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(TOPIC, modelAndView.getAttributes().get("topic"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void reversedSortPeriodicalsByCostWhenEmptyTopicTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getCost).reversed()).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn("");
        when(request.getParameter("name")).thenReturn(NAME);
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.reversedSortPeriodicalsByCostByName(NAME, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefName(NAME)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.reversedSortPeriodicalsByCost(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(NAME, modelAndView.getAttributes().get("name"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void sortPeriodicalsByNameWhenEmptyTopicAndNameTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getName)).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn("");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.sortPeriodicalsByName(PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefPeriodical()).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.sortPeriodicalsByName(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void sortPeriodicalsByNameWhenEmptyTopicTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getName)).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn("");
        when(request.getParameter("name")).thenReturn(NAME);
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.sortPeriodicalsByNameByName(NAME, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefName(NAME)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.sortPeriodicalsByName(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(NAME, modelAndView.getAttributes().get("name"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void sortPeriodicalsByNameWhenEmptyNameTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getName)).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn(TOPIC);
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.sortPeriodicalsByNameByTopic(TOPIC, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefTopic(TOPIC)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.sortPeriodicalsByName(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(TOPIC, modelAndView.getAttributes().get("topic"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void reversedSortPeriodicalsByNameWhenEmptyTopicAndNameTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getName).reversed()).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn("");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.reversedSortPeriodicalsByName(PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefPeriodical()).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.reversedSortPeriodicalsByName(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void reversedSortPeriodicalsByNameWhenEmptyTopicTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getName).reversed()).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn("");
        when(request.getParameter("name")).thenReturn(NAME);
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.reversedSortPeriodicalsByNameByName(NAME, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefName(NAME)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.reversedSortPeriodicalsByName(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(NAME, modelAndView.getAttributes().get("name"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void reversedSortPeriodicalsByNameWhenEmptyNameTest() {
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);
        expectedList.add(periodical3);
        expectedList = expectedList.stream().sorted(Comparator.comparing(Periodical::getName).reversed()).collect(Collectors.toList());

        when(request.getParameter("topic")).thenReturn(TOPIC);
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(periodicalService.reversedSortPeriodicalsByNameByTopic(TOPIC, PAGE)).thenReturn(expectedList);
        when(periodicalService.getCountOfHrefTopic(TOPIC)).thenReturn(COUNT_PAGES);

        ModelAndView modelAndView = periodicalController.reversedSortPeriodicalsByName(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchPeriodical.jsp", modelAndView.getView());
        assertEquals(expectedList, modelAndView.getAttributes().get("periodicals"));
        assertEquals(TOPIC, modelAndView.getAttributes().get("topic"));
        assertEquals(COUNT_PAGES, modelAndView.getAttributes().get("countOfHref"));
        assertEquals(PAGE, modelAndView.getAttributes().get("page"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getPeriodicalsByReaderIdTest() {
        List<Periodical> expectedListPeriodical = new ArrayList<>();
        List<Prepayment> expectedListPrepayment = new ArrayList<>();
        expectedListPeriodical.add(periodical1);
        expectedListPeriodical.add(periodical2);
        expectedListPrepayment.add(prepayment1);
        expectedListPrepayment.add(prepayment2);

        when(request.getParameter("readerId")).thenReturn(String.valueOf(READER_ID));
        when(periodicalService.getPeriodicalsByReaderId(READER_ID)).thenReturn(expectedListPeriodical);
        when(periodicalService.getPrepaymentsByReaderId(READER_ID)).thenReturn(expectedListPrepayment);

        ModelAndView modelAndView = periodicalController.getPeriodicalsByReaderId(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchSubscriptions.jsp", modelAndView.getView());
        assertEquals(expectedListPeriodical, modelAndView.getAttributes().get("periodicals"));
        assertEquals(expectedListPrepayment, modelAndView.getAttributes().get("prepayments"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getPeriodicalsByTopicByReaderIdTest() {
        Map<Periodical, Prepayment> expectedMap = new HashMap<>();
        expectedMap.put(periodical1, prepayment1);
        expectedMap.put(periodical2, prepayment2);
        List<Periodical> expectedPeriodicals = new ArrayList<>(expectedMap.keySet());
        List<Prepayment> expectedPrepayments = new ArrayList<>(expectedMap.values());

        when(request.getParameter("readerId")).thenReturn(String.valueOf(READER_ID));
        when(request.getParameter("topic")).thenReturn(TOPIC);
        when(periodicalService.getPeriodicalsByTopicByReaderId(TOPIC, READER_ID)).thenReturn(expectedMap);

        ModelAndView modelAndView = periodicalController.getPeriodicalsByTopicByReaderId(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchSubscriptions.jsp", modelAndView.getView());
        assertEquals(expectedPeriodicals, modelAndView.getAttributes().get("periodicals"));
        assertEquals(expectedPrepayments, modelAndView.getAttributes().get("prepayments"));
        assertEquals(TOPIC, modelAndView.getAttributes().get("topic"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void findPeriodicalsByNameByReaderIdTest() {
        Map<Periodical, Prepayment> expectedMap = new HashMap<>();
        expectedMap.put(periodical1, prepayment1);
        expectedMap.put(periodical2, prepayment2);
        List<Periodical> expectedPeriodicals = new ArrayList<>(expectedMap.keySet());
        List<Prepayment> expectedPrepayments = new ArrayList<>(expectedMap.values());

        when(request.getParameter("readerId")).thenReturn(String.valueOf(READER_ID));
        when(request.getParameter("name")).thenReturn(NAME);
        when(periodicalService.findPeriodicalsByNameByReaderId(NAME, READER_ID)).thenReturn(expectedMap);

        ModelAndView modelAndView = periodicalController.findPeriodicalsByNameByReaderId(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/watchSubscriptions.jsp", modelAndView.getView());
        assertEquals(expectedPeriodicals, modelAndView.getAttributes().get("periodicals"));
        assertEquals(expectedPrepayments, modelAndView.getAttributes().get("prepayments"));
        assertEquals(NAME, modelAndView.getAttributes().get("name"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getPeriodicalsForSubscribingTest() {
        Reader reader = Mockito.mock(Reader.class);
        List<Periodical> expectedListPeriodical = new ArrayList<>();
        expectedListPeriodical.add(periodical1);
        expectedListPeriodical.add(periodical2);

        when(request.getParameter("readerId")).thenReturn(String.valueOf(READER_ID));
        when(periodicalService.getPeriodicalsForSubscribing(READER_ID)).thenReturn(expectedListPeriodical);
        when(readerService.getReaderById(READER_ID)).thenReturn(reader);

        ModelAndView modelAndView = periodicalController.getPeriodicalsForSubscribing(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/periodicalsForSubscribing.jsp", modelAndView.getView());
        assertEquals(expectedListPeriodical, modelAndView.getAttributes().get("periodicals"));
        assertEquals(reader, modelAndView.getAttributes().get("reader"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getPeriodicalsForSubscribingByTopicByReaderIdTest() {
        Reader reader = Mockito.mock(Reader.class);
        List<Periodical> expectedListPeriodical = new ArrayList<>();
        expectedListPeriodical.add(periodical1);
        expectedListPeriodical.add(periodical2);

        when(request.getParameter("topic")).thenReturn(TOPIC);
        when(request.getParameter("readerId")).thenReturn(String.valueOf(READER_ID));
        when(periodicalService.getPeriodicalsForSubscribingByTopicByReaderId(TOPIC, READER_ID)).thenReturn(expectedListPeriodical);
        when(readerService.getReaderById(READER_ID)).thenReturn(reader);

        ModelAndView modelAndView = periodicalController.getPeriodicalsForSubscribingByTopicByReaderId(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/periodicalsForSubscribing.jsp", modelAndView.getView());
        assertEquals(expectedListPeriodical, modelAndView.getAttributes().get("periodicals"));
        assertEquals(reader, modelAndView.getAttributes().get("reader"));
        assertEquals(TOPIC, modelAndView.getAttributes().get("topic"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void findPeriodicalsForSubscribingByNameByReaderId() {
        Reader reader = Mockito.mock(Reader.class);
        List<Periodical> expectedListPeriodical = new ArrayList<>();
        expectedListPeriodical.add(periodical1);
        expectedListPeriodical.add(periodical2);

        when(request.getParameter("name")).thenReturn(NAME);
        when(request.getParameter("readerId")).thenReturn(String.valueOf(READER_ID));
        when(periodicalService.findPeriodicalsForSubscribingByNameByReaderId(NAME, READER_ID)).thenReturn(expectedListPeriodical);
        when(readerService.getReaderById(READER_ID)).thenReturn(reader);

        ModelAndView modelAndView = periodicalController.findPeriodicalsForSubscribingByNameByReaderId(request);
        assertNotNull(modelAndView);
        assertEquals("/periodical/periodicalsForSubscribing.jsp", modelAndView.getView());
        assertEquals(expectedListPeriodical, modelAndView.getAttributes().get("periodicals"));
        assertEquals(reader, modelAndView.getAttributes().get("reader"));
        assertEquals(NAME, modelAndView.getAttributes().get("name"));
        assertFalse(modelAndView.isRedirect());
    }
}
