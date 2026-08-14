package ru.burdak.mainservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.burdak.mainservice.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    //TODO в ДТO добавить аннотацию: @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") над полем
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    @ToString.Exclude
    private User initiator;

    @OneToOne(cascade = CascadeType.ALL)
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(value = EnumType.STRING)
    private EventState state;

    @Column(nullable = false)
    private String title;

    private Long views;

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Event event = (Event) o;
        return getId() != null && Objects.equals(getId(), event.getId());
    }
}
