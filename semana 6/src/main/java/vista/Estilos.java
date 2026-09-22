package vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;

/**
 * Colores y fuentes de las ventanas.
 */
final class Estilos {

    static final Color TEXTO = new Color(30, 30, 30);

    private Estilos() {
    }

    static JLabel titulo(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setForeground(TEXTO);
        label.setFont(new Font("SansSerif", Font.BOLD, 26));
        return label;
    }

    static JLabel subtitulo(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setForeground(TEXTO);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return label;
    }

    static JButton boton(String texto) {
        JButton boton = new JButton(texto);
        boton.setForeground(TEXTO);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        boton.setOpaque(true);
        return boton;
    }

    static Border padding() {
        return BorderFactory.createEmptyBorder(25, 25, 25, 25);
    }
}
