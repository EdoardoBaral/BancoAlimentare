package gui;

import impl.controller.BancoAlimentareController;
import om.TipoTransazione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
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
    public static void startNewWindow(BancoAlimentareController controller, ICallbackReceiver callbackReceiver)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    SchedaInserimentoTransazione window = new SchedaInserimentoTransazione(controller, callbackReceiver);
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
    public SchedaInserimentoTransazione(BancoAlimentareController controller, ICallbackReceiver callbackReceiver)
    {
        initialize(controller, callbackReceiver);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(BancoAlimentareController controller, ICallbackReceiver callbackReceiver)
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
                    salvaSuFile();
                    mainWindow.dispose();
                    callbackReceiver.callback(SchedaInserimentoTransazione.this);
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
            return false;
        }

        if(quantitaField.getText() == null || quantitaField.getText().equals(""))
        {
            String errorMessage = "Nessuna quantit\u00e0 specificata per il prodotto";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
            LOGGER.warn("Nessuna quantit\u00e0 specificata per il prodotto da indicare nella transazione");
            return false;
        }

        String nomeProdotto = ((String) selezioneProdotti.getSelectedItem());
        
        int quantita;
        try
        {
            quantita = Integer.parseInt(quantitaField.getText());
        }
        catch(NumberFormatException nfe)
        {   
            String errorMessage = "Il valore della quantit\u00e0 specificata per il prodotto deve essere un numero intero";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
            LOGGER.warn("Il valore specificato per la quantit\u00e0 del prodotto non e' un numero intero");  
            return false;
        }
        
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
                return false;
            }
            else
            {
                result = controller.preleva(nomeProdotto, quantita, destinatario);
            }
        }

        if(!result){
            JOptionPane.showMessageDialog(new JFrame(), "Transazione gi\u00e0 presente nel registro", "Avviso", JOptionPane.WARNING_MESSAGE);
            LOGGER.warn("Transazione gi\\u00e0 presente nel registro");
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
}
