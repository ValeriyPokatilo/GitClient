package app.xl.androidapp.data.network

fun String.toBearerHeader(): String {
    return "Bearer ${this.trim()}"
}