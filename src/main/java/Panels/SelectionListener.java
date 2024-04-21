package Panels;

import Entities.Factura;

public interface SelectionListener {
	void onSelectionChanged(Factura selectedFactura);
	void changedProducts();
}
