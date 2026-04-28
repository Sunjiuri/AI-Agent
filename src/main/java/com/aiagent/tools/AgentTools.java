package com.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tool functions that the AI agent can invoke.
 * Methods annotated with {@link Tool} are discovered by Spring AI and offered
 * to the language model as callable tools.
 */
@Component
public class AgentTools {

    /** Input record for the weather tool. */
    public record WeatherRequest(String city) {}

    /** Output record for the weather tool. */
    public record WeatherResponse(String city, String condition, int temperatureCelsius) {}

    /** Input record for the calculator tool. */
    public record CalculatorRequest(double a, String operator, double b) {}

    /** Output record for the calculator tool. */
    public record CalculatorResponse(double result) {}

    /** Returns the current date and time. */
    @Tool(description = "Get the current date and time")
    public String currentDateTime() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Mock weather service – returns a fixed sunny forecast for demonstration.
     *
     * @param city the city to get weather for
     */
    @Tool(description = "Get the current weather for a given city")
    public WeatherResponse getWeather(String city) {
        return new WeatherResponse(city, "Sunny", 22);
    }

    /**
     * Simple four-operation calculator.
     * Supports operators: +, -, *, /
     *
     * @param a        first operand
     * @param operator arithmetic operator (+, -, *, /)
     * @param b        second operand
     */
    @Tool(description = "Perform a basic arithmetic calculation (operators: +, -, *, /)")
    public CalculatorResponse calculate(double a, String operator, double b) {
        double result = switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
        return new CalculatorResponse(result);
    }
}
