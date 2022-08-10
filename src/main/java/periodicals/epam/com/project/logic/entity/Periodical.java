package periodicals.epam.com.project.logic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Periodical {
    private long id;
    private String name;
    private String topic;
    private Double cost;
    private String description;
    private Boolean isDeleted;
}
