package ru.burdak.mainservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.burdak.mainservice.model.Event;
import ru.burdak.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.Collection;

@UtilityClass
public class EventSpecification {
    public Specification<Event> hasCategories(Collection<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> root
            .get("category")
            .get("id")
            .in(categoryIds));
    }

    public Specification<Event> hasInitiators(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return null;
        }
        return ((root, query, cb) -> root
            .get("initiator")
            .get(("id"))
            .in(userIds));
    }

    public static Specification<Event> hasOnlyAvailable(Boolean onlyAvailable) {
        if (!Boolean.TRUE.equals(onlyAvailable)) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.equal(root.get("participantLimit"), 0),
            criteriaBuilder.lessThan(root.get("confirmedRequests"), root.get("participantLimit"))
        );
    }

    public static Specification<Event> hasPaid(Boolean paid) {
        if (paid == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> root.get("paid").in(paid));
    }

    /**
     *
     * @param rangeStart LocalDateTime - начальная дата диапазона, может быть null
     * @param rangeEnd LocalDateTime - конечная дата диапазона, может быть null
     * @return Возвращает Specification для фильтрации событий по диапазону дат. Если оба параметра null, возвращает
     * Specification для событий с датой после текущего времени.
     */
    public Specification<Event> hasRangeDate(LocalDateTime rangeStart, LocalDateTime rangeEnd) {

        if (rangeEnd == null && rangeStart == null) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("eventDate"),
                LocalDateTime.now()));
        }
        Specification<Event> spec = Specification.where(null);
        return spec.and(hasEventDateAfterOrEqual(rangeStart))
            .and(hasEventDateBeforeOreEqual(rangeEnd));

    }

    public Specification<Event> hasEventDateAfterOrEqual(LocalDateTime rangeStart) {
        if (rangeStart == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"),
            rangeStart));
    }

    /**
     *
     * @param rangeEnd LocalDateTime - конечная дата диапазона, может быть null
     * @return Возвращает Specification для фильтрации событий по дате события, которая должна быть меньше или равна
     * указанной дате rangeEnd. Если rangeEnd равен null, возвращает null.
     */
    public Specification<Event> hasEventDateBeforeOreEqual(LocalDateTime rangeEnd) {
        if (rangeEnd == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
    }

    public Specification<Event> hasStates(Collection<EventState> states) {
        if (states == null || states.isEmpty()) {
            return null;
        }
        return (((root, query, criteriaBuilder) -> root
            .get("state")
            .in(states)));
    }

    public static Specification<Event> hasText(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%")));
    }

}
