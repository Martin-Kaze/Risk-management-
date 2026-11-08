package com.martin.risk.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Organization {
    @Id
    private String id;

    private String Comp_name;
    private String ApiKey;
    private int reviewThreshold;
    private int blockThreshold;

    protected Organization(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComp_name() {
        return Comp_name;
    }

    public void setComp_name(String comp_name) {
        Comp_name = comp_name;
    }

    public String getApiKey() {
        return ApiKey;
    }

    public void setApiKey(String apiKey) {
        ApiKey = apiKey;
    }

    public int getReviewThreshold() {
        return reviewThreshold;
    }

    public void setReviewThreshold(int reviewThreshold) {
        this.reviewThreshold = reviewThreshold;
    }

    public int getBlockThreshold() {
        return blockThreshold;
    }

    public void setBlockThreshold(int blockThreshold) {
        this.blockThreshold = blockThreshold;
    }
}
