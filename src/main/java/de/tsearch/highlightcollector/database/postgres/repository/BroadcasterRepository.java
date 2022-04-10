package de.tsearch.highlightcollector.database.postgres.repository;

import de.tsearch.highlightcollector.database.postgres.entity.Broadcaster;
import de.tsearch.highlightcollector.database.postgres.entity.StreamStatus;
import org.springframework.data.repository.CrudRepository;

public interface BroadcasterRepository extends CrudRepository<Broadcaster, Long> {
    Iterable<Broadcaster> findAllByStatus(StreamStatus status);
}
