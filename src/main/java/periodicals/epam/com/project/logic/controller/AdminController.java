package periodicals.epam.com.project.logic.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import periodicals.epam.com.project.infrastructure.web.ModelAndView;
import periodicals.epam.com.project.infrastructure.web.QueryParameterHandler;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.dto.PeriodicalDTO;
import periodicals.epam.com.project.logic.services.AdminService;

import java.util.*;

@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final QueryParameterHandler queryParameterHandler;

    public ModelAndView getAllPeriodicals(HttpServletRequest request) {
        List<Periodical> listPeriodicals = adminService.getAllPeriodicals();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAttribute("periodicals", listPeriodicals);
        modelAndView.setView("/admin/managePeriodicals.jsp");
        return modelAndView;
    }

    public ModelAndView createNewPeriodical(HttpServletRequest request) {
        PeriodicalDTO dto = queryParameterHandler.handleRequest(request, PeriodicalDTO.class);
        adminService.createNewPeriodical(dto);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/admin/managePeriodicals?name=" + dto.getName() + "&topic=" + dto.getTopic() + "&cost=" + dto.getCost() + "&description=" + dto.getDescription());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView deletePeriodicalByPeriodicalId(HttpServletRequest request) {
        Long periodicalId = Long.parseLong(request.getParameter("periodicalId"));
        adminService.deletePeriodicalByPeriodicalId(periodicalId);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/admin/managePeriodicals?periodicalId=" + periodicalId);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView deletePeriodicalForReaders(HttpServletRequest request) {
        Long periodicalId = Long.parseLong(request.getParameter("periodicalId"));
        adminService.deletePeriodicalForReaders(periodicalId);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/admin/managePeriodicals?periodicalId=" + periodicalId);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView restorePeriodicalForReaders(HttpServletRequest request) {
        Long periodicalId = Long.parseLong(request.getParameter("periodicalId"));
        adminService.restorePeriodicalForReaders(periodicalId);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/admin/managePeriodicals?periodicalId=" + periodicalId);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView getPeriodicalById(HttpServletRequest request) {
        Long periodicalId = Long.parseLong(request.getParameter("periodicalId"));
        Periodical periodical = adminService.getPeriodicalById(periodicalId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAttribute("periodical", periodical);
        modelAndView.setView("/admin/editPeriodical.jsp");
        return modelAndView;
    }

    public ModelAndView editPeriodicalById(HttpServletRequest request) {
        PeriodicalDTO dto = queryParameterHandler.handleRequest(request, PeriodicalDTO.class);
        adminService.editPeriodicalById(dto);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/admin/getPeriodicalForEdit?periodicalId=" + dto.getPeriodicalId());
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView getAllReaders(HttpServletRequest request) {
        Map<Reader, Periodical> mapOfReaders = adminService.getAllReaders();
        List<Reader> readers = new LinkedList<>(mapOfReaders.keySet());
        List<Periodical> subscriptions = new LinkedList<>(mapOfReaders.values());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAttribute("readers", readers);
        modelAndView.addAttribute("subscriptions", subscriptions);
        modelAndView.setView("/admin/manageReaders.jsp");
        return modelAndView;
    }

    public ModelAndView lockReader(HttpServletRequest request) {
        Long readerId = Long.parseLong(request.getParameter("readerId"));
        adminService.lockReader(readerId);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/admin/manageReaders?readerId=" + readerId);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    public ModelAndView unlockReader(HttpServletRequest request) {
        Long readerId = Long.parseLong(request.getParameter("readerId"));
        adminService.unlockReader(readerId);
        ModelAndView modelAndView = ModelAndView.withView("/periodicals/admin/manageReaders?readerId=" + readerId);
        modelAndView.setRedirect(true);
        return modelAndView;
    }
}
