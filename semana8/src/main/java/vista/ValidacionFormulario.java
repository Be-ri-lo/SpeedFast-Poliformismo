package vista;

/**
 * Validación de formularios antes de llamar al CRUD.
 * Si falla, se muestra JOptionPane y no se toca MySQL.
 */
final class ValidacionFormulario {

    private ValidacionFormulario() {
    }

    static String nombreRepartidor(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        String texto = nombre.trim();
        if (texto.length() < 3 || texto.length() > 100) {
            throw new IllegalArgumentException("El nombre debe tener entre 3 y 100 caracteres.");
        }
        if (texto.chars().noneMatch(Character::isLetter)) {
            throw new IllegalArgumentException("El nombre debe incluir letras.");
        }
        return texto;
    }

    static String direccion(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        String texto = direccion.trim();
        if (texto.length() > 100) {
            throw new IllegalArgumentException("La dirección no puede superar 100 caracteres.");
        }
        if (texto.chars().noneMatch(Character::isLetter)) {
            throw new IllegalArgumentException("La dirección debe incluir letras.");
        }
        return texto;
    }

    static double distancia(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("La distancia es obligatoria.");
        }
        double km = Double.parseDouble(texto.trim().replace(',', '.'));
        if (km < 0.1 || km > 100) {
            throw new IllegalArgumentException("La distancia debe estar entre 0.1 km y 100 km.");
        }
        return km;
    }

    static double peso(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar el peso de la encomienda.");
        }
        double valor = Double.parseDouble(texto.trim().replace(',', '.'));
        if (valor <= 0 || valor > 100) {
            throw new IllegalArgumentException("El peso debe estar entre 0.1 y 100 kg.");
        }
        return valor;
    }
}
