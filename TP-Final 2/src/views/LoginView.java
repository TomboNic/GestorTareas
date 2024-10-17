package views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LoginView extends JFrame {
    private JPanel panel1;
    private JPanel panelPrincipal;
    private JTextField usuarioField;
    private JTextField contraseniaField;
    private JButton ingresarButton;
    private JButton registrarseButton;
    private JLabel alertaField;
    private List<ActionListener> listeners = new ArrayList<>(); // Lista para almacenar los listeners


    public LoginView() {
        setContentPane(panelPrincipal);
        setTitle("Login");
        setVisible(true);
        setSize(320, 190);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyListeners("ingresar");
            }
        });

        registrarseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyListeners("registrarse");
            }
        });
    }

    // Getters para obtener los datos de los campos de texto
    public String getUsuarioField() {
        return usuarioField.getText();
    }

    public String getContraseniaField() {
        return contraseniaField.getText();
    }

    public void mostrarAlerta(String mensaje) {
        alertaField.setText(mensaje);
    }

    public void addOpcionListener(ActionListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(String evento) {
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, evento);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(e);
        }
    }
}
