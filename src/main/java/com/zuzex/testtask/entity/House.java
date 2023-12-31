package com.zuzex.testtask.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "houses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIncludeProperties({"id"})
    private User host;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "house_id")
    @ToString.Exclude
    @JsonIncludeProperties({"id"})
    private List<User> occupants = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !Objects.equals(Hibernate.getClass(this), Hibernate.getClass(o)))
            return false;
        House house = (House) o;
        return this.id != null && Objects.equals(this.id, house.id);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
