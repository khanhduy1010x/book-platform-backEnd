package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
}
