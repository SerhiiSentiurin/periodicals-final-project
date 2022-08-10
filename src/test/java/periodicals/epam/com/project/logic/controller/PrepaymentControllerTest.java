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
import periodicals.epam.com.project.logic.entity.dto.PrepaymentDTO;
import periodicals.epam.com.project.logic.services.PrepaymentService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrepaymentControllerTest {
    @Mock
    private PrepaymentService prepaymentService;
    @Mock
    private QueryParameterHandler queryParameterHandler;
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PrepaymentController prepaymentController;

    private static final Long READER_ID= 1L;
    private static final Long PERIODICAL_ID = 1L;

    @Test
    public void addSubscriptionTest(){
        PrepaymentDTO dto = Mockito.mock(PrepaymentDTO.class);
        when(queryParameterHandler.handleRequest(request,PrepaymentDTO.class)).thenReturn(dto);
        when(prepaymentService.addSubscription(dto)).thenReturn(dto);

        ModelAndView modelAndView = prepaymentController.addSubscription(request);
        assertNotNull(modelAndView);
        assertEquals("/periodicals/periodical/periodicalsForSubscribing?readerId=" + dto.getReaderId(),modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void deleteSubscriptionTest(){
        when(request.getParameter("readerId")).thenReturn(String.valueOf(READER_ID));
        when(request.getParameter("periodicalId")).thenReturn(String.valueOf(PERIODICAL_ID));
        when(prepaymentService.deleteSubscription(READER_ID,PERIODICAL_ID)).thenReturn(true);

        ModelAndView modelAndView = prepaymentController.deleteSubscription(request);
        assertNotNull(modelAndView);
        assertEquals("/periodicals/periodical/readerSubscriptions?readerId="+READER_ID,modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }
}
