package ale.atos.apiFootball.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ale.atos.apiFootball.entity.Player;
import ale.atos.apiFootball.entity.Team;

public interface PlayerRepository extends JpaRepository<Player, Long> {

	@Query("from Team")
	public List<Team> findAllTeams();
	
}
