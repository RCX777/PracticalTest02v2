package ro.pub.cs.systems.eim.practicaltest02v2

data class WordResponse(
    val word: String,
    val meanings: List<Meaning>
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>
)

data class Definition(
    val definition: String,
    val example: String? = null
)
