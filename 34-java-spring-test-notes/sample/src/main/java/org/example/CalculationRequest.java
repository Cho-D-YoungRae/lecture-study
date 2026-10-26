package org.example;

public record CalculationRequest(
        long num1,
        long num2,
        String operator
) {
    /**
     * VO 의 특징 중 하나가 'VO 안의 변수들은 값이 항상 유효하다'
     */
    public static CalculationRequest of(final String[] parts) {
        if (parts.length != 3) {
            throw new BadRequestException();
        }
        if (parts[1].length() != 1 || !"+-*/".contains(parts[1])) {
            throw new InvalidOperatorException();
        }
        return new CalculationRequest(Long.parseLong(parts[0]), Long.parseLong(parts[2]), parts[1]);
    }
}
