package periodicals.epam.com.project.logic.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import periodicals.epam.com.project.infrastructure.web.ModelAndView;
import periodicals.epam.com.project.infrastructure.web.QueryParameterHandler;
import periodicals.epam.com.project.logic.entity.dto.AccountDTO;
import periodicals.epam.com.project.logic.services.AccountService;

@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final QueryParameterHandler queryParameterHandler;

    public ModelAndView topUpAccountAmount(HttpServletRequest request) {
        AccountDTO dto = queryParameterHandler.handleRequest(request, AccountDTO.class);
        accountService.topUpAccountAmount(dto);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/account/getAccountInfo?readerId=" + dto.getReaderId());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView getAmountOfMoneyByReaderId(HttpServletRequest request) {
        AccountDTO dto = queryParameterHandler.handleRequest(request, AccountDTO.class);
        Double amountOfMoney = accountService.getAmountOfMoneyByReaderId(dto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAttribute("amountOfMoney", amountOfMoney);
        modelAndView.setView("/account/manageAccount.jsp");
        return modelAndView;
    }
}
