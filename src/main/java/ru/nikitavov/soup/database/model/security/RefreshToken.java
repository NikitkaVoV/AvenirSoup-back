package ru.nikitavov.soup.database.model.security;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.nikitavov.soup.database.model.IEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken implements IEntity<Integer>  {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "token")
    private String token;
}