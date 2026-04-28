package com.aiagent;

import com.aiagent.tools.AgentTools;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the built-in agent tools.
 * These tests run without a Spring context and require no API key.
 */
class AgentToolsTest {

    private final AgentTools tools = new AgentTools();

    // -----------------------------------------------------------------------
    // currentDateTime
    // -----------------------------------------------------------------------

    @Test
    void currentDateTime_returnsNonBlankString() {
        String result = tools.currentDateTime();
        assertThat(result).isNotBlank();
        // format is yyyy-MM-dd HH:mm:ss  →  always 19 characters (HH uses zero-padded 2-digit hour)
        assertThat(result).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }

    // -----------------------------------------------------------------------
    // getWeather
    // -----------------------------------------------------------------------

    @Test
    void getWeather_returnsWeatherForCity() {
        AgentTools.WeatherResponse response = tools.getWeather("Beijing");

        assertThat(response.city()).isEqualTo("Beijing");
        assertThat(response.condition()).isEqualTo("Sunny");
        assertThat(response.temperatureCelsius()).isEqualTo(22);
    }

    // -----------------------------------------------------------------------
    // calculate
    // -----------------------------------------------------------------------

    @Test
    void calculate_addition() {
        AgentTools.CalculatorResponse response = tools.calculate(3, "+", 4);
        assertThat(response.result()).isEqualTo(7.0);
    }

    @Test
    void calculate_subtraction() {
        AgentTools.CalculatorResponse response = tools.calculate(10, "-", 3);
        assertThat(response.result()).isEqualTo(7.0);
    }

    @Test
    void calculate_multiplication() {
        AgentTools.CalculatorResponse response = tools.calculate(3, "*", 4);
        assertThat(response.result()).isEqualTo(12.0);
    }

    @Test
    void calculate_division() {
        AgentTools.CalculatorResponse response = tools.calculate(10, "/", 2);
        assertThat(response.result()).isEqualTo(5.0);
    }

    @Test
    void calculate_divisionByZero_throwsArithmeticException() {
        assertThatThrownBy(() -> tools.calculate(5, "/", 0))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("Division by zero");
    }

    @Test
    void calculate_unknownOperator_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> tools.calculate(5, "%", 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown operator");
    }
}
