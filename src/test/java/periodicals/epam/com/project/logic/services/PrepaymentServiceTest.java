package periodicals.epam.com.project.logic.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.dao.PrepaymentDAO;
import periodicals.epam.com.project.logic.entity.dto.PrepaymentDTO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrepaymentServiceTest {
    @Mock
    private PrepaymentDAO dao;

    @InjectMocks
    private PrepaymentService prepaymentService;

    private static final Long READER_ID = 1L;
    private static final Long PERIODICAL_ID = 1L;

    @Test
    public void addSubscriptionTest(){
        PrepaymentDTO dto = Mockito.mock(PrepaymentDTO.class);
        when(dao.addSubscription(dto)).thenReturn(dto);

        PrepaymentDTO resultDto = prepaymentService.addSubscription(dto);
        assertEquals(dto,resultDto);
    }

    @Test
    public void deleteSubscriptionTest(){
        when(dao.deleteSubscription(READER_ID,PERIODICAL_ID)).thenReturn(true);
        boolean result = prepaymentService.deleteSubscription(READER_ID,PERIODICAL_ID);
        assertTrue(result);
    }
}
