package views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CrearUsrView extends JFrame {
    private JPanel panel1;
    private JPanel panelPrincipal;
    private JTextField usuarioField;
    private JTextField contraseniaField;
    private JButton crearCuentaButton;
    private JComboBox tipoEmpleadoBox;
    private List<ActionListener> listeners = new ArrayList<>(); // Lista para almacenar los listeners


    public CrearUsrView() {
        setContentPane(panelPrincipal);
        setTitle("Crear Usuario");
        setVisible(true);
        setSize(320, 190);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        crearCuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyListeners("crearUsuario");
            }
        });
    }

    public String getUsuarioField() {
        return usuarioField.getText();
    }

    public String getContraseniaField() {
        return contraseniaField.getText();
    }

    public String gettipoEmpleadoBox() {
        return tipoEmpleadoBox.getSelectedItem().toString();
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
