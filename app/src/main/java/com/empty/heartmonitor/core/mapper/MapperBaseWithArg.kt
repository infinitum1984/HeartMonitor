package com.empty.heartmonitor.core.mapper

abstract class MapperBaseWithArg<T1 : Any, T2 : Any, T3 : Any> : MapperWithArg<T1, T2, T3> {

    fun map(values: List<T1?>, arg: T2): List<T3> {
        return values.mapNotNull { it?.let { it1 -> map(it1, arg) } }
    }
}