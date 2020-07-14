package de.adesso.kicker.user.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Data
@NoArgsConstructor
public class User {

    @Id
    private String userId;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;

    private boolean emailNotifications;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistic_id")
    private Statistic statistic;

    public User(String userId, String firstName, String lastName, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.emailNotifications = true;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
