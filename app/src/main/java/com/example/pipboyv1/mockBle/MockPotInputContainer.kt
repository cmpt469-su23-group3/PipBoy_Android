package com.example.pipboyv1.mockBle

import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.input.PotInputListener
import kotlin.Exception


class MockPotInputContainer : IPotInputContainer {
    private val mockPotValues: FloatArray = FloatArray(3) { 0F }
    private val listeners: MutableSet<PotInputListener> = mutableSetOf()

    override fun addListener(listener: PotInputListener) {
        listeners += listener
    }

    override fun removeListener(listener: PotInputListener) {
        listeners -= listener
    }

    fun getPotValue(potIndex: Int): Float {
        return mockPotValues[potIndex]
    }

    // Left means decreasing the value and right means increasing the value
    fun moveLeft(potIndex: Int, decrementValue: Float) {
        checkPotIndex(potIndex)
        if (decrementValue < 0F) {
            throw Exception("Potentiometer value to increment must not be negative. Given value is: $decrementValue")
        }

        val newPotVal = mockPotValues[potIndex] - decrementValue
        if (newPotVal < 0F) {
            setPotValue(potIndex, 0F)
        } else {
            setPotValue(potIndex, newPotVal)
        }

        for (listener in listeners) {
            listener.onMoveLeft(potIndex, mockPotValues[potIndex])
        }
    }

    fun moveRight(potIndex: Int, incrementValue: Float) {
        checkPotIndex(potIndex)
        if (incrementValue < 0F) {
            throw Exception("Potentiometer value to increment must not be negative. Given value is: $incrementValue")
        }

        val newPotVal = mockPotValues[potIndex] + incrementValue
        if (newPotVal > 100F) {
            setPotValue(potIndex, 100F)
        } else {
            setPotValue(potIndex, newPotVal)
        }

        for (listener in listeners) {
            listener.onMoveRight(potIndex, mockPotValues[potIndex])
        }
    }

    private fun setPotValue(potIndex: Int, percentageValue: Float) {
        checkPotIndex(potIndex)
        if (percentageValue !in 0F..100F) {
            throw Exception("Potentiometer value to be set must be between 0 and 100. Given value: $percentageValue")
        }

        mockPotValues[potIndex] = percentageValue
        for (listener in listeners) {
            listener.onInputChange(potIndex, percentageValue)
        }
    }

    private final fun checkPotIndex(potIndex: Int) {
        if (potIndex !in 0..2) {
            throw Exception("Invalid potentiometer index. Given index is: $potIndex")
        }
    }
}