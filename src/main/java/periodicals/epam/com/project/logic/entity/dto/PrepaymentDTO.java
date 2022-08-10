package periodicals.epam.com.project.logic.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrepaymentDTO {
    private Integer durationOfSubscription;
    private Long readerId;
    private Long periodicalId;
    private Double amountOfMoney;
}
