package ru.nikitavov.soup.database.model.base;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.nikitavov.soup.database.model.IEntity;
import ru.nikitavov.soup.database.model.security.Permission;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role implements IEntity<Integer> {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank
    @Column(name = "name", nullable = false)
    String name;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")}
    )
    final Set<Permission> permissions = new LinkedHashSet<>();
}
