package com.uzuu.ptbac2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testcaseIdInput = findViewById<EditText>(R.id.inputTestcaseId)
        val aInput = findViewById<EditText>(R.id.inputA)
        val bInput = findViewById<EditText>(R.id.inputB)
        val cInput = findViewById<EditText>(R.id.inputC)
        val expectedInput = findViewById<EditText>(R.id.inputExpected)
        val calculateButton = findViewById<Button>(R.id.buttonCalculate)
        val testcaseResultView = findViewById<TextView>(R.id.textTestcaseResult)
        val actualResultView = findViewById<TextView>(R.id.textActualResult)
        val statusResultView = findViewById<TextView>(R.id.textStatusResult)

        calculateButton.setOnClickListener {
            val testcaseId = testcaseIdInput.text.toString().trim().ifEmpty { "TC01" }
            val a = aInput.text.toString().toDoubleOrNull()
            val b = bInput.text.toString().toDoubleOrNull()
            val c = cInput.text.toString().toDoubleOrNull()
            val expectedResult = expectedInput.text.toString().trim()

            if (a == null || b == null || c == null) {
                testcaseResultView.text = "Vui lòng nhập hợp lệ a, b, c."
                actualResultView.text = "Kết quả thực tế: -"
                statusResultView.text = "Trạng thái: CHƯA THỂ CHẠY"
                return@setOnClickListener
            }

            val actualResult = solveEquation(a, b, c)
            val isPass = actualResult == expectedResult

            testcaseResultView.text = "Testcase: $testcaseId\nInput: a=${formatDouble(a)}, b=${formatDouble(b)}, c=${formatDouble(c)}\nKết quả mong đợi: $expectedResult"
            actualResultView.text = "Kết quả thực tế: $actualResult"
            statusResultView.text = "Trạng thái: ${if (isPass) "PASS" else "FAIL"}"
        }
    }
}

private fun formatDouble(value: Double): String {
    return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString()
}

private fun solveEquation(a: Double, b: Double, c: Double): String {
    fun normalizeZero(value: Double): Double = if (value == -0.0) 0.0 else value

    return if (a == 0.0) {
        if (b == 0.0) {
            if (c == 0.0) {
                "Phuong trinh co vo so nghiem"
            } else {
                "Phuong trinh vo nghiem"
            }
        } else {
            val x = normalizeZero(-c / b)
            "Phuong trinh co 1 nghiem: x = ${formatDouble(x)}"
        }
    } else {
        val delta = b * b - 4 * a * c
        when {
            delta < 0 -> "Phuong trinh vo nghiem"
            delta == 0.0 -> {
                val x = normalizeZero(-b / (2 * a))
                "Phuong trinh co nghiem kep: x1 = x2 = ${formatDouble(x)}"
            }
            else -> {
                val sqrtDelta = Math.sqrt(delta)
                val x1 = (-b + sqrtDelta) / (2 * a)
                val x2 = (-b - sqrtDelta) / (2 * a)
                "Co 2 nghiem phan biet: x1 = ${formatDouble(x1)}, x2 = ${formatDouble(x2)}"
            }
        }
    }
}