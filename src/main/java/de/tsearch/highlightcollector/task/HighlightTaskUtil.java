package de.tsearch.highlightcollector.task;

import de.tsearch.highlightcollector.database.postgres.converter.TVideoConverter;
import de.tsearch.highlightcollector.database.postgres.entity.Broadcaster;
import de.tsearch.highlightcollector.database.postgres.entity.Highlight;
import de.tsearch.highlightcollector.database.postgres.repository.HighlightRepository;
import de.tsearch.tclient.http.respone.Video;
import org.springframework.stereotype.Service;

@Service
public class HighlightTaskUtil {

    private final TVideoConverter tVideoConverter;

    private final HighlightRepository highlightRepository;

    public HighlightTaskUtil(TVideoConverter tVideoConverter, HighlightRepository highlightRepository) {
        this.tVideoConverter = tVideoConverter;
        this.highlightRepository = highlightRepository;
    }

    protected void createOrUpdateHighlight(Video video, Broadcaster broadcaster) {
        Highlight highlight = new Highlight();
        highlight.setBroadcaster(broadcaster);
        tVideoConverter.updateDatabaseHighlightProperties(highlight, video);
        highlightRepository.save(highlight);
    }
}
