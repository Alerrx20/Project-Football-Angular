package ale.atos.apiFootball.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ale.atos.apiFootball.entity.Team;
import ale.atos.apiFootball.services.TeamService;
import jakarta.validation.Valid;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class TeamController {

	@Autowired
	private TeamService teamService;
	
	@GetMapping("/teams")
	public List<Team> allTeams() {
		return teamService.findAll();
	}
	
	@GetMapping("/teams/page/{page}")
	public Page<Team> allPlayers(@PathVariable int page) {
		Pageable pageable = PageRequest.of(page, 4);
		return teamService.findAll(pageable);
	}
	
	@GetMapping("/teams/{id}")
	public ResponseEntity<?> getTeam(@PathVariable Long id) {
		
		Team team = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			team = teamService.findById(id);
		} catch (DataAccessException e) {
			response.put("message", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (team == null) {
			response.put("message", "El equipo ID: " + id.toString().concat(" no existe en la base de datos!"));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Team>(team, HttpStatus.OK);
	}
	
	@PostMapping("/teams")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> addTeam(@Valid @RequestBody Team team, BindingResult result) {
		
		Team newTeam;
		HashMap<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
			.stream()
			.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
			.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			newTeam = teamService.save(team);
		} catch (DataAccessException e) {
			response.put("message", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "El equipo ha sido creado con éxito!");
		response.put("team", newTeam);
		
		return new ResponseEntity<HashMap<String, Object>>(response , HttpStatus.CREATED);
	}
	
	@PutMapping("/teams/{id}")
	public ResponseEntity<?> updateTeam(@PathVariable Long id, @Valid @RequestBody Team newTeam, BindingResult result) {
		
		Team currentTeam = teamService.findById(id);
		Team teamUpdated = null;
		HashMap<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
			.stream()
			.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
			.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (currentTeam == null) {
			response.put("message", "Error: no se pudo editar, el equipo ID: " + id.toString().concat(" no existe en la base de datos!"));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			currentTeam.setName(newTeam.getName());
			currentTeam.setAcronym(newTeam.getAcronym());
					
			teamUpdated = teamService.save(currentTeam);
		} catch (DataAccessException e) {
			response.put("message", "Error al realizar el actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "El equipo ha sido actualizado con éxito!");
		response.put("team", teamUpdated);
		return new ResponseEntity<HashMap<String, Object>>(response , HttpStatus.CREATED);
	}
	
	@DeleteMapping("/teams/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Team team = teamService.findById(id);
		HashMap<String, Object> response = new HashMap<>();
		
		try {
			teamService.delete(team);
		} catch (DataAccessException e) {
			response.put("message", "Error al eliminar el equipo en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "El equipo eliminado con éxito!");
		
		return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
	}
	
}
