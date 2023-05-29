package io.github.zkit.beans.enums

internal enum class EnumWatermarkType(val value: String) {
    Text(value = "text"),
    Time(value = "time"),
    Date(value = "date"),
    DateTime(value = "datetime"),
    Address(value = "address"),
    Location(value = "location")
}