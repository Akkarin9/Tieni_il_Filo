package com.tieniilfilo.app.ui.screens.yarn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tieniilfilo.app.data.local.entity.YarnComposition
import com.tieniilfilo.app.data.local.entity.YarnEntity
import com.tieniilfilo.app.data.local.entity.YarnSource
import com.tieniilfilo.app.data.local.entity.YarnStatus
import com.tieniilfilo.app.data.repository.YarnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class YarnFilter(
    val searchQuery: String = "",
    val status: YarnStatus? = null,
    val composition: YarnComposition? = null,
) {
    val isActive: Boolean get() = searchQuery.isNotEmpty() || status != null || composition != null
}

@HiltViewModel
class YarnViewModel @Inject constructor(
    private val repository: YarnRepository,
) : ViewModel() {

    private val _filter = MutableStateFlow(YarnFilter())
    val filter: StateFlow<YarnFilter> = _filter

    private val allYarns = repository.getAllYarns()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val filteredYarns: StateFlow<List<YarnEntity>> = combine(allYarns, _filter) { yarns, filter ->
        yarns.filter { yarn ->
            val matchesSearch = filter.searchQuery.isEmpty() ||
                    yarn.name.contains(filter.searchQuery, ignoreCase = true) ||
                    yarn.brand.contains(filter.searchQuery, ignoreCase = true) ||
                    yarn.colorName.contains(filter.searchQuery, ignoreCase = true)

            val matchesStatus = filter.status == null || yarn.status == filter.status
            val matchesComposition = filter.composition == null || yarn.composition == filter.composition

            matchesSearch && matchesStatus && matchesComposition
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun observeYarn(id: Long) = repository.getYarnByIdFlow(id)

    fun updateSearchQuery(query: String) {
        _filter.value = _filter.value.copy(searchQuery = query)
    }

    fun setStatusFilter(status: YarnStatus?) {
        _filter.value = _filter.value.copy(status = if (_filter.value.status == status) null else status)
    }

    fun setCompositionFilter(composition: YarnComposition?) {
        _filter.value = _filter.value.copy(composition = if (_filter.value.composition == composition) null else composition)
    }

    fun clearFilters() {
        _filter.value = YarnFilter()
    }

    fun addYarn(
        name: String,
        brand: String,
        colorName: String,
        colorHex: Int,
        colorHexes: String?,
        composition: YarnComposition,
        customComposition: String?,
        quantityBallsTotal: Double,
        quantityGramsTotal: Double,
        quantityMetersTotal: Double,
        yarnSource: YarnSource,
        storeName: String,
        storeLink: String,
        notes: String,
        photoUri: String?,
    ) {
        viewModelScope.launch {
            repository.insert(
                YarnEntity(
                    name = name,
                    brand = brand,
                    colorName = colorName,
                    colorHex = colorHex,
                    colorHexes = colorHexes,
                    composition = composition,
                    customComposition = customComposition,
                    quantityBallsTotal = quantityBallsTotal,
                    quantityGramsTotal = quantityGramsTotal,
                    quantityMetersTotal = quantityMetersTotal,
                    status = YarnStatus.DISPONIBILE,
                    yarnSource = yarnSource,
                    storeName = storeName,
                    storeLink = storeLink,
                    notes = notes,
                    photoUri = photoUri,
                )
            )
        }
    }

    fun updateYarn(yarn: YarnEntity) {
        viewModelScope.launch {
            val totalQty = yarn.quantityBallsTotal + yarn.quantityGramsTotal + yarn.quantityMetersTotal
            val newStatus = when {
                yarn.quantityUsed >= 100.0 && totalQty > 0 -> YarnStatus.ESAURITO
                yarn.quantityUsed > 0 -> YarnStatus.IN_USO
                else -> YarnStatus.DISPONIBILE
            }
            repository.update(yarn.copy(status = newStatus))
        }
    }

    fun deleteYarn(yarn: YarnEntity) {
        viewModelScope.launch {
            repository.delete(yarn)
        }
    }

    fun updateQuantityUsed(yarn: YarnEntity, usedPercent: Double) {
        val newPercent = usedPercent.coerceIn(0.0, 100.0)
        val totalQty = yarn.quantityBallsTotal + yarn.quantityGramsTotal + yarn.quantityMetersTotal
        val newStatus = when {
            newPercent >= 100.0 && totalQty > 0 -> YarnStatus.ESAURITO
            newPercent > 0 -> YarnStatus.IN_USO
            else -> YarnStatus.DISPONIBILE
        }
        viewModelScope.launch {
            repository.update(yarn.copy(quantityUsed = newPercent, status = newStatus))
        }
    }
}
