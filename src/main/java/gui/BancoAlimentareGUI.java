package gui;

import impl.controller.BancoAlimentareController;
import om.EntityMagazzino;
import om.EntityRegistro;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class BancoAlimentareGUI
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BancoAlimentareGUI.class);
    private static final String[] COLONNE_MAGAZZINO = {"PRODOTTO", "GIACENZA"};
    private static final String[][] RIGHE_MAGAZZINO = {};
    private static final String[] COLONNE_REGISTRO = {"ID", "PRODOTTO", "QUANTITA'", "NOME DESTINATARIO", "DATA TRANSAZIONE", "TIPO"};
    private static final String[][] RIGHE_REGISTRO = {};
    private JFrame mainWindow;
    private JTable tabellaMagazzino;
    private JTable tabellaRegistro;
    private BancoAlimentareController controller;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    BancoAlimentareGUI window = new BancoAlimentareGUI();
                    window.mainWindow.setVisible(true);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public BancoAlimentareGUI()
    {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        try
        {
            controller = new BancoAlimentareController();
            controller.inizializza();
        }
        catch(IOException e)
        {
            String errorMessage = "L'applicazione non può essere avviata a causa di un errore nell'istanziazione del controller.\nConsultare i log per maggiori informazioni.";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore avvio programma", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("Errore durante l'istanziazione del controller all'avvio del programma\n" + e.getMessage());
        }

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

        tabellaMagazzino = new JTable(RIGHE_MAGAZZINO, COLONNE_MAGAZZINO);
        popolaTabellaMagazzino();
        scrollPaneMagazzino.setViewportView(tabellaMagazzino);

        JPanel pulsantiMagazzinoPanel = new JPanel();
        pulsantiMagazzinoPanel.setBounds(770, 11, 203, 670);
        schedaMagazzino.add(pulsantiMagazzinoPanel);
        pulsantiMagazzinoPanel.setLayout(null);

        JButton nuovoProdottoBtn = new JButton("Aggiungi nuovo prodotto");
        nuovoProdottoBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                SchedaInserimentoProdotto.startNewWindow(controller, tabellaMagazzino, tabellaRegistro);
            }
        });
        nuovoProdottoBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        nuovoProdottoBtn.setBounds(10, 50, 183, 23);
        pulsantiMagazzinoPanel.add(nuovoProdottoBtn);

        JButton salvaMagazzinoBtn = new JButton("Salva su file CSV");
        salvaMagazzinoBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		try
        		{
        			controller.caricaSuFile();
        			JOptionPane.showMessageDialog(new JFrame(), "Salvataggio avvenuto correttamente", "Conferma operazione", JOptionPane.INFORMATION_MESSAGE);
        			LOGGER.info("Salvataggio dello stato del controller sul file CSV avvenuto correttamente");
        			LOGGER.info("Stato del controller: "+ controller);
        		}
        		catch (IOException ex)
        		{
        			JOptionPane.showMessageDialog(new JFrame(), "Errore durante il salvataggio dello stato del controller", "Errore", JOptionPane.ERROR_MESSAGE);
        			LOGGER.error("Errore durante il salvataggio dello stato del controller - "+ ex.getMessage());
        		}
        	}
        });
        salvaMagazzinoBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        salvaMagazzinoBtn.setBounds(10, 100, 183, 23);
        pulsantiMagazzinoPanel.add(salvaMagazzinoBtn);
        
        JButton aggiornaTabellaBtn = new JButton("Aggiorna tabella");
        aggiornaTabellaBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        aggiornaTabellaBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) 
        	{
        		popolaTabellaMagazzino();
        		JOptionPane.showMessageDialog(new JFrame(), "Aggiornamento completato", "Info", JOptionPane.INFORMATION_MESSAGE);
        	}
        });
        aggiornaTabellaBtn.setBounds(10, 150, 183, 23);
        pulsantiMagazzinoPanel.add(aggiornaTabellaBtn);

        JPanel schedaRegistro = new JPanel();
        tabbedPane.addTab("Registro", null, schedaRegistro, null);
        schedaRegistro.setLayout(null);

        JScrollPane scrollPaneRegistro = new JScrollPane();
        scrollPaneRegistro.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneRegistro.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneRegistro.setBounds(10, 11, 750, 670);
        scrollPaneRegistro.setLayout(new ScrollPaneLayout());
        schedaRegistro.add(scrollPaneRegistro);

        tabellaRegistro = new JTable(RIGHE_REGISTRO, COLONNE_REGISTRO);
        popolaTabellaRegistro();
        scrollPaneRegistro.setViewportView(tabellaRegistro);

        JPanel pulsantiRegistroPanel = new JPanel();
        pulsantiRegistroPanel.setBounds(770, 11, 203, 670);
        schedaRegistro.add(pulsantiRegistroPanel);
        pulsantiRegistroPanel.setLayout(null);

        JButton nuovaTransazioneBtn = new JButton("Aggiungi nuova transazione");
        nuovaTransazioneBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                SchedaInserimentoTransazione.startNewWindow(controller, tabellaRegistro, tabellaMagazzino);
            }
        });
        nuovaTransazioneBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        nuovaTransazioneBtn.setBounds(10, 50, 183, 23);
        pulsantiRegistroPanel.add(nuovaTransazioneBtn);

        JButton btnSalvaSuFile = new JButton("Salva su file CSV");
        btnSalvaSuFile.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    controller.caricaSuFile();
                }
                catch(IOException ex)
                {
                    String errorMessage = "Errore nel salvataggio dello stato del controller sul file CSV";
                    JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
                    LOGGER.error("Errore nel salvataggio dello stato del controller sul file CSV - " + ex.getMessage());
                }
            }
        });
        btnSalvaSuFile.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnSalvaSuFile.setBounds(10, 100, 183, 23);
        pulsantiRegistroPanel.add(btnSalvaSuFile);
    }

    private void popolaTabellaMagazzino()
    {
        DefaultTableModel model = new DefaultTableModel(RIGHE_MAGAZZINO, COLONNE_MAGAZZINO);
        tabellaMagazzino.setModel(model);

        List<EntityMagazzino> prodotti = controller.getProdotti();
        for(EntityMagazzino p : prodotti)
        {
            String[] valori = new String[2];
            valori[0] = p.getNome();
            valori[1] = Integer.toString(p.getGiacenza());

            model.addRow(valori);
        }
    }

    private void popolaTabellaRegistro()
    {
        DefaultTableModel model = new DefaultTableModel(RIGHE_REGISTRO, COLONNE_REGISTRO);
        tabellaRegistro.setModel(model);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");

        List<EntityRegistro> transazioni = controller.getTransazioni();
        for(EntityRegistro t : transazioni)
        {
            String[] valori = new String[6];
            valori[0] = Long.toString(t.getId());
            valori[1] = t.getProdotto();
            valori[2] = Integer.toString(t.getQuantita());
            valori[3] = (t.getDestinatario() != null) && (!"null".equals(t.getDestinatario())) ? t.getDestinatario() : "";
            valori[4] = dtf.print(t.getDataTransazione());
            valori[5] = t.getTipoTransazione().toString();

            model.addRow(valori);
        }
    }
}
