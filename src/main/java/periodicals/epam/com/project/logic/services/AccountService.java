package periodicals.epam.com.project.logic.services;

import lombok.RequiredArgsConstructor;
import periodicals.epam.com.project.logic.dao.AccountDAO;
import periodicals.epam.com.project.logic.entity.dto.AccountDTO;

@RequiredArgsConstructor
public class AccountService {
    private final AccountDAO accountDAO;

    public AccountDTO topUpAccountAmount(AccountDTO accountDTO) {
        return accountDAO.topUpAccountAmount(accountDTO);
    }

    public Double getAmountOfMoneyByReaderId(AccountDTO accountDTO) {
        return accountDAO.getAmountOfMoneyByReaderId(accountDTO);
    }
}
