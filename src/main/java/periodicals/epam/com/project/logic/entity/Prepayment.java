package periodicals.epam.com.project.logic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prepayment {
    private Long id;
    private String startDate;
    private String dueDate;
    private Long periodicalId;
    private Long readerId;
}
