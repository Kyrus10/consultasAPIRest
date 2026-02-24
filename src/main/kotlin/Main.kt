package org.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

fun obtenerPosts(): List<Post> {
    val client = HttpClient.newHttpClient()

    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
        .GET()
        .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString<List<Post>>(response.body())
}
fun main() {

        println("Descargando datos...")
        val posts = obtenerPosts()
        println("Se han descargado ${posts.size} posts\n")

        // CONSULTA 1: Posts de un usuario concreto (el usuario lo elige el usuario por teclado)
        print("Introduce un userId (del 1 al 10): ")
        val userId = readLine()!!.toInt()
        val postsDeLUsuario = posts.filter { it.userId == userId }
        println("\nEl usuario $userId tiene ${postsDeLUsuario.size} posts:")
        postsDeLUsuario.forEach { println("  - ${it.title}") }

        // CONSULTA 2: Posts ordenados por longitud de título (de más corto a más largo)
        println("\n--- Los 5 posts con el título más corto ---")
        val ordenadosPorTitulo = posts.sortedBy { it.title.length }.take(5)
        ordenadosPorTitulo.forEach { println("  [${it.title.length} chars] ${it.title}") }

        // CONSULTA 3: Cuántos posts ha escrito cada usuario
        println("\n--- Posts por usuario ---")
        val postsPorUsuario = posts.groupBy { it.userId }
        postsPorUsuario.forEach { (usuario, listaposts) ->
            println("  Usuario $usuario: ${listaposts.size} posts")
        }

        // CONSULTA 4: Buscar posts cuyo título contenga una palabra clave
        print("\nIntroduce una palabra para buscar en títulos: ")
        val palabra = readLine()!!.lowercase()
        val resultados = posts.filter { it.title.lowercase().contains(palabra) }
        println("Se encontraron ${resultados.size} posts con '$palabra':")
        resultados.forEach { println("  - ${it.title}") }
    }


