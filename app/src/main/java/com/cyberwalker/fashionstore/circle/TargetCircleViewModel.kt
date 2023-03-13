package com.cyberwalker.fashionstore.circle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TargetCircleViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(getTargetCircleUiState())

    private fun getTargetCircleUiState(): TargetCircleUiState {

        return  TargetCircleUiState()
    }

    val uiState: StateFlow<TargetCircleUiState> = _uiState.asStateFlow()

    private val _dataSet = MutableLiveData<MutableList<CircleOffers>>()
    val dataSet: LiveData<MutableList<CircleOffers>> = _dataSet

    init {
        _dataSet.value = mutableListOf( CircleOffers("Select skin care", "https://target.scene7.com/is/image/Target/prod_offer_386524_d5d450d7-f6a7-3688-94f7-12a34366945f?wid=320&hei=320", "20% off", "Expires Mar 21, 2023", true),
            CircleOffers("Hair car", "https://target.scene7.com/is/image/Target/prod_offer_386526_000fb4a2-9228-344a-b0a0-d0461b194110?wid=320&hei=320", "15% off", "Expires Mar 21, 2023", false),
            CircleOffers("Bedding", "https://target.scene7.com/is/image/Target/prod_offer_386552_d7f26f1d-60f6-399b-8eef-b8d974c16fac?wid=320&hei=320", "17% off", "Expires Mar 21, 2023", true),
            CircleOffers("Sun care", "https://target.scene7.com/is/image/Target/prod_offer_386525_86f42818-aa4f-3dfc-9bf2-19d223d05a87?wid=320&hei=320", "$12.99", "Expires Mar 21, 2023",false),
            CircleOffers("Outdoor furniture", "https://target.scene7.com/is/image/Target/prod_offer_386545_ed252eb2-f7ca-35e7-bea4-88ec607a6c57?wid=320&hei=320", "$11.99", "Expires Mar 21, 2023",true))
    }




    fun updateDataAt(item: CircleOffers) {
        _dataSet.value?.removeIf {
            it.itemName == item.itemName
        }
        _dataSet.value?.add(item.copy(favourite = !item.favourite))
    }


}

data class TargetCircleUiState(
    val txt: String? = null
)