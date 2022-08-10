package periodicals.epam.com.project.logic.services;

import lombok.RequiredArgsConstructor;
import periodicals.epam.com.project.logic.dao.AdminDAO;
import periodicals.epam.com.project.logic.entity.Periodical;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.dto.PeriodicalDTO;

import java.util.List;

@RequiredArgsConstructor
public class AdminService {
    private final AdminDAO adminDAO;

    public List<Periodical> getAllPeriodicals() {
        return adminDAO.getAllPeriodicals();
    }

    public boolean createNewPeriodical(PeriodicalDTO dto) {
        return adminDAO.createNewPeriodical(dto);
    }

    public boolean deletePeriodicalByPeriodicalId(Long periodicalId) {
        return adminDAO.deletePeriodicalByPeriodicalId(periodicalId);
    }

    public boolean deletePeriodicalForReaders(Long periodicalId) {
        return adminDAO.deletePeriodicalForReaders(periodicalId);
    }

    public boolean restorePeriodicalForReaders(Long periodicalId) {
        return adminDAO.restorePeriodicalForReaders(periodicalId);
    }

    public Periodical getPeriodicalById(Long periodicalId) {
        return adminDAO.getPeriodicalById(periodicalId);
    }

    public boolean editPeriodicalById(PeriodicalDTO dto) {
        return adminDAO.editPeriodicalById(dto);
    }

    public List<Reader> getAllReaders() {
        return adminDAO.getAllReaders();
    }

    public boolean lockReader(Long readerId) {
        return adminDAO.lockReader(readerId);
    }

    public boolean unlockReader(Long readerId) {
        return adminDAO.unlockReader(readerId);
    }
}
