package appVGShop.gestionVideojuegos.infra;

import appVGShop.gestionVideojuegos.domain.Videogame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideogameRepository extends JpaRepository<Videogame, Integer> {
}
