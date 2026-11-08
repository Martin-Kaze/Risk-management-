package com.martin.risk.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Organization {
    @Id
    private String id;

    private String name;
    private String apiKey;
    private int reviewThreshold;
    private int blockThreshold;

    protected Organization() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getReviewThreshold() {
        return reviewThreshold;
    }

    public int getBlockThreshold() {
        return blockThreshold;
    }
}
