package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Location;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    @DisplayName("geometry")
    public void test() throws Exception {
        // given
        GeometryFactory factory = new GeometryFactory();
        Point testPoint = factory.createPoint(new Coordinate(123.123, 456.456));
        Location testLocation = new Location();
        testLocation.setPoint(testPoint);

        // when
        locationRepository.save(testLocation);

        // then
        System.out.println("testLocation = " + testLocation);

    }
}