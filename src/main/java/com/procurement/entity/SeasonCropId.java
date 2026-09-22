package com.procurement.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SeasonCropId implements Serializable {
    @Column(name="season_id") private Long seasonId;
    @Column(name="crop_id") private Long cropId;
    public SeasonCropId(){}
    public SeasonCropId(Long seasonId, Long cropId){this.seasonId=seasonId;this.cropId=cropId;}
    public Long getSeasonId(){return seasonId;} public Long getCropId(){return cropId;}
    public void setSeasonId(Long v){seasonId=v;} public void setCropId(Long v){cropId=v;}
    @Override public boolean equals(Object o){if(this==o)return true;if(!(o instanceof SeasonCropId x))return false;return Objects.equals(seasonId,x.seasonId)&&Objects.equals(cropId,x.cropId);}
    @Override public int hashCode(){return Objects.hash(seasonId,cropId);}
}
