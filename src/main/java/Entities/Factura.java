package Entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Factura {
	
	@Id
	private String nrFactura;
	private LocalDate dataFacturare;
	private LocalDate dataScadenta;
	
	@ManyToOne
	@JoinColumn(name="cui")
	private Magazin magazin;
	
	@OneToMany(mappedBy="factura", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Produs> listaProduse;
	
	@ManyToOne
	@JoinColumn(name="username")
	private User user;

	public String getNrFactura() {
		return nrFactura;
	}

	public void setNrFactura(String nrFactura) {
		this.nrFactura = nrFactura;
	}

	public LocalDate getDataFacturare() {
		return dataFacturare;
	}

	public void setDataFacturare(LocalDate dataFacturare) {
		this.dataFacturare = dataFacturare;
	}

	public LocalDate getDataScadenta() {
		return dataScadenta;
	}

	public void setDataScadenta(LocalDate dataScadenta) {
		this.dataScadenta = dataScadenta;
	}

	public Magazin getMagazin() {
		return magazin;
	}

	public void setMagazin(Magazin magazin) {
		this.magazin = magazin;
	}

	public List<Produs> getListaProduse() {
		return listaProduse;
	}

	public void setListaProduse(List<Produs> listaProduse) {
		this.listaProduse = listaProduse;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
