package projeto.loja.lojabijuteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.loja.lojabijuteria.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
