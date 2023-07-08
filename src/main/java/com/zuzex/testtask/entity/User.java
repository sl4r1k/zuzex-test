package com.zuzex.testtask.entity;

import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "password")
    private char[] password;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !Objects.equals(Hibernate.getClass(this), Hibernate.getClass(o)))
            return false;
        User user = (User) o;
        return this.id != null && Objects.equals(this.id, user.id);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
