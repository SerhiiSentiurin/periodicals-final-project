package periodicals.epam.com.project.logic.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import periodicals.epam.com.project.infrastructure.web.ModelAndView;
import periodicals.epam.com.project.infrastructure.web.QueryParameterHandler;
import periodicals.epam.com.project.logic.entity.dto.PrepaymentDTO;
import periodicals.epam.com.project.logic.services.PrepaymentService;


@AllArgsConstructor
public class PrepaymentController {
    private final PrepaymentService prepaymentService;
    private final QueryParameterHandler queryParameterHandler;

    public ModelAndView addSubscription(HttpServletRequest request) {
        PrepaymentDTO dto = queryParameterHandler.handleRequest(request, PrepaymentDTO.class);
        prepaymentService.addSubscription(dto);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/periodical/periodicalsForSubscribing?readerId=" + dto.getReaderId());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView deleteSubscription(HttpServletRequest request) {
        long readerId = Long.parseLong(request.getParameter("readerId"));
        long periodicalId = Long.parseLong(request.getParameter("periodicalId"));
        prepaymentService.deleteSubscription(readerId, periodicalId);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/periodical/readerSubscriptions?readerId=" + readerId);
        modelAndView.setRedirect(true);
        return modelAndView;
    }
}
