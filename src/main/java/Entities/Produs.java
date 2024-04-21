package Entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Produs {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String nume;
	private String unitateMasura;
	private int tva;
	private int cantitate;
	private double pret;
	private double pretVanzare;
	
	@ManyToOne
	@JoinColumn(name="nrFactura")
	private Factura factura;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getUnitateMasura() {
		return unitateMasura;
	}

	public void setUnitateMasura(String unitateMasura) {
		this.unitateMasura = unitateMasura;
	}

	public int getTva() {
		return tva;
	}

	public void setTva(int tva) {
		this.tva = tva;
	}

	public int getCantitate() {
		return cantitate;
	}

	public void setCantitate(int cantitate) {
		this.cantitate = cantitate;
	}

	public double getPret() {
		return pret;
	}

	public void setPret(double pret) {
		this.pret = pret;
	}

	public double getPretVanzare() {
		return pretVanzare;
	}

	public void setPretVanzare(double pretVanzare) {
		this.pretVanzare = pretVanzare;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	public double getValoare() {
		return pret*cantitate;
	}
	
	public double getTvaValoare() {
		return (pret*cantitate*tva)/100;
	}
}
