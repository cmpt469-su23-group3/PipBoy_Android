package com.example.pipboyv1.input

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
    
    // TODO implement your mocks for calling the PotInputListener functions
    fun setPotValue(potIndex: Int, percentageValue: Float) {
        checkPotIndex(potIndex)
        if (percentageValue !in 0F..100F) {
            throw Exception("Potentiometer value to be set must be between 0 and 100. Given value: $percentageValue")
        }

        mockPotValues[potIndex] = percentageValue
        for (listener in listeners) {
            listener.onInputChange(potIndex, percentageValue)
        }
    }

    // Left means decreasing the value and right means increasing the value
    fun moveLeft(potIndex: Int, decrementValue: Float) {
        checkPotIndex(potIndex)
        if (decrementValue < 0F) {
            throw Exception("Potentiometer value to increment must not be negative. Given value is: $decrementValue")
        }
        if (mockPotValues[potIndex] - decrementValue < 0F) {
            // could show a debug pop-up window here
            mockPotValues[potIndex] = 0F
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
        if (mockPotValues[potIndex] + incrementValue > 100F) {
            // could show a debug pop-up window here
            mockPotValues[potIndex] = 100F
        }

        for (listener in listeners) {
            listener.onMoveRight(potIndex, mockPotValues[potIndex])
        }
    }

    private final fun checkPotIndex(potIndex: Int) {
        if (potIndex !in 0..2) {
            throw Exception("Invalid potentiometer index. Given index is: $potIndex")
        }
    }
}