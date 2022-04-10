package de.tsearch.highlightcollector.database.postgres.repository;

import de.tsearch.highlightcollector.database.postgres.entity.Highlight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HighlightRepository extends CrudRepository<Highlight, Long> {
    @Query("select p.id from #{#entityName} p")
    List<Long> getAllIds();
}
