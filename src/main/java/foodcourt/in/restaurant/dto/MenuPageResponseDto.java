package foodcourt.in.restaurant.dto;

import java.util.List;
import lombok.Data;

@Data
public class MenuPageResponseDto {
    private List<MenuItemResponseDto> content;
    private int pageNo;                        // Current page number kya hai
    private int pageSize;                      // Ek page pe kitne items the
    private long totalElements;                // Database mein total kitne items hain
    private int totalPages;                    // Total kitne pages banenge
    private boolean isLast;                    // Kya ye aakhiri page hai? (Next button hide karne ke liye)
}