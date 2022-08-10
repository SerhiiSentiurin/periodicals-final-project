package periodicals.epam.com.project.logic.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.infrastructure.web.ModelAndView;
import periodicals.epam.com.project.infrastructure.web.QueryParameterHandler;
import periodicals.epam.com.project.logic.entity.dto.AccountDTO;
import periodicals.epam.com.project.logic.services.AccountService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {
    @Mock
    private AccountService accountService;
    @Mock
    private QueryParameterHandler queryParameterHandler;
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AccountController accountController;

    private static final Long READER_ID = 2L;
    private static final Double AMOUNT_OF_MONEY = 50d;
    private static final Long PERIODICAL_ID = 5L;

    @Test
    public void topUpAccountAmountTest() {
        AccountDTO dto = new AccountDTO(AMOUNT_OF_MONEY, READER_ID, PERIODICAL_ID);
        when(queryParameterHandler.handleRequest(request, AccountDTO.class)).thenReturn(dto);
        when(accountService.topUpAccountAmount(dto)).thenReturn(dto);

        ModelAndView modelAndView = accountController.topUpAccountAmount(request);
        assertNotNull(modelAndView);
        assertEquals("/periodicals/account/getAccountInfo?readerId=" + READER_ID, modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void getAmountOfMoneyByReaderIdTest() {
        AccountDTO dto = new AccountDTO(AMOUNT_OF_MONEY, READER_ID, PERIODICAL_ID);
        when(queryParameterHandler.handleRequest(request, AccountDTO.class)).thenReturn(dto);
        when(accountService.getAmountOfMoneyByReaderId(dto)).thenReturn(AMOUNT_OF_MONEY);

        ModelAndView modelAndView = accountController.getAmountOfMoneyByReaderId(request);
        assertNotNull(modelAndView);
        assertEquals("/account/manageAccount.jsp", modelAndView.getView());
        assertFalse(modelAndView.isRedirect());
        assertEquals(AMOUNT_OF_MONEY, modelAndView.getAttributes().get("amountOfMoney"));
    }
}
