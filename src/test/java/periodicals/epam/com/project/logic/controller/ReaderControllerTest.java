package periodicals.epam.com.project.logic.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.infrastructure.web.ModelAndView;
import periodicals.epam.com.project.infrastructure.web.QueryParameterHandler;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.dto.ReaderCreateDTO;
import periodicals.epam.com.project.logic.services.PeriodicalService;
import periodicals.epam.com.project.logic.services.ReaderService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReaderControllerTest {
    @Mock
    private ReaderService readerService;
    @Mock
    private PeriodicalService periodicalService;
    @Mock
    private QueryParameterHandler queryParameterHandler;
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ReaderController readerController;

    private static final Long READER_ID = 1L;

    @Test
    public void getReaderByIdTest(){
        Reader expectedReader = Mockito.mock(Reader.class);
        Periodical periodical1 = Mockito.mock(Periodical.class);
        List <Periodical> expectedPeriodicals = new ArrayList<>();
        expectedPeriodicals.add(periodical1);

        when(request.getParameter("readerId")).thenReturn(String.valueOf(READER_ID));
        when(readerService.getReaderById(READER_ID)).thenReturn(expectedReader);
        when(periodicalService.getPeriodicalsByReaderId(READER_ID)).thenReturn(expectedPeriodicals);

        ModelAndView modelAndView = readerController.getReaderById(request);
        assertNotNull(modelAndView);
        assertEquals("/reader/readerHome.jsp",modelAndView.getView());
        assertEquals(expectedReader,modelAndView.getAttributes().get("reader"));
        assertEquals(expectedPeriodicals,modelAndView.getAttributes().get("periodicals"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void createReaderTest(){
        Reader expectedReader = Mockito.mock(Reader.class);
        ReaderCreateDTO dto = Mockito.mock(ReaderCreateDTO.class);

        when(queryParameterHandler.handleRequest(request,ReaderCreateDTO.class)).thenReturn(dto);
        when(readerService.createReader(dto)).thenReturn(expectedReader);

        ModelAndView modelAndView = readerController.createReader(request);
        assertNotNull(modelAndView);
        assertEquals("/reader/successRegister.jsp",modelAndView.getView());
        assertEquals(expectedReader,modelAndView.getAttributes().get("reader"));
        assertTrue(modelAndView.isRedirect());
    }
}
