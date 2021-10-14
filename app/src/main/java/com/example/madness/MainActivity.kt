package com.example.madness

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val tracker = BalanceTracker()
    private var update: Double = 0.0
    private var condition: Boolean = false

    override fun onPause() {
        super.onPause()
        val preferences: SharedPreferences = getPreferences(MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putFloat("FoodBalance", tracker.foodBalance.toFloat())
        editor.putFloat("IncomeBalance", tracker.incomeBalance.toFloat())
        editor.putFloat("ExtrasBalance", tracker.extrasBalance.toFloat())
        editor.apply()
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        val preferences: SharedPreferences = getPreferences(MODE_PRIVATE)
        val fb = preferences.getFloat("FoodBalance", tracker.food.toFloat()).toDouble()
        val ib = preferences.getFloat("IncomeBalance", -tracker.income.toFloat()).toDouble()
        val eb = preferences.getFloat("ExtrasBalance", tracker.extras.toFloat()).toDouble()
        setValues(tracker, fb, ib, eb)

        val foodDisplay: TextView = findViewById(R.id.food_display)
        val extrasDisplay: TextView = findViewById(R.id.extras_display)
        val incomeDisplay: TextView = findViewById(R.id.income_display)
        val balanceDisplay: TextView = findViewById(R.id.balance_display)

        foodDisplay.text = "%.${2}f".format(tracker.foodBalance)
        extrasDisplay.text = "%.${2}f".format(tracker.extrasBalance)
        incomeDisplay.text = "%.${2}f".format(tracker.incomeBalance)
        balanceDisplay.text = "%.${2}f".format(tracker.totalBalance)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val food: Button = findViewById(R.id.food)
        val extras: Button = findViewById(R.id.extras)
        val income: Button = findViewById(R.id.income)
        val input: EditText = findViewById(R.id.input)

        val foodDisplay: TextView = findViewById(R.id.food_display)
        val extrasDisplay: TextView = findViewById(R.id.extras_display)
        val incomeDisplay: TextView = findViewById(R.id.income_display)
        val balanceDisplay: TextView = findViewById(R.id.balance_display)

        food.setOnClickListener {
            fetch(input)
            tracker.foodBalance = tracker.updateFood(update)
            tracker.totalBalance = tracker.getTotal()
            foodDisplay.text = "%.${2}f".format(tracker.foodBalance)
            balanceDisplay.text = "%.${2}f".format(tracker.totalBalance)

            if (condition) {
                reset(tracker)
                foodDisplay.text = "%.${2}f".format(tracker.foodBalance)
                extrasDisplay.text = "%.${2}f".format(tracker.extrasBalance)
                incomeDisplay.text = "%.${2}f".format(tracker.incomeBalance)
                balanceDisplay.text = "%.${2}f".format(tracker.totalBalance)
            }
        }
        extras.setOnClickListener {
            fetch(input)
            tracker.extrasBalance = tracker.updateExtras(update)
            tracker.totalBalance = tracker.getTotal()
            extrasDisplay.text = "%.${2}f".format(tracker.extrasBalance)
            balanceDisplay.text = "%.${2}f".format(tracker.totalBalance)

            if (condition) {
                reset(tracker)
                foodDisplay.text = "%.${2}f".format(tracker.foodBalance)
                extrasDisplay.text = "%.${2}f".format(tracker.extrasBalance)
                incomeDisplay.text = "%.${2}f".format(tracker.incomeBalance)
                balanceDisplay.text = "%.${2}f".format(tracker.totalBalance)
            }
        }
        income.setOnClickListener {
            fetch(input)
            tracker.incomeBalance = tracker.updateIncome(update)
            tracker.totalBalance = tracker.getTotal()
            incomeDisplay.text = "%.${2}f".format(tracker.incomeBalance)
            balanceDisplay.text = "%.${2}f".format(tracker.totalBalance)

            if (condition) {
                reset(tracker)
                foodDisplay.text = "%.${2}f".format(tracker.foodBalance)
                extrasDisplay.text = "%.${2}f".format(tracker.extrasBalance)
                incomeDisplay.text = "%.${2}f".format(tracker.incomeBalance)
                balanceDisplay.text = "%.${2}f".format(tracker.totalBalance)
            }
        }
    }
    private fun fetch(input: EditText) {
        try {
            update = input.text.toString().toDouble()
            condition = update.equals(0.0)
        } catch (e: NumberFormatException) {
            update = 0.0
            condition = false
        }
    }
    private fun reset(tracker: BalanceTracker) {
        tracker.foodBalance = tracker.food
        tracker.incomeBalance = -tracker.income
        tracker.extrasBalance = tracker.extras
        tracker.totalBalance = tracker.getTotal()
    }
    private fun setValues(tracker: BalanceTracker, fb: Double, ib: Double, eb: Double) {
        tracker.foodBalance = fb
        tracker.incomeBalance = ib
        tracker.extrasBalance = eb
        tracker.totalBalance = tracker.getTotal()
    }
}

class BalanceTracker {
    private val savings = 420.0
    private val insurance = 105.80
    private val rent = 210.82

    val food = 100.0
    val income = 1000.0
    val extras = income - insurance - rent - savings - food

    var foodBalance = food
    var incomeBalance = -income
    var extrasBalance = extras
    var totalBalance = incomeBalance + extrasBalance + foodBalance

    fun updateFood(update: Double): Double {
        return foodBalance - update
    }
    fun updateIncome(update: Double): Double {
        return incomeBalance + update
    }
    fun updateExtras(update: Double): Double {
        return extrasBalance - update
    }
    fun getTotal(): Double {
        return incomeBalance + extrasBalance + foodBalance
    }
}