package Entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_details")
public class User {
	
	@Id
	private String username;
	
	private String email;
	private String password;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	List<Factura> listaFacturi;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Factura> getListaFacturi() {
		return listaFacturi;
	}

	public void setListaFacturi(List<Factura> listaFacturi) {
		this.listaFacturi = listaFacturi;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
