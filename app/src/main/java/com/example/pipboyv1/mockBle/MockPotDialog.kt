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
    enum class PotIndex {
        POT_ZERO, POT_ONE, POT_TWO
    }

    enum class PotAction {
        POT_ADD, POT_SUB
    }

    companion object {
        fun displayPotIndexDialog(activityContext: Context, mockPotType: PotAction,
                                  mockPotContainer: MockPotInputContainer) {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activityContext)
            val items: Array<String> = arrayOf("Potentiometer 1", "Potentiometer 2", "Potentiometer 3")

            var chosenPot: PotIndex = PotIndex.POT_ZERO
            alertDialog.setSingleChoiceItems(items, 0, DialogInterface.OnClickListener { _: DialogInterface, choice: Int ->
                when (choice) {
                    0 -> { chosenPot = PotIndex.POT_ZERO }
                    1 -> { chosenPot = PotIndex.POT_ONE }
                    2 -> { chosenPot = PotIndex.POT_TWO }
                }
            })

            alertDialog.setPositiveButton("Next", DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
                Toast.makeText(activityContext.applicationContext, "Chose POT ${chosenPot.ordinal} with option $mockPotType", Toast.LENGTH_SHORT)
                    .show()
                displayPotValueDialog(activityContext, chosenPot, mockPotType, mockPotContainer)
            })
            alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            })

            alertDialog.setCancelable(true)
            alertDialog.show()
        }

        private fun displayPotValueDialog(activityContext: Context, potIndex: PotIndex, mockPotType: PotAction,
                                          mockPotContainer: MockPotInputContainer) {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activityContext)
            val selectedPotValue: Float =  mockPotContainer.getPotValue(potIndex.ordinal)
            alertDialog.setTitle("POT ${potIndex.ordinal} value: $selectedPotValue")

            val potValueInput = EditText(activityContext)
            potValueInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            when (mockPotType) {
                PotAction.POT_ADD -> {
                    potValueInput.hint = "Value to Add (Move Pot to Right)"
                }
                PotAction.POT_SUB -> {
                    potValueInput.hint = "Value to Subtract (Move Pot to Left)"
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
                        PotAction.POT_ADD -> {
                            mockPotContainer.moveRight(potIndex.ordinal, potValueInput.text.toString().toFloat())
                        }
                        PotAction.POT_SUB -> {
                            mockPotContainer.moveLeft(potIndex.ordinal, potValueInput.text.toString().toFloat())
                        }
                    }

                    val updatedPotValue: Float =  mockPotContainer.getPotValue(potIndex.ordinal)
                    Toast.makeText(activityContext.applicationContext, "New POT ${potIndex.ordinal} value: $updatedPotValue",
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