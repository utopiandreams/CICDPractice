package study.datajpa.entity;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Location {
    @Id @GeneratedValue
    private Long id;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point point;

}
