package Entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Magazin {
	@Id
	private String cui;
	private String nume;
	private boolean tva;
	
	@OneToMany(mappedBy="magazin", cascade = CascadeType.ALL)
	private List<Factura> listaFacturi;

	public String getCui() {
		return cui;
	}

	public void setCui(String cui) {
		this.cui = cui;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public boolean isTva() {
		return tva;
	}

	public void setTva(boolean tva) {
		this.tva = tva;
	}

	public List<Factura> getListaFacturi() {
		return listaFacturi;
	}

	public void setListaFacturi(List<Factura> listaFacturi) {
		this.listaFacturi = listaFacturi;
	}
	
	
}
