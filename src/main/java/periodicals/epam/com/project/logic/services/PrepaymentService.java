package periodicals.epam.com.project.logic.services;

import lombok.RequiredArgsConstructor;
import periodicals.epam.com.project.logic.dao.PrepaymentDAO;
import periodicals.epam.com.project.logic.entity.dto.PrepaymentDTO;

@RequiredArgsConstructor
public class PrepaymentService {
    private final PrepaymentDAO prepaymentDAO;

    public PrepaymentDTO addSubscription(PrepaymentDTO dto) {
        return prepaymentDAO.addSubscription(dto);
    }

    public boolean deleteSubscription(Long readerId, Long periodicalId) {
        return prepaymentDAO.deleteSubscription(readerId, periodicalId);
    }
}
