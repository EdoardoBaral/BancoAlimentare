package gui;

import impl.controller.BancoAlimentareController;
import om.EntityMagazzino;
import om.EntityRegistro;
import om.TipoTransazione;
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
import java.util.ArrayList;
import java.util.List;

public class SchedaInserimentoTransazione
{

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedaInserimentoTransazione.class);

    private JFrame mainWindow;

    private BancoAlimentareController controller;
    private JTextField quantitaField;
    private JTextField destinatarioField;
    private JComboBox<String> selezioneProdotti;
    private JComboBox<String> selezioneTipoTransazione;

    /**
     * Launch the application.
     */
    public static void startNewWindow(BancoAlimentareController controller, JTable tabellaRegistro, JTable tabellaMagazzino)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    SchedaInserimentoTransazione window = new SchedaInserimentoTransazione(controller, tabellaRegistro, tabellaMagazzino);
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
    public SchedaInserimentoTransazione(BancoAlimentareController controller, JTable tabellaRegistro, JTable tabellaMagazzino)
    {
        initialize(controller, tabellaRegistro, tabellaMagazzino);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(BancoAlimentareController controller, JTable tabellaRegistro, JTable tabellaMagazzino)
    {
        this.controller = controller;

        mainWindow = new JFrame();
        mainWindow.setResizable(false);
        mainWindow.setTitle("Inserire prodotto nel magazzino");
        mainWindow.setBounds(100, 100, 400, 290);
        mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow.getContentPane().setLayout(null);

        JLabel intestazione = new JLabel("Inserire i dati della transazione da registrare");
        intestazione.setBounds(86, 11, 212, 14);
        intestazione.setFont(new Font("Tahoma", Font.PLAIN, 11));
        mainWindow.getContentPane().add(intestazione);

        List<String> nomiProdotti = controller.getNomiProdotti();
        nomiProdotti.add(0, "-- Selezionare nome prodotto --");
        selezioneProdotti = new JComboBox<>();
        selezioneProdotti.setFont(new Font("Tahoma", Font.PLAIN, 11));
        for(String p : nomiProdotti)
            selezioneProdotti.addItem(p);
        selezioneProdotti.setSelectedIndex(0);
        selezioneProdotti.setBounds(150, 36, 200, 20);
        mainWindow.getContentPane().add(selezioneProdotti);

        JLabel nomeProdottoLabel = new JLabel("Nome prodotto:");
        nomeProdottoLabel.setBounds(45, 39, 76, 14);
        nomeProdottoLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        mainWindow.getContentPane().add(nomeProdottoLabel);

        quantitaField = new JTextField();
        quantitaField.setBounds(150, 67, 45, 20);
        mainWindow.getContentPane().add(quantitaField);
        quantitaField.setColumns(5);

        JLabel quantitaLabel = new JLabel("Quantità:");
        quantitaLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        quantitaLabel.setBounds(45, 70, 46, 14);
        mainWindow.getContentPane().add(quantitaLabel);

        destinatarioField = new JTextField();
        destinatarioField.setBounds(149, 98, 200, 20);
        mainWindow.getContentPane().add(destinatarioField);
        destinatarioField.setColumns(100);

        JLabel destinatarioLabel = new JLabel("Destinatario:");
        destinatarioLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        destinatarioLabel.setBounds(45, 101, 62, 14);
        mainWindow.getContentPane().add(destinatarioLabel);

        JLabel notaLabel = new JLabel("(Indicare il destinatario solo in caso di transazione in uscita)");
        notaLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
        notaLabel.setBounds(86, 129, 264, 14);
        mainWindow.getContentPane().add(notaLabel);

        List<String> tipiTransazione = new ArrayList<>();
        tipiTransazione.add(TipoTransazione.USCITA.toString());
        tipiTransazione.add(TipoTransazione.INGRESSO.toString());
        selezioneTipoTransazione = new JComboBox<>();
        selezioneTipoTransazione.setFont(new Font("Tahoma", Font.PLAIN, 11));
        for(String t : tipiTransazione)
            selezioneTipoTransazione.addItem(t);
        selezioneTipoTransazione.setSelectedIndex(0);
        selezioneTipoTransazione.setBounds(150, 154, 100, 20);
        mainWindow.getContentPane().add(selezioneTipoTransazione);

        JLabel transazioneLabel = new JLabel("Tipo:");
        transazioneLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        transazioneLabel.setBounds(45, 157, 76, 14);
        mainWindow.getContentPane().add(transazioneLabel);

        JButton confermaBtn = new JButton("Conferma");
        confermaBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(aggiungiTransazioneAlRegistro())
                {
                    JOptionPane.showMessageDialog(new JFrame(), "Transazione aggiunta al registro", "Conferma operazione", JOptionPane.INFORMATION_MESSAGE);
                    LOGGER.info("Transazione registrata correttamente");
                    popolaTabellaRegistro(tabellaRegistro);
                    popolaTabellaMagazzino(tabellaMagazzino);
                    salvaSuFile();
                    mainWindow.dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(new JFrame(), "Transazione gi\u00e0 presente nel registro", "Avviso", JOptionPane.WARNING_MESSAGE);
                    LOGGER.warn("Transazione gi\\u00e0 presente nel registro");
                }
            }
        });
        confermaBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        confermaBtn.setBounds(85, 225, 89, 23);
        mainWindow.getContentPane().add(confermaBtn);

        JButton annullaBtn = new JButton("Annulla");
        annullaBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                mainWindow.dispose();
            }
        });
        annullaBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        annullaBtn.setBounds(215, 225, 89, 23);
        mainWindow.getContentPane().add(annullaBtn);
    }

    private boolean aggiungiTransazioneAlRegistro()
    {
        if(((String) selezioneProdotti.getSelectedItem()).equals("-- Selezionare nome prodotto --"))
        {
            String errorMessage = "Nessun nome specificato per il prodotto";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
            LOGGER.warn("Nessun nome specificato per il prodotto da indicare nella transazione");
        }

        if(quantitaField.getText() == null || quantitaField.getText().equals(""))
        {
            String errorMessage = "Nessuna quantit\u00e0 specificata per il prodotto";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
            LOGGER.warn("Nessuna quantit\u00e0 specificata per il prodotto da indicare nella transazione");
        }

        String nomeProdotto = ((String) selezioneProdotti.getSelectedItem());
        int quantita = Integer.parseInt(quantitaField.getText());
        String destinatario = destinatarioField.getText();
        String tipoTransazione = ((String) selezioneTipoTransazione.getSelectedItem());

        boolean result = false;

        if(tipoTransazione.equals("INGRESSO"))
        {
            result = controller.deposita(nomeProdotto, quantita);
        }
        else
        {
            if(destinatario == null || destinatario.equals(""))
            {
                String errorMessage = "Nessun destinatario specificato per la transazione in uscita";
                JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
                LOGGER.warn("Nessun destinatario specificato per la transazione in uscita");
            }
            else
            {
                result = controller.preleva(nomeProdotto, quantita, destinatario);
            }
        }

        return result;
    }

    private void salvaSuFile()
    {
        try
        {
            controller.caricaSuFile();
        }
        catch(IOException e)
        {
            String errorMessage = "Errore nel salvataggio dello stato del controller sul file CSV";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("Errore nel salvataggio dello stato del controller sul file CSV - " + e.getMessage());
        }
    }

    private void popolaTabellaRegistro(JTable tabellaRegistro)
    {
        final String[] COLONNE_REGISTRO = {"ID", "PRODOTTO", "QUANTITA'", "NOME DESTINATARIO", "DATA TRANSAZIONE", "TIPO"};
        final String[][] RIGHE_REGISTRO = {};
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
            valori[3] = t.getDestinatario();
            valori[4] = dtf.print(t.getDataTransazione());
            valori[5] = t.getTipoTransazione().toString();

            model.addRow(valori);
        }
    }

    private void popolaTabellaMagazzino(JTable tabellaMagazzino)
    {
        final String[] COLONNE_MAGAZZINO = {"PRODOTTO", "GIACENZA"};
        final String[][] RIGHE_MAGAZZINO = {};
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
}
