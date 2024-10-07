package thebook.fshop.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "UserMemberShips")
public class UserMemberShip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userMSID")
    int id;

    @ManyToOne
    @JoinColumn(name = "accID")
    Account account;

    @ManyToOne
    @JoinColumn(name = "MP_ID")
    MembershipPackage membershipPackage;

    Date startDate;
    Date endDate;
}
