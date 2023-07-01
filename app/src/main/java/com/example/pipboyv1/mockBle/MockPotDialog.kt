package com.example.pipboyv1.mockBle

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import com.example.pipboyv1.input.MockPotInputContainer
import kotlin.Exception

class MockPotDialog {
    companion object {
        private const val POT_ZERO: Int = 0
        private const val POT_ONE: Int = 1
        private const val POT_TWO: Int = 2

        const val MOCK_POT_ADD: Int = 0
        const val MOCK_POT_SUB: Int = 1
        const val MOCK_POT_CHANGE: Int = 2

        fun displayPotIndexDialog(activityContext: Context, mockPotType: Int,
                                  mockPotContainer: MockPotInputContainer) {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activityContext)
            val items: Array<String> = arrayOf<String>("Potentiometer 1", "Potentiometer 2", "Potentiometer 3")

            var potIndex: Int = 0
            alertDialog.setSingleChoiceItems(items, potIndex, DialogInterface.OnClickListener { _: DialogInterface, choice: Int ->
                when (choice) {
                    0 -> { potIndex = POT_ZERO }
                    1 -> { potIndex = POT_ONE }
                    2 -> { potIndex = POT_TWO }
                }
            })

            alertDialog.setPositiveButton("Next", DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
                Toast.makeText(activityContext.applicationContext, "Chose POT $potIndex with option $mockPotType", Toast.LENGTH_SHORT)
                    .show()
                displayPotValueDialog(activityContext, potIndex, mockPotType, mockPotContainer)
            })
            alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            })

            alertDialog.setCancelable(true)
            alertDialog.show()
        }

        private fun displayPotValueDialog(activityContext: Context, potIndex: Int, mockPotType: Int,
                                          mockPotContainer: MockPotInputContainer) {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activityContext)
            val selectedPotValue: Float =  mockPotContainer.getPotValue(potIndex)
            alertDialog.setTitle("POT $potIndex value: $selectedPotValue")

            val potValueInput: EditText = EditText(activityContext)
            potValueInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            when (mockPotType) {
                MOCK_POT_ADD -> {
                    potValueInput.hint = "Value to Add"
                }
                MOCK_POT_SUB -> {
                    potValueInput.hint = "Value to Subtract"
                }
                MOCK_POT_CHANGE -> {
                    potValueInput.hint = "New Value (Between 0-100)"
                }
                else -> {
                    Toast.makeText(activityContext.applicationContext, "Uh oh. I didn't get what I was supposed to do with this data...",
                        Toast.LENGTH_SHORT).show()
                    return
                }
            }
            alertDialog.setView(potValueInput)

            alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
                try {
                    when (mockPotType) {
                        MOCK_POT_ADD -> {
                            mockPotContainer.moveRight(potIndex, potValueInput.text.toString().toFloat())
                        }
                        MOCK_POT_SUB -> {
                            mockPotContainer.moveLeft(potIndex, potValueInput.text.toString().toFloat())
                        }
                        MOCK_POT_CHANGE -> {
                            mockPotContainer.setPotValue(potIndex, potValueInput.text.toString().toFloat())
                        }
                    }

                    val updatedPotValue: Float =  mockPotContainer.getPotValue(potIndex)
                    Toast.makeText(activityContext.applicationContext, "New POT $potIndex value: $updatedPotValue",
                        Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(activityContext.applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            })
            alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            })

            alertDialog.setCancelable(true)
            alertDialog.show()
        }
    }
}