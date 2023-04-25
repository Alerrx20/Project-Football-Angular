package ale.atos.apiFootball.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ale.atos.apiFootball.entity.Player;
import ale.atos.apiFootball.entity.Team;
import ale.atos.apiFootball.services.PlayerService;
import ale.atos.apiFootball.services.UploadFileService;
import jakarta.validation.Valid;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class PlayerController {
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private UploadFileService uploadFileService;
	
	@GetMapping("/players")
	public List<Player> allPlayers() {
		return playerService.findAll();
	}
	
	@GetMapping("/players/teams")
	public List<Team> listTeams() {
		return playerService.findAllTeams();
	}
	
	@GetMapping("/players/page/{page}")
	public Page<Player> allPlayers(@PathVariable int page) {
		Pageable pageable = PageRequest.of(page, 4);
		return playerService.findAll(pageable);
	}
	
	@GetMapping("/players/{id}")
	public ResponseEntity<?> getPlayer(@PathVariable Long id) {
		
		Player player = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			player = playerService.findById(id);
		} catch (DataAccessException e) {
			response.put("message", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (player == null) {
			response.put("message", "El jugador ID: " + id.toString().concat(" no existe en la base de datos!"));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Player>(player, HttpStatus.OK);
	}
	
	@PostMapping("/players")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> addPlayer(@Valid @RequestBody Player player, BindingResult result) {
		
		Player newPlayer;
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
			newPlayer = playerService.save(player);
		} catch (DataAccessException e) {
			response.put("message", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "El jugador ha sido creado con éxito!");
		response.put("player", newPlayer);
		
		return new ResponseEntity<HashMap<String, Object>>(response , HttpStatus.CREATED);
	}
	
	@PutMapping("/players/{id}")
	public ResponseEntity<?> updatePlayer(@PathVariable Long id, @Valid @RequestBody Player newPlayer, BindingResult result) {
		
		Player currentPlayer = playerService.findById(id);
		Player playerUpdated = null;
		HashMap<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
			.stream()
			.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
			.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (currentPlayer == null) {
			response.put("message", "Error: no se pudo editar, el jugador ID: " + id.toString().concat(" no existe en la base de datos!"));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			currentPlayer.setName(newPlayer.getName());
			currentPlayer.setLastname(newPlayer.getLastname());
			currentPlayer.setEmail(newPlayer.getEmail());
			currentPlayer.setBirthdate(newPlayer.getBirthdate());
			currentPlayer.setJerseyNumber(newPlayer.getJerseyNumber());
			currentPlayer.setTeam(newPlayer.getTeam());
			
			playerUpdated = playerService.save(currentPlayer);
		} catch (DataAccessException e) {
			response.put("message", "Error al realizar al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "El jugador ha sido actualizado con éxito!");
		response.put("player", playerUpdated);
		return new ResponseEntity<HashMap<String, Object>>(response , HttpStatus.CREATED);
	}
	
	@DeleteMapping("/players/{id}")
	public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
		
		Player player = playerService.findById(id);
		HashMap<String, Object> response = new HashMap<>();
		
		try {
			if (player != null) {
				String lastPhotoName = player.getPhoto();
				uploadFileService.delete(lastPhotoName);				
			}
			
			playerService.delete(player);
		} catch (DataAccessException e) {
			response.put("message", "Error al eliminar el jugador en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "El jugador eliminado con éxito!");
		
		return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/players/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
		
		HashMap<String, Object> response = new HashMap<>();
		Player player = playerService.findById(id);
		
		if (!file.isEmpty()) {
			String fileName = null;
			try {
				fileName = uploadFileService.copy(file);
			} catch (IOException e) {
				response.put("message", "Error al subir la imagen del jugador");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String lastPhotoName = player.getPhoto();
			uploadFileService.delete(lastPhotoName);
			
			player.setPhoto(fileName);
			playerService.save(player);
			
			response.put("player", player);
			response.put("message", "Has subido correctamente la imagen ".concat(fileName));
		}
		
		return new ResponseEntity<HashMap<String, Object>>(response , HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/img/{photoName:.+}")
	public ResponseEntity<Resource> viewPhoto(@PathVariable String photoName) {
		
		Resource resource = null;
		
		try {
			 resource = uploadFileService.load(photoName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + resource.getFilename() + "\"");
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

}
