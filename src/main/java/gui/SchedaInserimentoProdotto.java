package gui;

import impl.controller.BancoAlimentareController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SchedaInserimentoProdotto
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedaInserimentoProdotto.class);

    private JFrame mainWindow;
    private JTextField nomeField;
    private JTextField quantitaField;

    private BancoAlimentareController controller;

    /**
     * Launch the application.
     */
    public static void startNewWindow(BancoAlimentareController controller)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    SchedaInserimentoProdotto window = new SchedaInserimentoProdotto(controller);
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
    public SchedaInserimentoProdotto(BancoAlimentareController controller)
    {
        initialize(controller);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(BancoAlimentareController controller)
    {
        this.controller = controller;

        mainWindow = new JFrame();
        mainWindow.setResizable(false);
        mainWindow.setTitle("Inserire prodotto nel magazzino");
        mainWindow.setBounds(100, 100, 400, 200);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.getContentPane().setLayout(null);

        JLabel intestazione = new JLabel("Inserire i dati del prodotto che si intende aggiungere al magazzino");
        intestazione.setFont(new Font("Tahoma", Font.PLAIN, 11));
        intestazione.setBounds(38, 11, 320, 14);
        mainWindow.getContentPane().add(intestazione);

        nomeField = new JTextField();
        nomeField.setBounds(129, 36, 229, 20);
        mainWindow.getContentPane().add(nomeField);
        nomeField.setColumns(10);

        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        nomeLabel.setBounds(38, 39, 46, 14);
        mainWindow.getContentPane().add(nomeLabel);

        quantitaField = new JTextField();
        quantitaField.setBounds(129, 67, 46, 20);
        mainWindow.getContentPane().add(quantitaField);
        quantitaField.setColumns(5);

        JLabel quantitaLabel = new JLabel("Quantità:");
        quantitaLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        quantitaLabel.setBounds(38, 70, 46, 14);
        mainWindow.getContentPane().add(quantitaLabel);

        JButton confermaBtn = new JButton("Conferma");
        confermaBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(aggiungiProdottoAlMagazzino())
                {
                    //TODO verificare l'effettiva aggiunta del prodotto nel controller e l'aggiornamento della tabella
                    JOptionPane.showMessageDialog(new JFrame(), "Prodotto aggiunto al magazzino", "Conferma operazione", JOptionPane.INFORMATION_MESSAGE);
                    LOGGER.info("Prodotto aggiunto al magazzino");
                    salvaSuFile();
                    mainWindow.dispose();
                } else
                {
                    JOptionPane.showMessageDialog(new JFrame(), "Prodotto gi\u00e0 presente nel magazzino", "Avviso", JOptionPane.WARNING_MESSAGE);
                    LOGGER.info("Prodotto aggiunto al magazzino");
                }
                this.notifyAll();
            }
        });

        confermaBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        confermaBtn.setBounds(86, 137, 89, 23);
        mainWindow.getContentPane().add(confermaBtn);

        JButton annullaBtn = new JButton("Annulla");
        annullaBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                mainWindow.dispose();
            }
        });
        annullaBtn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        annullaBtn.setBounds(216, 137, 89, 23);
        mainWindow.getContentPane().add(annullaBtn);
    }

    private boolean aggiungiProdottoAlMagazzino()
    {
        if(nomeField.getText() == null || nomeField.getText().equals(""))
        {
            String errorMessage = "Nessun nome specificato per il prodotto";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
            LOGGER.warn("Nessun nome specificato per il prodotto da aggiungere in magazzino");
        }

        if(quantitaField.getText() == null || quantitaField.getText().equals(""))
        {
            String errorMessage = "Nessuna quantit\u00e0 specificata per il prodotto";
            JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Errore", JOptionPane.ERROR_MESSAGE);
            LOGGER.warn("Nessuna quantit\u00e0 specificata per il prodotto da aggiungere in magazzino");
        }

        String nome = nomeField.getText();
        int quantita = Integer.parseInt(quantitaField.getText());

        return controller.deposita(nome, quantita);
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
