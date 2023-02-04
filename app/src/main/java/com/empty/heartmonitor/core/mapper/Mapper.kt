package com.empty.heartmonitor.core.mapper

interface Mapper<T1, T2> {
    fun map(value: T1): T2
}