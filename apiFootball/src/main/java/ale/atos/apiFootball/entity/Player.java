package ale.atos.apiFootball.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "players")
@Data
public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "No puede estar vacio")
	@Size(min = 4, max = 12, message = "El tamaño tiene que estar entre 4 y 12 caracteres")
	@Column(nullable = false)
	private String name;
	
	@NotEmpty(message = "No puede estar vacio")
	private String lastname;
	
	@NotEmpty(message = "No puede estar vacio")
	@Email(message = "No es una direccion de correo bien formada")
	@Column(nullable = false, unique = true)
	private String email;
	
	@NotNull(message = "No puede estar vacio")
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
	@NotNull(message = "No puede estar vacio")
	private Long jerseyNumber;
	
	private String photo;
	
	@ManyToOne()
	@JoinColumn(name = "team_id")
	private Team team;
	
}
