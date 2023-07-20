package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
