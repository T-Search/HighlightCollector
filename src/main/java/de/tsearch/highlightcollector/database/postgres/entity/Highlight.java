package de.tsearch.highlightcollector.database.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Highlight {
    @Id
    private long id;

    @ManyToOne
    private Broadcaster broadcaster;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Date createdAt;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Date publishedAt;

    @Column
    private String thumbnailUrl;

    @Column
    private long viewCount;

    @Column
    private String language;

    @Column
    private String duration;
}
