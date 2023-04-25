package ale.atos.apiFootball.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ale.atos.apiFootball.entity.Team;
import ale.atos.apiFootball.repository.TeamRepository;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;
	
	@Override
	public List<Team> findAll() {
		return teamRepository.findAll();
	}

	@Override
	public Page<Team> findAll(Pageable pageable) {
		return teamRepository.findAll(pageable);
	}

	@Override
	public Team findById(Long id) {
		return teamRepository.findById(id).orElse(null);
	}
	
	public Team findByName(String name) {
		return teamRepository.findByName(name);
	}

	@Override
	public Team save(Team newTeam) {
		return teamRepository.save(newTeam);
	}

	@Override
	public void delete(Team team) {
		teamRepository.delete(team);
	}

}
