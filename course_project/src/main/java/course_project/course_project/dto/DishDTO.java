package course_project.course_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {
    private long id;
    private String name;
    private String description;
    private String url;
    private BigDecimal price;
    private int weight;

    //для поиска по названию блюда
    private int restaurantId;
    private String restaurantName;
}
