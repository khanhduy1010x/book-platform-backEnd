package thebook.fshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thebook.fshop.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {}
