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
@Table(name = "Notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationID")
    int ID;

    @ManyToOne
    @JoinColumn(name = "accID")
    Account account;

    @Column(columnDefinition = "TEXT")
    String message;

    String title;
    boolean isRead;

    Date createAt;
}
