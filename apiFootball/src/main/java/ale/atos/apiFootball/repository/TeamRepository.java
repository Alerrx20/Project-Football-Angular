package ale.atos.apiFootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ale.atos.apiFootball.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

	public Team findByName(String name);
}
