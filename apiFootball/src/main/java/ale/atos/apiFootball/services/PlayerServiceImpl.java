package ale.atos.apiFootball.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ale.atos.apiFootball.entity.Player;
import ale.atos.apiFootball.entity.Team;
import ale.atos.apiFootball.repository.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {
	
	@Autowired
	private PlayerRepository playerRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Player> findAll() {
		return playerRepository.findAll();
	}

	@Override
	public Page<Player> findAll(Pageable pageable) {
		return playerRepository.findAll(pageable);
	}

	@Override
	public Player findById(Long id) {
		return playerRepository.findById(id).orElse(null);
	}

	@Override
	public Player save(Player newPlayer) {
		return playerRepository.save(newPlayer);
	}

	@Override
	public void delete(Player player) {
		playerRepository.delete(player);
	}

	@Override
	public List<Team> findAllTeams() {
		return playerRepository.findAllTeams();
	}
	
	

}
