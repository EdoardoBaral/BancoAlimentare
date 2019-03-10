package gui;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;

public class BancoAlimentareGUI {

	private static final String[] COLONNE_MAGAZZINO = {"PRODOTTO", "GIACENZA"};
	private static final String[][] RIGHE_MAGAZZINO = {};
	private static final String[] COLONNE_REGISTRO = {"ID", "PRODOTTO", "QUANTITA'", "NOME DESTINATARIO", "DATA TRANSAZIONE", "TIPO"};
	private static final String[][] RIGHE_REGISTRO = {};
	private JFrame mainWindow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BancoAlimentareGUI window = new BancoAlimentareGUI();
					window.mainWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BancoAlimentareGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainWindow = new JFrame();
		mainWindow.setTitle("Banco Alimentare (v 1.0)");
		mainWindow.setBounds(100, 100, 1024, 768);
		mainWindow.setResizable(false);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel topMargin = new JPanel();
		mainWindow.getContentPane().add(topMargin, BorderLayout.NORTH);
		
		JPanel bottomMargin = new JPanel();
		mainWindow.getContentPane().add(bottomMargin, BorderLayout.SOUTH);
		
		JPanel leftMargin = new JPanel();
		mainWindow.getContentPane().add(leftMargin, BorderLayout.WEST);
		
		JPanel rightMargin = new JPanel();
		mainWindow.getContentPane().add(rightMargin, BorderLayout.EAST);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainWindow.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel schedaMagazzino = new JPanel();
		tabbedPane.addTab("Magazzino", null, schedaMagazzino, null);
		schedaMagazzino.setLayout(null);
		
		JScrollPane scrollPaneMagazzino = new JScrollPane();
		scrollPaneMagazzino.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneMagazzino.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneMagazzino.setBounds(10, 11, 750, 670);
		scrollPaneMagazzino.setLayout(new ScrollPaneLayout());
		schedaMagazzino.add(scrollPaneMagazzino);
		
		JTable tabellaMagazzino = new JTable(RIGHE_MAGAZZINO, COLONNE_MAGAZZINO);
//		tabellaMagazzino.setModel(new DefaultTableModel(
//			new Object[][] {
//				{null, null},
//				{null, null},
//				{null, null},
//				{null, null},
//			},
//			new String[] {
//				"PRODOTTO", "GIACENZA"
//			}
//		));
		scrollPaneMagazzino.setViewportView(tabellaMagazzino);
		
		JPanel pulsantiMagazzinoPanel = new JPanel();
		pulsantiMagazzinoPanel.setBounds(770, 11, 203, 670);
		schedaMagazzino.add(pulsantiMagazzinoPanel);
		pulsantiMagazzinoPanel.setLayout(null);
		
		JButton nuovoProdottoBtn = new JButton("Aggiungi nuovo prodotto");
		nuovoProdottoBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
		nuovoProdottoBtn.setBounds(10, 50, 183, 23);
		pulsantiMagazzinoPanel.add(nuovoProdottoBtn);
		
		JButton salvaMagazzinoBtn = new JButton("Salva su file CSV");
		salvaMagazzinoBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
		salvaMagazzinoBtn.setBounds(10, 100, 183, 23);
		pulsantiMagazzinoPanel.add(salvaMagazzinoBtn);
		
		JPanel schedaRegistro = new JPanel();
		tabbedPane.addTab("Registro", null, schedaRegistro, null);
		schedaRegistro.setLayout(null);
		
		JScrollPane scrollPaneRegistro = new JScrollPane();
		scrollPaneRegistro.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneRegistro.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneRegistro.setBounds(10, 11, 750, 670);
		scrollPaneRegistro.setLayout(new ScrollPaneLayout());
		schedaRegistro.add(scrollPaneRegistro);
		
		JTable tabellaRegistro = new JTable(RIGHE_REGISTRO, COLONNE_REGISTRO);
//		tabellaRegistro.setModel(new DefaultTableModel(
//			new Object[][] {
//				{null, null, null, null, null, null},
//				{null, null, null, null, null, null},
//				{null, null, null, null, null, null},
//				{null, null, null, null, null, null},
//			},
//			new String[] {
//				"ID", "PRODOTTO", "QUANTITA'", "NOME DESTINATARIO", "DATA TRANSAZIONE", "TIPO"
//			}
//		));
		scrollPaneRegistro.setViewportView(tabellaRegistro);
		
		JPanel pulsantiRegistroPanel = new JPanel();
		pulsantiRegistroPanel.setBounds(770, 11, 203, 670);
		schedaRegistro.add(pulsantiRegistroPanel);
		pulsantiRegistroPanel.setLayout(null);
		
		JButton nuovaTransazioneBtn = new JButton("Aggiungi nuova transazione");
		nuovaTransazioneBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
		nuovaTransazioneBtn.setBounds(10, 50, 183, 23);
		pulsantiRegistroPanel.add(nuovaTransazioneBtn);
		
		JButton btnSalvaSuFile = new JButton("Salva su file CSV");
		btnSalvaSuFile.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSalvaSuFile.setBounds(10, 100, 183, 23);
		pulsantiRegistroPanel.add(btnSalvaSuFile);
	}
}
