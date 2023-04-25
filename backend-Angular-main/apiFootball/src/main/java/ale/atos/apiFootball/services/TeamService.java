package ale.atos.apiFootball.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ale.atos.apiFootball.entity.Team;

public interface TeamService {

	public List<Team> findAll();
	public Page<Team> findAll(Pageable pageable);
	public Team findById(Long id);
	public Team save(Team newTeam);
	public void delete(Team team);
	
}
