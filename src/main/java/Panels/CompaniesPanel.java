package Panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import Entities.Factura;
import Entities.Magazin;
import Entities.Produs;
import Entities.User;
import Panels.ProdusePanel.EditTableModel;
import Repository.EntityManagerFactoryCreator;
import Repository.MyRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class CompaniesPanel extends JPanel implements SelectionListener{
	JButton adaugaButton;
	JButton modificaButton;
	JButton stergeButton;
	
	JTable tabelFirme;
	String[] coloaneTabel;
	Object[][] data;
	
	MyRepository<Factura> facturaRepository;
	MyRepository<Magazin> magazinRepository;
	
	User user;
	
	int indexFactura;
	int editingRow;
	
	SelectionListener productsListener;
	
	JScrollPane scrollPane;
	
	List<Magazin> listaMagazine; 
	
	public CompaniesPanel(User user, SelectionListener selectionListener) {
		setVisible(true);
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout());
		
		this.indexFactura = 1;
		this.user = user;
		facturaRepository = new MyRepository<Factura>(Factura.class, 
				EntityManagerFactoryCreator.obtainOrCreateEntityManagerFactory());
		magazinRepository = new MyRepository<Magazin>(Magazin.class,
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
		
		coloaneTabel = new String[] {"Nr. int.", "Nr. doc.", "CUI", "Furnizor", 
				"TVA", "Data", "Scadent", "Valoare", "TVA", "Total"};
		data = getData();
		
		DefaultTableModel tableModel = new EditTableModel(data, coloaneTabel);
		
		tabelFirme = new JTable(tableModel);
		ListSelectionModel selectionModel = tabelFirme.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println();
				if (!e.getValueIsAdjusting()) {
					
					int selectedRow = tabelFirme.getSelectedRow();
					System.out.println("Selected row from companies :" + selectedRow);
					if (selectedRow != -1 && checkAllCellsFromRow(selectedRow)) {
						String selectedFactura = (String) tabelFirme.getValueAt(selectedRow, 1);
						Factura factura = facturaRepository.findById(selectedFactura);
						if (factura != null) {
							productsListener.onSelectionChanged(factura);
						}
					}
					
					if (editingRow != -1 && tabelFirme.isCellEditable(editingRow, 0)) {
						System.out.println("so far so good1");
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
								tabelFirme.editCellAt(editingRow, 0);
								tabelFirme.setColumnSelectionInterval(0, 0);
								tabelFirme.transferFocus();
							}
						}
					}
					System.out.println("editing row selected : " + editingRow);
				}
				
			}
		});
		customizeTable(tabelFirme);
		
		this.scrollPane = new JScrollPane(tabelFirme);
		
		add(scrollPane, BorderLayout.CENTER);

	}
	
	private void customizeTable(JTable tabel) {
		tabel.setRowHeight(25);
		
		TableColumnModel columnModel = tabel.getColumnModel();
	    
	    columnModel.getColumn(0).setPreferredWidth(60);
	    columnModel.getColumn(1).setPreferredWidth(65);
	    columnModel.getColumn(2).setPreferredWidth(65);
	    columnModel.getColumn(3).setPreferredWidth(350);
	    columnModel.getColumn(4).setPreferredWidth(30);
	    columnModel.getColumn(5).setPreferredWidth(70);
	    columnModel.getColumn(6).setPreferredWidth(70);
	    columnModel.getColumn(7).setPreferredWidth(90);
	    columnModel.getColumn(8).setPreferredWidth(90);
	    columnModel.getColumn(9).setPreferredWidth(90);
		
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
	        TableColumn column = columnModel.getColumn(i);
	        column.setHeaderRenderer(new LeftAlignedHeaderRenderer(column.getPreferredWidth()));
	        column.setCellRenderer(new MyCustomCellRenderer(column.getPreferredWidth(), new Color(254,255,180)));
	        if (i == 7 || i == 8 || i == 9)
	        	column.setCellRenderer(new MyCustomCellRenderer(column.getPreferredWidth(), new Color(222,196,255)));
	    }
		
		columnModel.getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
            		boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                if (value instanceof Boolean) {
                    checkBox.setSelected((Boolean) value);
                } else {
                    checkBox.setSelected(false);
                }
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);
                return checkBox;
            }
        });
		columnModel.getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
			@Override
			 public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
					 int row, int column) {
				JCheckBox checkBox = (JCheckBox) super.getTableCellEditorComponent(table, value, 
														isSelected, row, column);
				checkBox.setHorizontalAlignment(SwingConstants.CENTER);
				return checkBox;
			}
		});
		
		
		getListaMagazine();
		JComboBox<String> comboDenumiri = new JComboBox<String>();
		DefaultComboBoxModel model = (DefaultComboBoxModel)comboDenumiri.getModel();
		for (Magazin magazinTemp : listaMagazine) {
			model.addElement(magazinTemp.getNume());
		}
		
		comboDenumiri.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object selected = comboDenumiri.getSelectedItem();
				for (Magazin magazinTemp : listaMagazine) {
					if (magazinTemp.getNume().equals(selected)) {
						tabelFirme.setValueAt(magazinTemp.getCui(), editingRow, 2);
						tabelFirme.setValueAt(magazinTemp.isTva(), editingRow, 4);
					}
				}
				
			}
		});
		
		columnModel.getColumn(3).setCellEditor(new DefaultCellEditor(comboDenumiri));
	}
	
	private void addRow() {
		DefaultTableModel model = (DefaultTableModel) tabelFirme.getModel();
		Object[] newRow = new Object[model.getColumnCount()];
		newRow[0] = indexFactura;
		newRow[7] = 0.0;
		newRow[8] = 0.0;
		newRow[9] = 0.0;
		model.addRow(newRow);
		indexFactura++;
		ListSelectionModel selectionModel = tabelFirme.getSelectionModel();
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
	        
	        Object entityId = tabelFirme.getValueAt(selectedRow, 1);
	        if (entityId != null) {
	            Factura facturaToDelete = facturaRepository.findById((String) entityId);
	            if (facturaToDelete != null) {
	                facturaRepository.delete(facturaToDelete.getNrFactura());
	            }
	        }

	        if (editingRow == selectedRow) {
	            editingRow = -1;
	            modificaButton.setText("Modifica");
	            EditTableModel editModel = (EditTableModel) tabelFirme.getModel();
            	editModel.enableSelectedRowForEditing(selectedRow, false);
	        }
	        
	        model.removeRow(selectedRow);
	        indexFactura--;

	        int newSelectedRow = Math.min(selectedRow, model.getRowCount() - 1);
	        tabelFirme.getSelectionModel().setSelectionInterval(newSelectedRow, newSelectedRow);
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
            		String selectedFactura = (String) tabelFirme.getValueAt(selectedRow, 1);
					Factura factura = facturaRepository.findById(selectedFactura);
            		productsListener.onSelectionChanged(factura);
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
		Magazin magazinToAdd = new Magazin();
		Factura facturaToAdd = new Factura();
		
		String cui = tabelFirme.getValueAt(rowToAdd, 2).toString();
		String numeMagazin = tabelFirme.getValueAt(rowToAdd, 3).toString();
		boolean tva = (Boolean)tabelFirme.getValueAt(rowToAdd, 4);
		
		magazinToAdd.setCui(cui);
		magazinToAdd.setNume(numeMagazin);
		magazinToAdd.setTva(tva);
		magazinToAdd.setListaFacturi(new ArrayList<Factura>());
		magazinToAdd.getListaFacturi().add(facturaToAdd);
		
		String nrFactura = tabelFirme.getValueAt(rowToAdd, 1).toString();
		LocalDate dataFacturare = parseStringToLocalDate(tabelFirme.getValueAt(rowToAdd, 5).toString());
		LocalDate dataScadenta = parseStringToLocalDate(tabelFirme.getValueAt(rowToAdd, 6).toString());
		
		facturaToAdd.setNrFactura(nrFactura);
		facturaToAdd.setDataFacturare(dataFacturare);
		facturaToAdd.setDataScadenta(dataScadenta);
		facturaToAdd.setMagazin(magazinToAdd);
		facturaToAdd.setUser(user);
		facturaToAdd.setListaProduse(new ArrayList<Produs>());
		
		magazinRepository.save(magazinToAdd);
		facturaRepository.save(facturaToAdd);
		
	}

	private LocalDate parseStringToLocalDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return LocalDate.parse(dateString, formatter);
	}
	
	private void getListaMagazine() {
		listaMagazine = new ArrayList<Magazin>(magazinRepository.findAll());
	}

	private Object[][] getData(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		List<Factura> listaFacturiFromUser = user.getListaFacturi();
		Object[][] newData = new Object[listaFacturiFromUser.size()][10];
		int p = 0;
		indexFactura = 1;
		for (Factura facturaTemp : listaFacturiFromUser) {
			newData[p][0] = indexFactura;
			indexFactura++;
			newData[p][1] = facturaTemp.getNrFactura();
			newData[p][2] = facturaTemp.getMagazin().getCui();
			newData[p][3] = facturaTemp.getMagazin().getNume();
			newData[p][4] = facturaTemp.getMagazin().isTva();
			newData[p][5] = facturaTemp.getDataFacturare().format(formatter);
			newData[p][6] = facturaTemp.getDataScadenta().format(formatter);
			Double valoareFactura =0.0;
			for (Produs produsDinFactura : facturaTemp.getListaProduse()) {
				valoareFactura += produsDinFactura.getPret() * produsDinFactura.getCantitate();
			}
			newData[p][7] = valoareFactura;
			newData[p][8] = valoareFactura*19/100;
			newData[p][9] = valoareFactura + valoareFactura*19/100;
			p++;
		}
		return newData;
	}
	
	private boolean checkAllCellsFromRow(int row) {
		for (int i = 0; i < tabelFirme.getColumnCount(); i++){
			Object cellValue = tabelFirme.getValueAt(row, i);
			if (cellValue == null || cellValue.toString().isBlank()) {
				return false;
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
	
	public class EditTableModel extends DefaultTableModel{
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
		
	}
	
	@Override
	public void onSelectionChanged(Factura selectedFactura) {
	}

	@Override
	public void changedProducts() {
		refreshDataFromDatabase();
		int selectedRow = tabelFirme.getSelectedRow();
		EditTableModel tableModel = (EditTableModel) tabelFirme.getModel();
	    tableModel.setDataVector(getData(), coloaneTabel);
	    customizeTable(tabelFirme);
	    tabelFirme.repaint();
	    tabelFirme.revalidate();
	    scrollPane.repaint();
	    scrollPane.revalidate();
	    
	    ListSelectionModel selectionModel = tabelFirme.getSelectionModel();
	    selectionModel.setSelectionInterval(selectedRow, selectedRow);
	}
	
	private void refreshDataFromDatabase() {
		MyRepository<User> userRepository = new MyRepository<>(User.class, 
				EntityManagerFactoryCreator.obtainOrCreateEntityManagerFactory());
		
		this.user = userRepository.findById(this.user.getUsername());
	}
	
	public void setSelectionListener(SelectionListener productsListener) {
		this.productsListener = productsListener;
		System.out.println(productsListener + "Accesat 1");
	}
	
}
