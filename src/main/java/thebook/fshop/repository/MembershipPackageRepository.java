package thebook.fshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.MembershipPackage;
import thebook.fshop.helper.MemberType;

import java.util.List;

@Repository
public interface MembershipPackageRepository extends JpaRepository<MembershipPackage, Integer> {

    Page<MembershipPackage> findBy(Pageable pageable);
    @Query("SELECT m.memberType, COUNT(m) " +
            "FROM MembershipPackage m " +
            "GROUP BY m.memberType")
    List<Object[]> countAllMemberType();
    Page<MembershipPackage> findAllByMemberType(MemberType memberType, Pageable pageable);
}
