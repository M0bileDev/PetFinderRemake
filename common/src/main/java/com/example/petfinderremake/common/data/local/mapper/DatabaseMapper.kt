package com.example.petfinderremake.common.data.local.mapper

interface DatabaseMapper<E, D> {

    fun mapToDomain(databaseEntity: E): D
    fun mapToDatabaseEntity(domain: D): E
}