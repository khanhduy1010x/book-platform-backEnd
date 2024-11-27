package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.ShipInfo;
@Repository
public interface ShipInfoRepository extends JpaRepository<ShipInfo, Integer> {

}
