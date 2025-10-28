package course_project.course_project.dto;

import course_project.course_project.model.OrderItem;
import course_project.course_project.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private int userId;
    private List<OrderItem> orderItems;
    private LocalDateTime orderTime;
    private OrderStatus status;
    private BigDecimal total;
}
