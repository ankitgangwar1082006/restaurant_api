package foodcourt.in.restaurant.entity;

import java.util.ArrayList;
import java.util.List;

import foodcourt.in.restaurant.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @ToString.Exclude
    private UserProfile userProfile;

    @OneToMany(mappedBy="user",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Order> orderList = new ArrayList<>();
}
