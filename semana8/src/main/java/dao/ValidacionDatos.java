package dao;

/**
 * Reglas comunes de texto para create y update.
 */
final class ValidacionDatos {

    private ValidacionDatos() {
    }

    /** El valor no puede ir vacío ni ser solo números. */
    static void exigirTextoConLetras(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El " + campo + " es obligatorio.");
        }
        String texto = valor.trim();
        if (texto.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("El " + campo + " no puede ser solo números.");
        }
        if (texto.chars().noneMatch(Character::isLetter)) {
            throw new IllegalArgumentException("El " + campo + " debe incluir letras.");
        }
    }
}
