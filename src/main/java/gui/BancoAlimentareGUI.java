package gui;

import impl.controller.morphia.BancoAlimentareMorphiaController;
import interfaces.BancoAlimentareController;
import om.EntityMagazzino;
import om.EntityRegistro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BancoAlimentareGUI implements ICallbackReceiver
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
        controller = new BancoAlimentareMorphiaController();

        mainWindow = new JFrame();
        mainWindow.setTitle("Banco Alimentare (v 2.0) -- [Edoardo Baral]");
        mainWindow.setBounds(100, 100, 1200, 768);
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
        scrollPaneMagazzino.setBounds(10, 11, 900, 670);
        scrollPaneMagazzino.setLayout(new ScrollPaneLayout());
        schedaMagazzino.add(scrollPaneMagazzino);

        tabellaMagazzino = new JTable(RIGHE_MAGAZZINO, COLONNE_MAGAZZINO);
        popolaTabellaMagazzino();
        scrollPaneMagazzino.setViewportView(tabellaMagazzino);

        JPanel pulsantiMagazzinoPanel = new JPanel();
        pulsantiMagazzinoPanel.setBounds(950, 11, 203, 670);
        schedaMagazzino.add(pulsantiMagazzinoPanel);
        pulsantiMagazzinoPanel.setLayout(null);

        JButton nuovoProdottoBtn = new JButton("Aggiungi nuovo prodotto");
        nuovoProdottoBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                SchedaInserimentoProdotto.startNewWindow(controller, BancoAlimentareGUI.this);
            }
        });
        nuovoProdottoBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        nuovoProdottoBtn.setBounds(10, 50, 183, 23);
        pulsantiMagazzinoPanel.add(nuovoProdottoBtn);

        JButton aggiornaMagazzinoBtn = new JButton("Aggiorna tabella");
        aggiornaMagazzinoBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        aggiornaMagazzinoBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                popolaTabellaMagazzino();
                JOptionPane.showMessageDialog(new JFrame(), "Aggiornamento completato", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        aggiornaMagazzinoBtn.setBounds(10, 150, 183, 23);
        pulsantiMagazzinoPanel.add(aggiornaMagazzinoBtn);

        JPanel schedaRegistro = new JPanel();
        tabbedPane.addTab("Registro", null, schedaRegistro, null);
        schedaRegistro.setLayout(null);

        JScrollPane scrollPaneRegistro = new JScrollPane();
        scrollPaneRegistro.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneRegistro.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneRegistro.setBounds(10, 11, 900, 670);
        scrollPaneRegistro.setLayout(new ScrollPaneLayout());
        schedaRegistro.add(scrollPaneRegistro);

        tabellaRegistro = new JTable(RIGHE_REGISTRO, COLONNE_REGISTRO);
        popolaTabellaRegistro();
        scrollPaneRegistro.setViewportView(tabellaRegistro);

        JPanel pulsantiRegistroPanel = new JPanel();
        pulsantiRegistroPanel.setBounds(950, 11, 203, 670);
        schedaRegistro.add(pulsantiRegistroPanel);
        pulsantiRegistroPanel.setLayout(null);

        JButton nuovaTransazioneBtn = new JButton("Aggiungi nuova transazione");
        nuovaTransazioneBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                SchedaInserimentoTransazione.startNewWindow(controller, BancoAlimentareGUI.this);
            }
        });
        nuovaTransazioneBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        nuovaTransazioneBtn.setBounds(10, 50, 183, 23);
        pulsantiRegistroPanel.add(nuovaTransazioneBtn);

        JButton aggiornaRegistroBtn = new JButton("Aggiorna tabella");
        aggiornaRegistroBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                popolaTabellaRegistro();
                JOptionPane.showMessageDialog(new JFrame(), "Aggiornamento completato", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        aggiornaRegistroBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        aggiornaRegistroBtn.setBounds(10, 150, 183, 23);
        pulsantiRegistroPanel.add(aggiornaRegistroBtn);
    }

    private synchronized void popolaTabellaMagazzino()
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

    private synchronized void popolaTabellaRegistro()
    {
        DefaultTableModel model = new DefaultTableModel(RIGHE_REGISTRO, COLONNE_REGISTRO);
        tabellaRegistro.setModel(model);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<EntityRegistro> transazioni = controller.getTransazioni();
        for(EntityRegistro t : transazioni)
        {
            String[] valori = new String[6];
            valori[0] = Long.toString(t.getId());
            valori[1] = t.getProdotto().getNome();
            valori[2] = Integer.toString(t.getQuantita());
            valori[3] = (t.getDestinatario() != null) && (!"null".equals(t.getDestinatario())) ? t.getDestinatario() : "";
            valori[4] = t.getDataTransazione().format(dtf);
            valori[5] = t.getTipoTransazione().toString();

            model.addRow(valori);
        }
    }

    @Override
    public void callback(Object caller) {
        if (caller instanceof SchedaInserimentoProdotto)
            LOGGER.info("callback from SchedaInserimentoProdotto");
        else if (caller instanceof SchedaInserimentoTransazione)
            LOGGER.info("callback from SchedaInserimentoTransazione");
            
        popolaTabellaMagazzino();
        popolaTabellaRegistro();
    }
}
