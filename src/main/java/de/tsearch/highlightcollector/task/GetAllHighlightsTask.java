package de.tsearch.highlightcollector.task;

import de.tsearch.highlightcollector.database.postgres.entity.Broadcaster;
import de.tsearch.highlightcollector.database.postgres.repository.BroadcasterRepository;
import de.tsearch.tclient.HighlightClient;
import de.tsearch.tclient.data.PagedResponse;
import de.tsearch.tclient.http.respone.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class GetAllHighlightsTask {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final HighlightClient highlightClient;
    private final BroadcasterRepository broadcastersRepository;
    private final HighlightTaskUtil highlightTaskUtil;

    public GetAllHighlightsTask(HighlightClient highlightClient, BroadcasterRepository broadcastersRepository, HighlightTaskUtil highlightTaskUtil) {
        this.highlightClient = highlightClient;
        this.broadcastersRepository = broadcastersRepository;
        this.highlightTaskUtil = highlightTaskUtil;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000, initialDelay = 5 * 60 * 1000)
    protected void getAllHighlights() {
        logger.info("Get all highlights");
        List<Future<?>> futures = new ArrayList<>();
        for (Broadcaster broadcaster : broadcastersRepository.findAll()) {
            futures.add(executorService.submit(() -> getAllHighlightsBroadcaster(broadcaster)));

        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (ExecutionException | InterruptedException ignored) {

            }
        }
        logger.info("Finished getting all highlights");
    }

    private void getAllHighlightsBroadcaster(Broadcaster broadcaster) {
        PagedResponse<Video> highlightResponse = highlightClient.getAllHighlightsUncached(broadcaster.getId());
        logger.debug("Found {} highlights for broadcaster {}({})", highlightResponse.getData().size(), broadcaster.getDisplayName(), broadcaster.getId());
        for (Video video : highlightResponse.getData()) {
            highlightTaskUtil.createOrUpdateHighlight(video, broadcaster);
        }
    }
}
