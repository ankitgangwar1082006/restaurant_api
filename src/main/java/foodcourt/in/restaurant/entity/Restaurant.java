package foodcourt.in.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private Double rating;

    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL)
    @ToString.Exclude
    List<MenuItem> listMenu = new ArrayList<>();

    private boolean status = true;
}
