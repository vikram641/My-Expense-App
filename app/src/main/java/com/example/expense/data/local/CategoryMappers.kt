package com.example.expense.data.local

import com.example.expense.data.model.CatDataResponse

fun CatDataResponse.toEntity(): CategoryEntity = CategoryEntity(
    id = id.orEmpty(),
    name = name.orEmpty(),
    color = color.orEmpty(),
    icon = icon.orEmpty(),
    isDefault = isDefault ?: false
)

fun CategoryEntity.toDomain(): CatDataResponse = CatDataResponse(
    id = id,
    name = name,
    color = color,
    icon = icon,
    isDefault = isDefault
)

fun List<CatDataResponse>.toEntityList(): List<CategoryEntity> = map { it.toEntity() }
fun List<CategoryEntity>.toDomainList(): List<CatDataResponse> = map { it.toDomain() }
