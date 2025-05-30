package poiupv;

import java.util.List;

public class ejercicios {
    private int respuestaCorrecta;
    private String enunciado;
    private List<String> respuestas;

    public ejercicios(int respuestaCorrecta, String enunciado, List<String> respuestas) {
        this.respuestaCorrecta = respuestaCorrecta;
        this.enunciado = enunciado;
        this.respuestas = respuestas;
    }

    // Getters
    public int getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getRespuestas() {
        return respuestas;
    }

    // Método para mostrar el ejercicio formateado
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ENUNCIADO:\n").append(enunciado).append("\n\n");
        sb.append("RESPUESTAS:\n");
        
        for (int i = 0; i < respuestas.size(); i++) {
            sb.append(i + 1).append(") ").append(respuestas.get(i)).append("\n");
        }
        
        return sb.toString();
    }

    // Método para verificar si una respuesta es correcta
    public boolean esRespuestaCorrecta(int indiceRespuesta) {
        return indiceRespuesta == respuestaCorrecta;
    }

    // Método para obtener la respuesta correcta como texto
    public String getRespuestaCorrectaTexto() {
        if (respuestaCorrecta >= 0 && respuestaCorrecta < respuestas.size()) {
            return respuestas.get(respuestaCorrecta);
        }
        return "No hay respuesta correcta definida";
    }
}