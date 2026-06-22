package foodcourt.in.restaurant.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import foodcourt.in.restaurant.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User user;

    private LocalDate orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrderItem> listOrderItem = new ArrayList<>();

//    @PrePersist
//    @PreUpdate
//    public void calculateTotalAmount() {
//        Double totalAmt = 0.0;
//        if (listOrderItem != null && !listOrderItem.isEmpty()) {
//            for (OrderItem item : listOrderItem) {
//                if (item != null && item.getMenuItem() != null) {
//                    Integer qty = item.getQuantity();
//                    Double price = item.getMenuItem().getPrice();
//                    if (qty != null && price != null) {
//                        totalAmt += qty * price;
//                    }
//                }
//            }
//        }
//        this.amt = totalAmt;
//    }
}
