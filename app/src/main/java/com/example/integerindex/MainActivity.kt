package com.example.integerindex

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import kotlin.math.sqrt

class MainActivity : Activity() {

    private lateinit var editTextNumber: EditText
    private lateinit var radioGroupNumberType: RadioGroup
    private lateinit var radioEven: RadioButton
    private lateinit var radioOdd: RadioButton
    private lateinit var radioPrime: RadioButton
    private lateinit var radioPerfectSquare: RadioButton
    private lateinit var radioPalindrome: RadioButton
    private lateinit var radioPerfect: RadioButton
    private lateinit var listViewNumbers: ListView
    private lateinit var textViewEmpty: TextView

    private var isUpdating = false

    enum class NumberType {
        EVEN, ODD, PRIME, PERFECT_SQUARE, PALINDROME, PERFECT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupListeners()
        updateNumberList()
    }

    private fun initializeViews() {
        editTextNumber = findViewById(R.id.editTextNumber)
        radioGroupNumberType = findViewById(R.id.radioGroupNumberType)
        radioEven = findViewById(R.id.radioEven)
        radioOdd = findViewById(R.id.radioOdd)
        radioPrime = findViewById(R.id.radioPrime)
        radioPerfectSquare = findViewById(R.id.radioPerfectSquare)
        radioPalindrome = findViewById(R.id.radioPalindrome)
        radioPerfect = findViewById(R.id.radioPerfect)
        listViewNumbers = findViewById(R.id.listViewNumbers)
        textViewEmpty = findViewById(R.id.textViewEmpty)
    }

    private fun setupListeners() {
        // TextWatcher for EditText
        editTextNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    updateNumberList()
                }
            }
        })

        // RadioGroup listener
        radioGroupNumberType.setOnCheckedChangeListener { _, _ ->
            if (!isUpdating) {
                updateNumberList()
            }
        }
    }

    private fun updateNumberList() {
        val inputText = editTextNumber.text.toString()
        
        if (inputText.isEmpty()) {
            showEmptyMessage()
            return
        }

        try {
            val n = inputText.toInt()
            
            if (n <= 0) {
                showEmptyMessage()
                return
            }

            val numberType = getSelectedNumberType()
            val numbers = getNumbersByType(n, numberType)

            if (numbers.isEmpty()) {
                showEmptyMessage()
            } else {
                showNumberList(numbers)
            }
        } catch (e: NumberFormatException) {
            showEmptyMessage()
        }
    }

    private fun getSelectedNumberType(): NumberType {
        return when (radioGroupNumberType.checkedRadioButtonId) {
            R.id.radioEven -> NumberType.EVEN
            R.id.radioOdd -> NumberType.ODD
            R.id.radioPrime -> NumberType.PRIME
            R.id.radioPerfectSquare -> NumberType.PERFECT_SQUARE
            R.id.radioPalindrome -> NumberType.PALINDROME
            R.id.radioPerfect -> NumberType.PERFECT
            else -> NumberType.EVEN
        }
    }

    private fun getNumbersByType(n: Int, type: NumberType): List<Int> {
        return when (type) {
            NumberType.EVEN -> getEvenNumbers(n)
            NumberType.ODD -> getOddNumbers(n)
            NumberType.PRIME -> getPrimeNumbers(n)
            NumberType.PERFECT_SQUARE -> getPerfectSquares(n)
            NumberType.PALINDROME -> getPalindromes(n)
            NumberType.PERFECT -> getPerfectNumbers(n)
        }
    }

    // Số chẵn: các số chia hết cho 2
    private fun getEvenNumbers(n: Int): List<Int> {
        val result = mutableListOf<Int>()
        for (i in 2 until n step 2) {
            result.add(i)
        }
        return result
    }

    // Số lẻ: các số không chia hết cho 2
    private fun getOddNumbers(n: Int): List<Int> {
        val result = mutableListOf<Int>()
        for (i in 1 until n step 2) {
            result.add(i)
        }
        return result
    }

    // Số nguyên tố: số chỉ chia hết cho 1 và chính nó
    private fun getPrimeNumbers(n: Int): List<Int> {
        val result = mutableListOf<Int>()
        for (i in 2 until n) {
            if (isPrime(i)) {
                result.add(i)
            }
        }
        return result
    }

    private fun isPrime(num: Int): Boolean {
        if (num < 2) return false
        if (num == 2) return true
        if (num % 2 == 0) return false
        
        val sqrtNum = sqrt(num.toDouble()).toInt()
        for (i in 3..sqrtNum step 2) {
            if (num % i == 0) return false
        }
        return true
    }

    // Số chính phương: số có căn bậc hai là số nguyên
    private fun getPerfectSquares(n: Int): List<Int> {
        val result = mutableListOf<Int>()
        var i = 1
        while (true) {
            val square = i * i
            if (square >= n) break
            result.add(square)
            i++
        }
        return result
    }

    // Số palindrome: số đọc xuôi và ngược đều giống nhau
    private fun getPalindromes(n: Int): List<Int> {
        val result = mutableListOf<Int>()
        for (i in 1 until n) {
            if (isPalindrome(i)) {
                result.add(i)
            }
        }
        return result
    }

    private fun isPalindrome(num: Int): Boolean {
        val str = num.toString()
        return str == str.reversed()
    }

    // Số hoàn hảo: số bằng tổng các ước số thực sự của nó
    // Ví dụ: 6 = 1 + 2 + 3, 28 = 1 + 2 + 4 + 7 + 14
    private fun getPerfectNumbers(n: Int): List<Int> {
        val result = mutableListOf<Int>()
        for (i in 2 until n) {
            if (isPerfectNumber(i)) {
                result.add(i)
            }
        }
        return result
    }

    private fun isPerfectNumber(num: Int): Boolean {
        if (num < 2) return false
        
        var sum = 1 // 1 luôn là ước số
        val sqrtNum = sqrt(num.toDouble()).toInt()
        
        for (i in 2..sqrtNum) {
            if (num % i == 0) {
                sum += i
                if (i != num / i) {
                    sum += num / i
                }
            }
        }
        
        return sum == num
    }

    private fun showNumberList(numbers: List<Int>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            numbers
        )
        listViewNumbers.adapter = adapter
        listViewNumbers.visibility = View.VISIBLE
        textViewEmpty.visibility = View.GONE
    }

    private fun showEmptyMessage() {
        listViewNumbers.visibility = View.GONE
        textViewEmpty.visibility = View.VISIBLE
    }
}
