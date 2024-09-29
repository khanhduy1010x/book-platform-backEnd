package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thebook.fshop.entity.MembershipPackage;

@Repository
public interface MembershipPackageRepository extends JpaRepository<MembershipPackage, Integer> {
}
