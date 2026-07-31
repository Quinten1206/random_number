package com.example.randomnumber

import kotlin.math.abs

/** 错误类型枚举 */
enum class ErrorType {
    EMPTY,
    INVALID_FORMAT,
    ZERO_DENOMINATOR,
    OUT_OF_RANGE,
    INVALID_PERCENT
}

/** 解析结果：成功时包含概率值，失败时包含错误类型和提示信息 */
sealed class ParseResult {
    data class Success(val value: Double) : ParseResult()
    data class Error(val type: ErrorType, val message: String) : ParseResult()
}

/** 概率输入解析器，支持小数、分数、百分比三种格式 */
object ProbabilityParser {

    /**
     * 解析用户输入的概率字符串。
     *
     * 支持格式：
     * - 小数: "0.5", "1.0", "0"
     * - 分数: "1/2", "3/4"
     * - 百分比: "50%", "7.5%"
     *
     * 自动处理前后空格及分数中 "/" 周围的空格。
     */
    fun parse(input: String): ParseResult {
        val trimmed = input.trim()

        // 空输入
        if (trimmed.isEmpty()) {
            return ParseResult.Error(ErrorType.EMPTY, "请输入概率值")
        }

        // 百分比格式: 以 '%' 结尾
        if (trimmed.endsWith("%")) {
            return parsePercentage(trimmed)
        }

        // 分数格式: 包含 '/'
        if (trimmed.contains("/")) {
            return parseFraction(trimmed)
        }

        // 小数格式
        return parseDecimal(trimmed)
    }

    private fun parsePercentage(input: String): ParseResult {
        val numberPart = input.dropLast(1).trim() // 去掉 '%'
        val percentValue = numberPart.toDoubleOrNull()
            ?: return ParseResult.Error(ErrorType.INVALID_PERCENT, "无效的百分比格式")

        val probability = percentValue / 100.0

        if (probability < 0.0 || probability > 1.0) {
            return ParseResult.Error(ErrorType.OUT_OF_RANGE, "概率必须在 0 到 1 之间")
        }

        return ParseResult.Success(probability)
    }

    private fun parseFraction(input: String): ParseResult {
        val parts = input.split("/").map { it.trim() }

        if (parts.size != 2) {
            return ParseResult.Error(ErrorType.INVALID_FORMAT, "格式错误，请使用小数、分数或百分比")
        }

        val numerator = parts[0].toDoubleOrNull()
        val denominator = parts[1].toDoubleOrNull()

        if (numerator == null || denominator == null) {
            return ParseResult.Error(ErrorType.INVALID_FORMAT, "格式错误，请使用小数、分数或百分比")
        }

        // 分母为零检查（使用极小阈值处理浮点）
        if (abs(denominator) < 1e-12) {
            return ParseResult.Error(ErrorType.ZERO_DENOMINATOR, "分母不能为零")
        }

        val probability = numerator / denominator

        if (probability < 0.0 || probability > 1.0) {
            return ParseResult.Error(ErrorType.OUT_OF_RANGE, "概率必须在 0 到 1 之间")
        }

        return ParseResult.Success(probability)
    }

    private fun parseDecimal(input: String): ParseResult {
        val value = input.toDoubleOrNull()
            ?: return ParseResult.Error(ErrorType.INVALID_FORMAT, "格式错误，请使用小数、分数或百分比")

        if (value < 0.0 || value > 1.0) {
            return ParseResult.Error(ErrorType.OUT_OF_RANGE, "概率必须在 0 到 1 之间")
        }

        return ParseResult.Success(value)
    }
}
