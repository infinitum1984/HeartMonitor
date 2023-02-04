package com.empty.heartmonitor.core.mapper

abstract class MapperBase<T1 : Any, T2 : Any> : Mapper<T1, T2> {

    fun map(values: List<T1?>): List<T2> {
        return values.mapNotNull { it?.let { it1 -> map(it1) } }
    }
}