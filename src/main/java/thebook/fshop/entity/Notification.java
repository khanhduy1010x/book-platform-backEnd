package thebook.fshop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="notificationID")
    int ID;

    @ManyToOne
    @JoinColumn(name= "accID")
    Account account;

    String message;

    boolean isRead;

    Date createAt;

}
