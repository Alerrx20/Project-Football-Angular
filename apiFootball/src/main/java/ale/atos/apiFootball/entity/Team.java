package ale.atos.apiFootball.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "teams")
@Data
public class Team implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 4, max = 35, message = "El tamaño tiene que estar entre 4 y 35 caracteres")
	@Column(nullable = false)
	private String name;
	
	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 2, max = 3, message = "El acronimo solo debe tener 3 caracteres")
	@Column(nullable = false)
	private String acronym;
	
	@JsonIgnore
	@OneToMany(mappedBy = "team")
	public List<Player> players;
	
}
