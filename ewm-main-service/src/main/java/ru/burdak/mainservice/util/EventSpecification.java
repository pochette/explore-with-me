package ru.burdak.mainservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.burdak.mainservice.model.Event;
import ru.burdak.mainservice.model.EventState;

import java.time.LocalDateTime;
import java.util.Collection;

@UtilityClass
public class EventSpecification {
    public Specification<Event> eventDateAfterOrEqual(LocalDateTime rangeStart) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"),
            rangeStart));
    }

    public Specification<Event> eventDateBeforeOreEqual(LocalDateTime rangeEnd) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
    }

    public Specification<Event> hasCategories(Collection<Long> categoryIds) {
        return ((root, query, criteriaBuilder) -> root
            .get("category")
            .get("id")
            .in(categoryIds));
    }

    public Specification<Event> hasInitiators(Collection<Long> userIds) {
        return ((root, query, cb) -> root
            .get("initiator")
            .get(("id"))
            .in(userIds));
    }

    public Specification<Event> hasStates(Collection<EventState> states) {
        return (((root, query, criteriaBuilder) -> root
            .get("state")
            .in(states)));
    }

    public Specification<Event> isPublished() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));
    }

}
