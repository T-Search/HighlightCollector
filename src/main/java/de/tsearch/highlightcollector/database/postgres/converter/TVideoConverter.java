package de.tsearch.highlightcollector.database.postgres.converter;

import de.tsearch.highlightcollector.database.postgres.entity.Highlight;
import de.tsearch.tclient.http.respone.Video;
import org.springframework.stereotype.Service;

@Service
public class TVideoConverter {
    /**
     * Update given highlight with information from video. This will not update the broadcaster!
     *
     * @param highlight database entity to update
     * @param video     Twitch video entity with information
     */
    public void updateDatabaseHighlightProperties(Highlight highlight, Video video) {
        highlight.setId(video.getId());
        highlight.setTitle(video.getTitle());
        highlight.setDescription(getNullStringWhenEmpty(video.getDescription()));
        highlight.setCreatedAt(video.getCreatedAt());
        highlight.setPublishedAt(video.getPublishedAt());
        highlight.setThumbnailUrl(video.getThumbnailUrl());
        highlight.setViewCount(video.getViewCount());
        highlight.setLanguage(video.getLanguage());
        highlight.setDuration(video.getDuration());
    }

    private String getNullStringWhenEmpty(String s) {
        if (s == null) return null;
        if (s.isEmpty()) return null;
        return s;
    }
}
