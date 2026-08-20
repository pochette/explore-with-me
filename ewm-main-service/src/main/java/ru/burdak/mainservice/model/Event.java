package ru.burdak.mainservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type Event.
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое описание события
     */
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    /**
     * Дата создания события
     */
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    /**
     * Полное описание события
     */
    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    /**
     * Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
     */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    /**
     * Пользователь, создавший событие
     */
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    @ToString.Exclude
    private User initiator;

    /**
     * Место проведения мероприятия
     */
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;

    /**
     * Нужно ли оплачивать участие
     */
    @Column(nullable = false)
    private Boolean paid;

    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;

    /**
     * Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    /**
     * Список состояний жизненного цикла события
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private EventState state;

    /**
     * Заголовок
     */
    @Column(nullable = false, length = 120)
    private String title;

    /**
     * Количество просмотрев события
     */
    private Long views;

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this)
            .getHibernateLazyInitializer()
            .getPersistentClass()
            .hashCode() : getClass().hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o)
            .getHibernateLazyInitializer()
            .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this)
            .getHibernateLazyInitializer()
            .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Event event = (Event) o;
        return getId() != null && Objects.equals(getId(), event.getId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "annotation = " + annotation + ", " +
            "confirmedRequests = " + confirmedRequests + ", " +
            "createdOn = " + createdOn + ", " +
            "description = " + description + ", " +
            "eventDate = " + eventDate + ", " +
            "initiator = " + initiator + ", " +
            "location = " + location + ", " +
            "paid = " + paid + ", " +
            "participantLimit = " + participantLimit + ", " +
            "publishedOn = " + publishedOn + ", " +
            "requestModeration = " + requestModeration + ", " +
            "state = " + state + ", " +
            "title = " + title + ", " +
            "views = " + views + ")";
    }
}
