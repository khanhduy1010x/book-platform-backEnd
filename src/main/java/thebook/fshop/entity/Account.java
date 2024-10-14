package thebook.fshop.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thebook.fshop.helper.LoginType;
import thebook.fshop.helper.MemberType;
import thebook.fshop.helper.Role;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accID")
    int accID;

    String phone;
    String password;

    @Enumerated(EnumType.STRING)
    Role role;

    Long amount;
    String avatar;
    String fullName;

    @Enumerated(EnumType.STRING)
    MemberType memberType;

    Date birth;

    String email;

    @Column(name = "skip_password_prompt", nullable = false)
    boolean skip_password_prompt;

    LoginType loginType;
}
