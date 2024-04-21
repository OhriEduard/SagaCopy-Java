package Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.mysql.cj.Session;

import Entities.Magazin;
import Panels.CompaniesPanel.EditTableModel;
import Panels.LoginPanel.*;
import Repository.EntityManagerFactoryCreator;
import Repository.MyRepository;

public class AdminDataPanel extends JPanel implements AdminActions {
	
	JTable tabelFirme;
	
	MyRepository<Magazin> magazinRepository;
	
	AdminActions adminActionPanelActions;
	
	int editingRow = -1;
	
	public AdminDataPanel () {
		this.setBackground(Color.BLUE);
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(18,18,18,18));
		
		magazinRepository = new MyRepository<Magazin>(Magazin.class, 
				EntityManagerFactoryCreator.obtainOrCreateEntityManagerFactory());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		//gbc.insets = new Insets(10, 10, 10, 10);
		
		String[] columnNames = {"CUI", "DENUMIRE", "TVA"};
		
		DefaultTableModel tableModel = new EditTableModel(null, columnNames); // FOR FUTURE CUSTOMISATION
		tabelFirme = new JTable(tableModel);
		
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
					                null,
					                new Object[]{"Yes", "No"},
					                "Yes"
					        );
							
							if (choice == JOptionPane.YES_OPTION) {
								DefaultTableModel model = (DefaultTableModel) tabelFirme.getModel();
								EditTableModel editModel = (EditTableModel) tabelFirme.getModel();
				            	editModel.enableSelectedRowForEditing(editingRow, false);
								model.removeRow(editingRow);
								editingRow = -1;
								adminActionPanelActions.modificaAction();
							}
							
							if (choice == JOptionPane.NO_OPTION) {
								selectionModel.setSelectionInterval(editingRow, editingRow);
								tabelFirme.setColumnSelectionInterval(0, 0);
								tabelFirme.editCellAt(editingRow, 0);
								tabelFirme.transferFocus();
							}
						}
					}
				}
				
			}
		});
		
		getData();
		customizeTable();
		
		JScrollPane scrollPane = new JScrollPane(tabelFirme);
		
		this.add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public void customizeTable() {
		tabelFirme.setRowHeight(23);
		
		TableColumnModel columnModel = tabelFirme.getColumnModel();
		//tabelFirme.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);21
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(0).setPreferredWidth(200);
		columnModel.getColumn(0).setPreferredWidth(20);
		
		columnModel.getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public void setPreferredSize(Dimension preferredSize) {
				super.setPreferredSize(new Dimension(50,50));
			}
		});
		columnModel.getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public void setPreferredSize(Dimension preferredSize) {
				super.setPreferredSize(new Dimension(200,50));
			}
		});
		columnModel.getColumn(2).setMinWidth(3);
		columnModel.getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			
			@Override
			public void setPreferredSize(Dimension preferredSize) {
				super.setPreferredSize(new Dimension(5,50));
			}
			
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
		columnModel.getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
			@Override
			 public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
					 int row, int column) {
				JCheckBox checkBox = (JCheckBox) super.getTableCellEditorComponent(table, value, 
														isSelected, row, column);
				checkBox.setHorizontalAlignment(SwingConstants.CENTER);
				return checkBox;
			}
		});
	}
	
	public void getData() {
		List<Magazin> dataMagazine = magazinRepository.findAll();
		DefaultTableModel model = (DefaultTableModel)tabelFirme.getModel();
		for(Magazin magazinTemp : dataMagazine) {
			Object[] data = new Object[model.getColumnCount()];
			data[0] = magazinTemp.getCui();
			data[1] = magazinTemp.getNume();
			data[2] = magazinTemp.isTva();
			model.addRow(data);
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
	}
	
	public void addRow() {
		DefaultTableModel model = (DefaultTableModel) tabelFirme.getModel();
		Object[] newRow = new Object[model.getColumnCount()];
		model.addRow(newRow);
		ListSelectionModel selectionModel = tabelFirme.getSelectionModel();
		selectionModel.setSelectionInterval(model.getRowCount()-1, model.getRowCount()-1);
		tabelFirme.setColumnSelectionInterval(0, 0);
		toggleRowEditing();
	}
	
	public void toggleRowEditing() {
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
	        	 adminActionPanelActions.modificaAction();
	         } else {
	        	 
	        	 if (tabelFirme.isEditing()) {
	        		 TableCellEditor cellEditor = tabelFirme.getCellEditor();
	    	         if (cellEditor != null) {
	    	        	 cellEditor.stopCellEditing();
	    	         }
	        	 }
	        	 
	        	 if (checkAllCellsFromRow(editingRow)) {
	        		 model.enableSelectedRowForEditing(selectedRow, newEditability);
	        		 addToDataBase(editingRow); // <----------- TO IMPELEMNT
	        		 adminActionPanelActions.modificaAction();
	        		 editingRow = -1;
	        	 } else {
	        		 JOptionPane.showMessageDialog(this, "Inserati datele in campurile goale !");
	        	 } 
	        	 
	         }
		} else {
            JOptionPane.showMessageDialog(this, "Selectati o linie pentru a o modifica.");
        }
	}
	
	public void deleteRow() {
		int selectedRow = tabelFirme.getSelectedRow();
		if (selectedRow != -1) {
			EditTableModel model = (EditTableModel) tabelFirme.getModel();
			
			if (tabelFirme.isEditing()) {
				TableCellEditor cellEditor = tabelFirme.getCellEditor();
	            if (cellEditor != null) {
	                cellEditor.stopCellEditing();
	            }
			}
			
			Magazin magazinToDelete = magazinRepository.findById(tabelFirme.getValueAt(selectedRow, 0));
			if (magazinToDelete != null) {
				magazinRepository.delete(magazinToDelete.getCui());
			}
			
			if (editingRow == selectedRow){
            	editingRow = -1;
            	adminActionPanelActions.modificaAction();
            	model.enableSelectedRowForEditing(selectedRow, false);
            }
            model.removeRow(selectedRow);
			
		} else {
			JOptionPane.showMessageDialog(this, "Selectati o linie pentru a o sterge.");
		}
	}
	
	private void addToDataBase(int rowToAdd) {
		// TODO Auto-generated method stub
		
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

	public void setAdminAction(AdminActions adminActionPanelActions) {
		this.adminActionPanelActions = adminActionPanelActions;
	}
	
	@Override
	public void adaugaAction() {
		addRow();
	}

	@Override
	public void modificaAction() {
		toggleRowEditing();
	}

	@Override
	public void stergeAction() {
		deleteRow();
	}
}
