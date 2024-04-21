package Panels;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import Entities.Factura;
import Entities.Produs;
import Entities.User;
import Panels.CompaniesPanel.EditTableModel;
import Panels.CompaniesPanel.LeftAlignedHeaderRenderer;
import Panels.CompaniesPanel.MyCustomCellRenderer;
import Repository.EntityManagerFactoryCreator;
import Repository.MyRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProdusePanel extends JPanel implements SelectionListener {
	JButton adaugaButton;
	JButton modificaButton;
	JButton stergeButton;
	
	JTable tabelFirme;
	String[] coloaneTabel;
	Object[][] data;
	
	User user;
	Factura factura;
	
	MyRepository<Produs> produsRepository;
	
	private int editingRow = -1;
	
	SelectionListener companiesListener;
	
	public ProdusePanel(User user, SelectionListener companiesListener) {
		setVisible(true);
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout());
		
		this.user = user;
		this.produsRepository = new MyRepository<Produs>(Produs.class, 
				EntityManagerFactoryCreator.obtainOrCreateEntityManagerFactory());
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		adaugaButton = new JButton();
		adaugaButton.setPreferredSize(new Dimension(75,25));
		adaugaButton.setText("Adauga");
		adaugaButton.setFont(adaugaButton.getFont().deriveFont(10f));
		adaugaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRow();
			}
		});
		modificaButton = new JButton();
		modificaButton.setPreferredSize(new Dimension(80,25));
		modificaButton.setText("Modific");
		modificaButton.setFont(modificaButton.getFont().deriveFont(10f));
		modificaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleRowEditing();
			}
		});
		stergeButton = new JButton();
		stergeButton.setPreferredSize(new Dimension(75,25));
		stergeButton.setText("Sterge");
		stergeButton.setFont(stergeButton.getFont().deriveFont(10f));
		stergeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteRow();
			}
		});
		
		buttonsPanel.add(adaugaButton);
		buttonsPanel.add(modificaButton);
		buttonsPanel.add(stergeButton);
		
		add(buttonsPanel, BorderLayout.NORTH);
		
		coloaneTabel = new String[] {"Denumire articol/serviciu", "UM", "TVA%", "Cantitate", 
		        "Pret unitar", "Valoare", "TVA", "Total", "Pret vanzare", "Adaos%"};
		
		DefaultTableModel tableModel = new EditTableModel(null, coloaneTabel);
		
		tabelFirme = new JTable(tableModel) {
			JTable tabelFirme = new JTable(tableModel) {
			    @Override
			    public int getSelectedColumn() {
			        return editingRow == -1 ? super.getSelectedColumn() : 0;
			    }

			    @Override
			    public int getSelectedRow() {
			        return editingRow == -1 ? super.getSelectedRow() : editingRow;
			    }
			};
		};
		ListSelectionModel selectionModel = tabelFirme.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = tabelFirme.getSelectedRow();
					if (editingRow != -1 && tabelFirme.isCellEditable(editingRow, 0)) {
						if (!checkAllCellsFromRow(editingRow) && editingRow != selectedRow) {
							int choice = JOptionPane.showOptionDialog(
					                null,
					                "Nu ai completat toate campurile, doresti sa stergi draftul?",
					                "Confirmation",
					                JOptionPane.YES_NO_OPTION,
					                JOptionPane.QUESTION_MESSAGE,
					                null,  // Use default icon
					                new Object[]{"Yes", "No"},  // Custom button labels
					                "Yes"  // Default button label
					        );
							
							if (choice == JOptionPane.YES_OPTION) {
								DefaultTableModel model = (DefaultTableModel) tabelFirme.getModel();
								EditTableModel editModel = (EditTableModel) tabelFirme.getModel();
				            	editModel.enableSelectedRowForEditing(editingRow, false);
								model.removeRow(editingRow);
								editingRow = -1;
								modificaButton.setText("Modifica");
							}
							
							if (choice == JOptionPane.NO_OPTION) {
								selectionModel.setSelectionInterval(editingRow, editingRow);
								tabelFirme.setColumnSelectionInterval(0, 0);
								tabelFirme.editCellAt(editingRow, 0);
								tabelFirme.transferFocus();
							}
						}
					}
					System.out.println("editing row selected : " + editingRow);
				}
				
			}
		});
		
		customizeTable(tabelFirme);
		
		JScrollPane scrollPane = new JScrollPane(tabelFirme);
		
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private void changeTableData(Factura factura) {
		DefaultTableModel model =(DefaultTableModel) tabelFirme.getModel();
		List<Produs> listaProduse = new ArrayList<Produs>(factura.getListaProduse());
		Object[][] newData = new Object[listaProduse.size()][10];
		int p = 0;
		for (Produs produsTemp : listaProduse) {
			newData[p][0] = produsTemp.getNume();
			newData[p][1] = produsTemp.getUnitateMasura();
			newData[p][2] = produsTemp.getTva();
			newData[p][3] = produsTemp.getCantitate();
			newData[p][4] = produsTemp.getPret();
			newData[p][5] = produsTemp.getValoare();
			newData[p][6] = produsTemp.getTvaValoare();
			newData[p][7] = produsTemp.getTvaValoare() + produsTemp.getValoare();
			newData[p][8] = produsTemp.getPretVanzare();
			newData[p][9] = (produsTemp.getPretVanzare()-produsTemp.getPret())/produsTemp.getPret()*100;
			p++;
		}
		
		Object[] columnIdentifiers = new Object[model.getColumnCount()];
        for (int i = 0; i < model.getColumnCount(); i++) {
            columnIdentifiers[i] = model.getColumnName(i);
        }
		
		model.setDataVector(newData, columnIdentifiers);
		customizeTable(tabelFirme);
	}
	
	private void customizeTable(JTable tabel) {
		tabel.setRowHeight(23);
		
		TableColumnModel columnModel = tabel.getColumnModel();
	    
	    columnModel.getColumn(0).setPreferredWidth(350);
	    columnModel.getColumn(1).setPreferredWidth(45);
	    columnModel.getColumn(2).setPreferredWidth(35);
	    columnModel.getColumn(3).setPreferredWidth(80);
	    columnModel.getColumn(4).setPreferredWidth(80);
	    columnModel.getColumn(5).setPreferredWidth(80);
	    columnModel.getColumn(6).setPreferredWidth(80);
	    columnModel.getColumn(7).setPreferredWidth(80);
	    columnModel.getColumn(8).setPreferredWidth(80);
	    columnModel.getColumn(9).setPreferredWidth(50);
		
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
	        TableColumn column = columnModel.getColumn(i);
	        column.setHeaderRenderer(new LeftAlignedHeaderRenderer(column.getPreferredWidth()));
	        column.setCellRenderer(new MyCustomCellRenderer(column.getPreferredWidth(), new Color(254,255,180)));
	    }
	}
	
	private void addRow() {
		DefaultTableModel model = (DefaultTableModel) tabelFirme.getModel();
		ListSelectionModel selectionModel = tabelFirme.getSelectionModel();
		model.addRow(new Object[model.getColumnCount()]);
		selectionModel.setSelectionInterval(model.getRowCount()-1, model.getRowCount()-1);
		tabelFirme.setColumnSelectionInterval(0, 0);
		toggleRowEditing();
	}
	
	private void deleteRow() {
		int selectedRow = tabelFirme.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) tabelFirme.getModel();
            
            if (tabelFirme.isEditing()) {
	            TableCellEditor cellEditor = tabelFirme.getCellEditor();
	            if (cellEditor != null) {
	                cellEditor.stopCellEditing();
	            }
	        }
            
            for (Produs produsTemp : factura.getListaProduse()){
            	if (produsTemp.getNume().equals((String)tabelFirme.getValueAt(selectedRow, 0))) {
            		produsRepository.delete(produsTemp.getId());
            	}
            }
            
            if (editingRow == selectedRow){
            	editingRow = -1;
            	modificaButton.setText("Modifica");
            	EditTableModel editModel = (EditTableModel) tabelFirme.getModel();
            	editModel.enableSelectedRowForEditing(selectedRow, false);
            }
            model.removeRow(selectedRow);
            companiesListener.changedProducts();
        } else {
            JOptionPane.showMessageDialog(this, "Selectati o linie pentru a o sterge.");
        }
	}
	
	private void toggleRowEditing() {
        int selectedRow = tabelFirme.getSelectedRow();
        if (selectedRow != -1) {
            boolean currentEditability = tabelFirme.isCellEditable(selectedRow, 0);
            boolean newEditability = !currentEditability;
            EditTableModel model = (EditTableModel) tabelFirme.getModel();
            
            if (newEditability) {
            	model.enableSelectedRowForEditing(selectedRow, newEditability);
            	editingRow = selectedRow;
            	tabelFirme.editCellAt(selectedRow, 0);
            	tabelFirme.transferFocus();
            	modificaButton.setText("Salveaza");
            }else {
            	
            	if (tabelFirme.isEditing()) {
    	            TableCellEditor cellEditor = tabelFirme.getCellEditor();
    	            if (cellEditor != null) {
    	                cellEditor.stopCellEditing();
    	            }
    	        }
            	
            	if (checkAllCellsFromRow(editingRow)) {
            		model.enableSelectedRowForEditing(selectedRow, newEditability);
            		modificaButton.setText("Modifica");
            		addToDataBase(editingRow);
            		companiesListener.changedProducts();
            		editingRow = -1;
            	}
            	else
            	{
            		JOptionPane.showMessageDialog(this, "Inserati datele in campurile goale !");
            	}
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Selectati o linie pentru a o modifica.");
        }
    }
	
	private void addToDataBase(int rowToAdd) {
		Produs produsToAdd = new Produs();
		
		String nume = tabelFirme.getValueAt(rowToAdd, 0).toString();
		String unitateMasura = tabelFirme.getValueAt(rowToAdd, 1).toString();
		Integer tva = Integer.parseInt(tabelFirme.getValueAt(rowToAdd, 2).toString());
		Integer cantitate = Integer.parseInt(tabelFirme.getValueAt(rowToAdd, 3).toString());
		Double pret = Double.parseDouble(tabelFirme.getValueAt(rowToAdd, 4).toString());
		Double pretVanzare = Double.parseDouble(tabelFirme.getValueAt(rowToAdd, 8).toString());
		
		produsToAdd.setNume(nume);
		produsToAdd.setUnitateMasura(unitateMasura);
		produsToAdd.setTva(tva);
		produsToAdd.setCantitate(cantitate);
		produsToAdd.setPret(pret);
		produsToAdd.setPretVanzare(pretVanzare);
		produsToAdd.setFactura(factura);
		
		factura.getListaProduse().add(produsToAdd);
		
		produsRepository.save(produsToAdd);
	}

	private boolean checkAllCellsFromRow(int row) {
		if (row != -1) {
			for (int i = 0; i < tabelFirme.getColumnCount(); i++){
				Object cellValue = tabelFirme.getValueAt(row, i);
				if (cellValue == null || cellValue.toString().isBlank()) {
					return false;
				}
			}
		}
		return true;
	}
	
	class LeftAlignedHeaderRenderer extends JLabel implements TableCellRenderer {
	    public LeftAlignedHeaderRenderer(int preferedWidth) {
	        setHorizontalAlignment(SwingConstants.LEFT);
	        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
	        setBackground(Color.LIGHT_GRAY);
	        setPreferredSize(new Dimension(preferedWidth, 20));
	    }

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	            boolean hasFocus, int row, int column) {
	        setText(value != null ? value.toString() : "");
	        return this;
	    }
	}
	
	class MyCustomCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
		Color backgroundColor;
		private final Color selectedBorderColor = Color.BLUE;
		
		public MyCustomCellRenderer(int preferedWidth, Color backgroundColor) {
			this.backgroundColor = backgroundColor;
			setBackground(backgroundColor);
			setPreferredSize(new Dimension(preferedWidth, 50));
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText(value != null ? value.toString() : "");
			if (isSelected){
				this.setBackground(new Color(161,231,255));
				if (hasFocus) {
					this.setBorder(BorderFactory.createLineBorder(selectedBorderColor));
				} else {
					this.setBorder(null);
				}
				
			}else {
				this.setBackground(backgroundColor);
				this.setBorder(null);
			}
			return this;
			
		}
		
	}
	
	class EditTableModel extends DefaultTableModel{
		int selectedRow = -1;
		boolean editability;
		
		public EditTableModel(Object[][] data, Object[] columnNames) {
			super(data,columnNames);
			this.selectedRow = -1;
			this.editability = false;
		}
		
		public void enableSelectedRowForEditing(int selectedRow, boolean editabilty) {
			this.selectedRow = selectedRow;
			this.editability = editabilty;
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
            if (row == selectedRow && selectedRow != -1){
            	return editability;
            }else
            	return false;
        }
		
		@Override
		public void setValueAt(Object aValue, int row, int column) {
		    System.out.println(aValue.getClass().getSimpleName() + " " + row + " " + column + ";");
		    super.setValueAt(aValue, row, column);
		    
		    if (column == 5 || column == 6) {
		        Object valoareObj = getValueAt(row, 5);
		        Object tvaObj = getValueAt(row, 6);
		        
		        System.out.println(valoareObj + " " + tvaObj);
		        
		        if (valoareObj != null && tvaObj != null) {
		            double valoare = Double.parseDouble(valoareObj.toString());
		            double tva = Double.parseDouble(tvaObj.toString());
		            double total = valoare + tva;
		            super.setValueAt(total, row, 7);
		            setValueAt(total, row, 7);
		        }
		    }

		    if (column == 3 || column == 4) {
		        Object cantitateObj = getValueAt(row, 3);
		        Object pretObj = getValueAt(row, 4);
		        
		        System.out.println(pretObj + " " + cantitateObj);
		        
		        if (cantitateObj != null && pretObj != null) {
		        	System.out.println("INTRU");
		            int cantitate = Integer.parseInt(cantitateObj.toString());
		            double pret = Double.parseDouble(pretObj.toString());
		            double valoare = cantitate * pret;
		            super.setValueAt(valoare, row, 5);
		            setValueAt(valoare, row, 5);
		        }
		    }

		    if (column == 2 || column == 5) {
		        Object tvaObj = getValueAt(row, 2);
		        Object valoareObj = getValueAt(row, 5);
		        
		        System.out.println(tvaObj + " " + valoareObj);
		        
		        if (tvaObj != null && valoareObj != null) {
		            int tva = Integer.parseInt(tvaObj.toString());
		            double valoare = Double.parseDouble(valoareObj.toString());
		            double tvaValoare = (valoare * tva) / 100;
		            super.setValueAt(tvaValoare, row, 6);
		            setValueAt(tvaValoare, row, 6);
		        }
		    }
		    
		    if (column == 4 || column == 8) {
		    	Object pretObj = getValueAt(row, 4);
		    	Object pretVanzareObj = getValueAt(row, 8);
		    	
		    	if (pretObj != null && pretVanzareObj != null) {
		    		double pret = Double.parseDouble(pretObj.toString());
		    		double pretVanzare = Double.parseDouble(pretVanzareObj.toString());
		    		double adaos = ((pretVanzare - pret)/pret)*100;
		    		String formattedAdaos = String.format("%.2f", adaos);
		    		super.setValueAt(formattedAdaos, row, 9);
		    		setValueAt(formattedAdaos, row, 9);
		    	}
		    }
		}
		
	}

	@Override
	public void onSelectionChanged(Factura selectedFactura) {
		this.factura = selectedFactura;
		changeTableData(selectedFactura);
	}

	@Override
	public void changedProducts() {
		
	}

	public void setSelectionListener(SelectionListener companiesListener) {
		this.companiesListener = companiesListener;
		System.out.println(companiesListener + "Accesat 2");
	}
}
