package ale.atos.apiFootball.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ale.atos.apiFootball.entity.Player;
import ale.atos.apiFootball.entity.Team;

public interface PlayerService {

	public List<Player> findAll();
	public Page<Player> findAll(Pageable pageable);
	public Player findById(Long id);
	public Player save(Player newPlayer);
	public void delete(Player player);
	public List<Team> findAllTeams();
	
}
