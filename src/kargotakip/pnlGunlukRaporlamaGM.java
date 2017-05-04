package kargotakip;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.toedter.calendar.JDateChooser;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kargotakip.op.Kargo;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;



public class pnlGunlukRaporlamaGM extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel dm;
	private JDateChooser dcTarih;
	private String yol="";
	private String pdfIsim="tablo";
	
	
	public pnlGunlukRaporlamaGM() {
		setBackground(new Color(204, 204, 153));
		setBounds(80, 60, 798, 488);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(102, 153, 153));
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 11, 178, 123);
		add(panel);
		panel.setLayout(null);
		
		// Listele buttonu
		JButton btn_aListele = new JButton("Listele");
		btn_aListele.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dm.getDataVector().removeAllElements();
				revalidate();
				
				Date tarih=new java.sql.Date(dcTarih.getDate().getTime());
				ArrayList<Kargo> gL= new Kargo().subelerGunluk(tarih);
				
				if(gL != null){
					for(Kargo k:gL){
						Object[] row = {
								k._gondericiSube.subeAdi,k.kargo_,k.ucret
							};
						dm.addRow(row);
					}
				
				}else{
					JOptionPane.showMessageDialog(null, 
							"Veri bulunamad� !", "Arama sonucu", 
							JOptionPane.INFORMATION_MESSAGE);
				}
				gL=null;
				
				
			}
		});
		btn_aListele.setBackground(new Color(51, 255, 153));
		btn_aListele.setIcon(new ImageIcon("img\\arama.png"));
		btn_aListele.setBounds(10, 70, 158, 31);
		panel.add(btn_aListele);
		
		dcTarih = new JDateChooser();
		dcTarih.setBounds(10, 11, 158, 31);
		panel.add(dcTarih);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(204, 204, 102));
		panel_1.setBounds(198, 11, 592, 466);
		add(panel_1);
		panel_1.setLayout(null);
		
		//tablo
		table = new JTable();
		dm = new DefaultTableModel(0, 0);
		dm.setColumnIdentifiers(new String[] {
				"�ube Ad�", "Kargo Say�s�", "Toplam �cret"
			});
		table.setModel(dm);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 11, 572, 406);
		panel_1.add(scrollPane);
		
		// pdf �ek butonu
		JButton btn_pdfCek = new JButton("PDF'e \u00C7ek");
		btn_pdfCek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				pdfIsim = df.format(dcTarih.getDate().getTime());
				
				JFileChooser fc = new JFileChooser();
		        fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		        int result = fc.showOpenDialog(frmMain.PANEL);//

		        if( result == JFileChooser.APPROVE_OPTION )
		        {
		            File selectedFile = fc.getSelectedFile();
		            yol=selectedFile.getAbsolutePath();
		            
		            print();
		        }
				
			}
		});
		btn_pdfCek.setIcon(new ImageIcon("img\\pdf.png"));
		btn_pdfCek.setBackground(new Color(204, 255, 153));
		btn_pdfCek.setBounds(463, 428, 119, 27);
		panel_1.add(btn_pdfCek);  
		

	}
	
	private void print(){
		  try {
	            Document doc = new Document();
	            PdfWriter.getInstance(doc, new FileOutputStream(yol+"\\"+pdfIsim+".pdf"));
	            doc.open();
	            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
	            
	            //tablo ba�l�klar� ekleme
	            for (int i = 0; i < table.getColumnCount(); i++) {
	                pdfTable.addCell(table.getColumnName(i));
	            }
	            //extracting data from the JTable and inserting it to PdfPTable
	            for (int rows = 0; rows < table.getRowCount(); rows++) {
	                for (int cols = 0; cols < table.getColumnCount(); cols++) {
	                    pdfTable.addCell(table.getModel().getValueAt(rows, cols).toString());

	                }
	            }
	            doc.add(pdfTable);
	            doc.close();
	        }catch (DocumentException ex) {
	            System.out.println(ex);
	        }catch (FileNotFoundException ex) {
	        	System.out.println(ex);
	        }
	}
	
	
	
}
