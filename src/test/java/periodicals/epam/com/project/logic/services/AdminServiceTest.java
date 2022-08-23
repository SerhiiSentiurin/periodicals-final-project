package periodicals.epam.com.project.logic.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.dao.AdminDAO;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.dto.PeriodicalDTO;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {
    @Mock
    private AdminDAO dao;

    @InjectMocks
    private AdminService adminService;

    private static final Long PERIODICAL_ID = 5L;
    private static final Long READER_ID = 1L;

    @Test
    public void getAllPeriodicalsTest() {
        Periodical periodical1 = Mockito.mock(Periodical.class);
        Periodical periodical2 = Mockito.mock(Periodical.class);
        List<Periodical> expectedList = new ArrayList<>();
        expectedList.add(periodical1);
        expectedList.add(periodical2);

        when(dao.getAllPeriodicals()).thenReturn(expectedList);

        List<Periodical> resultList = adminService.getAllPeriodicals();
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getAllPeriodicalsEmptyTest() {
        List<Periodical> expectedList = Collections.emptyList();
        when(dao.getAllPeriodicals()).thenReturn(expectedList);

        List<Periodical> resultList = adminService.getAllPeriodicals();
        assertEquals(expectedList, resultList);
    }

    @Test
    public void createNewPeriodicalTest() {
        PeriodicalDTO dto = Mockito.mock(PeriodicalDTO.class);

        when(dao.createNewPeriodical(dto)).thenReturn(true);

        boolean result = adminService.createNewPeriodical(dto);
        assertTrue(result);
    }

    @Test
    public void deletePeriodicalByPeriodicalId() {
        when(dao.deletePeriodicalByPeriodicalId(PERIODICAL_ID)).thenReturn(true);
        boolean result = adminService.deletePeriodicalByPeriodicalId(PERIODICAL_ID);
        assertTrue(result);
    }

    @Test
    public void deletePeriodicalForReadersTest() {
        when(dao.deletePeriodicalForReaders(PERIODICAL_ID)).thenReturn(true);
        boolean result = adminService.deletePeriodicalForReaders(PERIODICAL_ID);
        assertTrue(result);
    }

    @Test
    public void restorePeriodicalForReadersTest() {
        when(dao.restorePeriodicalForReaders(PERIODICAL_ID)).thenReturn(true);
        boolean result = adminService.restorePeriodicalForReaders(PERIODICAL_ID);
        assertTrue(result);
    }

    @Test
    public void getPeriodicalByIdTest() {
        Periodical expectedPeriodical = Mockito.mock(Periodical.class);
        when(dao.getPeriodicalById(PERIODICAL_ID)).thenReturn(expectedPeriodical);

        Periodical resultPeriodical = adminService.getPeriodicalById(PERIODICAL_ID);
        assertEquals(expectedPeriodical, resultPeriodical);
    }

    @Test
    public void editPeriodicalByIdTest() {
        PeriodicalDTO dto = Mockito.mock(PeriodicalDTO.class);
        when(dao.editPeriodicalById(dto)).thenReturn(true);

        boolean result = adminService.editPeriodicalById(dto);
        assertTrue(result);
    }

    @Test
    public void getAllReadersTest() {
        Reader reader1 = Mockito.mock(Reader.class);
        Reader reader2 = Mockito.mock(Reader.class);
        Periodical periodical1 = Mockito.mock(Periodical.class);
        Periodical periodical2 = Mockito.mock(Periodical.class);
        Map<Reader, Periodical> expectedMap = new HashMap<>();
        expectedMap.put(reader1,periodical1);
        expectedMap.put(reader2,periodical2);

        when(dao.getAllReaders()).thenReturn(expectedMap);

        Map<Reader,Periodical> resultMap = adminService.getAllReaders();
        assertEquals(expectedMap,resultMap);
    }

    @Test
    public void getAllReadersEmptyTest() {
        Map<Reader, Periodical> expectedMap = Collections.emptyMap();
        when(dao.getAllReaders()).thenReturn(expectedMap);
        Map<Reader, Periodical> resultMap = adminService.getAllReaders();
        assertEquals(expectedMap,resultMap);
    }

    @Test
    public void lockReader() {
        when(dao.lockReader(READER_ID)).thenReturn(true);
        boolean result = adminService.lockReader(READER_ID);
        assertTrue(result);
    }

    @Test
    public void unlockReader() {
        when(dao.unlockReader(READER_ID)).thenReturn(true);
        boolean result = adminService.unlockReader(READER_ID);
        assertTrue(result);
    }
}
