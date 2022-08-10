package periodicals.epam.com.project.logic.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import periodicals.epam.com.project.logic.dao.AccountDAO;
import periodicals.epam.com.project.logic.entity.dto.AccountDTO;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountDAO dao;

    @InjectMocks
    private AccountService accountService;

    private static final Long READER_ID = 2L;
    private static final Double AMOUNT_OF_MONEY = 50d;
    private static final Long PERIODICAL_ID = 5L;

    @Test
    public void topUpAccountTest(){
        AccountDTO dto = new AccountDTO(AMOUNT_OF_MONEY,READER_ID,PERIODICAL_ID);
        when(dao.topUpAccountAmount(dto)).thenReturn(dto);
        AccountDTO resDto = accountService.topUpAccountAmount(dto);
        Assert.assertEquals(resDto,dto);
    }

    @Test
    public void getAmountOfMoneyByReaderIdTest(){
        AccountDTO dto = new AccountDTO(AMOUNT_OF_MONEY,READER_ID,PERIODICAL_ID);
        when(dao.getAmountOfMoneyByReaderId(dto)).thenReturn(AMOUNT_OF_MONEY);
        Double amount = accountService.getAmountOfMoneyByReaderId(dto);
        Assert.assertEquals(AMOUNT_OF_MONEY,amount);
    }
}
