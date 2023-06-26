package com.example.pipboyv1.input

import kotlin.Exception


class MockPotInputContainer : IPotInputContainer {
    
    private val listeners: MutableSet<PotInputListener> = mutableSetOf()
    private var mockPotValues: FloatArray = FloatArray(3) { 0F }

    override fun addListener(listener: PotInputListener) {
        listeners += listener
    }

    override fun removeListener(listener: PotInputListener) {
        listeners -= listener
    }
    
    // TODO implement your mocks for calling the PotInputListener functions
    fun setPotValue(potIndex: Int, percentageValue: Float) {
        // only allow values 0, 1, 2
        if (potIndex < 0 || potIndex > 2) {
            throw Exception("Invalid potentiometer index.")
        }
        // only allow values between 0 and 100
        if (percentageValue < 0F || percentageValue > 100F) {
            throw Exception("Potentiometer value to be set must be between 0 and 100.")
        }

        mockPotValues[potIndex] = percentageValue
        for (listener in listeners) {
            listener.onInputChange(potIndex, percentageValue)
        }
    }

    // Left means decreasing the value and right means increasing the value
    fun moveLeft(potIndex: Int, decrementValue: Float) {
        if (potIndex < 0 || potIndex > 2) {
            throw Exception("Invalid potentiometer index.")
        } else if (decrementValue < 0F) {
            throw Exception("Potentiometer value to decrement must not be negative.")
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
        if (potIndex < 0 || potIndex > 2) {
            throw Exception("Invalid potentiometer index.")
        } else if (incrementValue < 0F) {
            throw Exception("Potentiometer value to increment must not be negative.")
        }
        if (mockPotValues[potIndex] + incrementValue > 100F) {
            // could show a debug pop-up window here
            mockPotValues[potIndex] = 100F
        }

        for (listener in listeners) {
            listener.onMoveRight(potIndex, mockPotValues[potIndex])
        }
    }
}