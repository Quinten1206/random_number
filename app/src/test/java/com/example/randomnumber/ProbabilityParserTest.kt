package com.example.randomnumber

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProbabilityParserTest {

    // ========== 小数格式 ==========

    @Test
    fun `parse decimal 0_5 returns success 0_5`() {
        val result = ProbabilityParser.parse("0.5")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.5, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse decimal 0_75 returns success 0_75`() {
        val result = ProbabilityParser.parse("0.75")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.75, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse decimal 1_0 returns success 1_0`() {
        val result = ProbabilityParser.parse("1.0")
        assertTrue(result is ParseResult.Success)
        assertEquals(1.0, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse decimal 0 returns success 0`() {
        val result = ProbabilityParser.parse("0")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.0, (result as ParseResult.Success).value, 1e-12)
    }

    // ========== 分数格式 ==========

    @Test
    fun `parse fraction 1_2 returns success 0_5`() {
        val result = ProbabilityParser.parse("1/2")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.5, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse fraction 3_4 returns success 0_75`() {
        val result = ProbabilityParser.parse("3/4")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.75, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse fraction 1_1 returns success 1_0`() {
        val result = ProbabilityParser.parse("1/1")
        assertTrue(result is ParseResult.Success)
        assertEquals(1.0, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse fraction 0_2 returns success 0`() {
        val result = ProbabilityParser.parse("0/2")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.0, (result as ParseResult.Success).value, 1e-12)
    }

    // ========== 百分比格式 ==========

    @Test
    fun `parse percentage 50_percent returns success 0_5`() {
        val result = ProbabilityParser.parse("50%")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.5, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse percentage 7_5_percent returns success 0_075`() {
        val result = ProbabilityParser.parse("7.5%")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.075, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse percentage 100_percent returns success 1_0`() {
        val result = ProbabilityParser.parse("100%")
        assertTrue(result is ParseResult.Success)
        assertEquals(1.0, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse percentage 0_percent returns success 0`() {
        val result = ProbabilityParser.parse("0%")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.0, (result as ParseResult.Success).value, 1e-12)
    }

    // ========== 空格处理 ==========

    @Test
    fun `parse decimal with leading trailing spaces trims correctly`() {
        val result = ProbabilityParser.parse("  0.5  ")
        assertTrue(result is ParseResult.Success)
        assertEquals(0.5, (result as ParseResult.Success).value, 1e-12)
    }

    @Test
    fun `parse fraction with spaces around slash parses correctly`() {
        val result = ProbabilityParser.parse("1 / 3")
        assertTrue(result is ParseResult.Success)
        assertEquals(1.0 / 3.0, (result as ParseResult.Success).value, 1e-12)
    }

    // ========== 错误情况 ==========

    @Test
    fun `parse empty input returns error`() {
        val result = ProbabilityParser.parse("")
        assertTrue(result is ParseResult.Error)
    }

    @Test
    fun `parse blank input returns error`() {
        val result = ProbabilityParser.parse("   ")
        assertTrue(result is ParseResult.Error)
    }

    @Test
    fun `parse non-numeric text returns error`() {
        val result = ProbabilityParser.parse("abc")
        assertTrue(result is ParseResult.Error)
    }

    @Test
    fun `parse fraction with zero denominator returns error`() {
        val result = ProbabilityParser.parse("1/0")
        assertTrue(result is ParseResult.Error)
        assertEquals(ErrorType.ZERO_DENOMINATOR, (result as ParseResult.Error).type)
    }

    @Test
    fun `parse invalid percent format returns error`() {
        val result = ProbabilityParser.parse("abc%")
        assertTrue(result is ParseResult.Error)
        assertEquals(ErrorType.INVALID_PERCENT, (result as ParseResult.Error).type)
    }
}
