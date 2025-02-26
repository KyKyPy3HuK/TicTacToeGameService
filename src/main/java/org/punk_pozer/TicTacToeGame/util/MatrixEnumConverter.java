package org.punk_pozer.TicTacToeGame.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.punk_pozer.TicTacToeGame.model.CellState;

public class MatrixEnumConverter implements AttributeConverter<CellState[][], String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(CellState[][] cellStates) {
        try {
            return objectMapper.writeValueAsString(cellStates);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при сериализации доски", e);
        }
    }

    @Override
    public CellState[][] convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, CellState[][].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при десериализации состояния доски", e);
        }
    }
}
