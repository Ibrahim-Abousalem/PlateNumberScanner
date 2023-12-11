package com.cafu.textrecognition

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.min

class TextRecognitionViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(ScannedData())
    val viewState: StateFlow<ScannedData> = _viewState

    fun updateScannedData(scannedText: Text) {
        val resultList = scannedText.text.trim().split(" ", "\n")
        Log.e("Ibrahim", "updateScannedData: $resultList")

        extractPlateData(resultList)
    }

    private fun extractPlateData(resultList: List<String>) {
        var emirate = ""
        var plateCode = ""
        var plateNumber = ""
        resultList.forEach { result ->
            val isAlphabets = result.all { it.isLetter() }
            val isDigits = result.count { it.isDigit() } >= 3
            if (result.length <= 5 && isDigits) plateNumber = result
            else if (result.length <= 2 && (isAlphabets || result.all { it.isDigit() }))
                plateCode = result
            else if (isAlphabets) emirate = constructEmirates(result)
        }
        _viewState.update {
            it.copy(
                emirate = emirate,
                plateCode = plateCode,
                plateNumber = plateNumber
            )
        }
    }

    private fun constructEmirates(emirate: String): String {
        val emiratesList = listOf("DUBAI", "SHARJAH", "A.D")
        var min = -1
        var ans = ""
        emiratesList.forEach {
            var countMatching = 0
            for (i in 0 until min(it.length, emirate.length)) {
                if (it[i] == emirate[i]) countMatching++
            }
            if (countMatching > min) {
                min = countMatching
                ans = it
            }
        }
        return ans
    }
}
