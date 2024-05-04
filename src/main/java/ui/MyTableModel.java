package ui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {
	static Vector<String> col = new Vector<String>();
	static
	{
		col.add("Id");col.add("Name");col.add("Date");col.add("From");col.add("To");col.add("Location");col.add("Type");
	}
	public MyTableModel(Vector<? extends Vector> data) {
		// TODO Auto-generated constructor stub
		super(data,col);
	}
	public MyTableModel()
	{
		super();
	}
	@Override
	public boolean isCellEditable(int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}
}
