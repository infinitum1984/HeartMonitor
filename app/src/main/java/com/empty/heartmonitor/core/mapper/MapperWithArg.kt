package com.empty.heartmonitor.core.mapper

interface MapperWithArg<T1, T2, T3> {
    fun map(value: T1, arg: T2): T3
}