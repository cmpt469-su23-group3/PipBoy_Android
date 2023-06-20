package com.example.pipboyv1.input


class MockPotInputContainer : IPotInputContainer {
    
    private val listeners: MutableSet<PotInputListener> = mutableSetOf()

    override fun addListener(listener: PotInputListener) {
        listeners += listener
    }

    override fun removeListener(listener: PotInputListener) {
        listeners -= listener
    }
    
    // TODO implement your mocks for calling the PotInputListener functions
}