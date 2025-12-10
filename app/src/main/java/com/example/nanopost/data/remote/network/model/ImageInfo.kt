package com.example.nanopost.data.remote.network.model

data class ImageInfo (
    val name: String,
    val mimeType: String?,
    val bytes: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageInfo

        if (name != other.name) return false
        if (mimeType != other.mimeType) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}