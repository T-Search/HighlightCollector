package de.tsearch.highlightcollector.task;

import de.tsearch.highlightcollector.database.postgres.converter.TVideoConverter;
import de.tsearch.highlightcollector.database.postgres.entity.Highlight;
import de.tsearch.highlightcollector.database.postgres.repository.HighlightRepository;
import de.tsearch.tclient.HighlightClient;
import de.tsearch.tclient.http.respone.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CheckHighlightsTask {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HighlightClient highlightClient;

    private final HighlightRepository highlightRepository;

    private final TVideoConverter tVideoConverter;

    public CheckHighlightsTask(HighlightClient highlightClient, HighlightRepository highlightRepository, TVideoConverter tVideoConverter) {
        this.highlightClient = highlightClient;
        this.highlightRepository = highlightRepository;
        this.tVideoConverter = tVideoConverter;
    }

    //Once a day
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000, initialDelay = 12 * 60 * 60 * 1000)
    protected void checkAllHighlights() {
        logger.info("Start check all existing highlights");
        List<Long> ids = highlightRepository.getAllIds();

        final int batchSize = 1_000;
        for (int round = 0; round < Math.ceil(((float) ids.size()) / batchSize); round++) {
            logger.debug("Check highlights starting index {} from {}", round * batchSize, ids.size());
            List<Long> list = ids.subList(round * batchSize, Math.min(ids.size(), (round + 1) * batchSize));
            checkClipTask(list);
        }
        logger.info("Finishing check all existing highlights");
    }

    private void checkClipTask(List<Long> ids) {
        List<Video> activeHighlights = highlightClient.getAllActiveHighlightsUncached(ids);
        for (Video activeHighlight : activeHighlights) {
            Optional<Highlight> highlightOptional = highlightRepository.findById(activeHighlight.getId());
            if (highlightOptional.isEmpty()) continue;
            tVideoConverter.updateDatabaseHighlightProperties(highlightOptional.get(), activeHighlight);
        }

        //Delete old clips
        List<Long> toDeleteIds = ids.stream().filter(id -> activeHighlights.stream().noneMatch(clip -> id == clip.getId())).toList();
        highlightRepository.deleteAllById(toDeleteIds);
        logger.debug("To delete clip ids: {}", toDeleteIds);
    }

}
