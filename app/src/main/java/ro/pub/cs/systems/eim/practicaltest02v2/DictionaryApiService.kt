package ro.pub.cs.systems.eim.practicaltest02v2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApiService {
    @GET("api/v2/entries/en/{word}")
    fun getWordMeaning(@Path("word") word: String): Call<List<WordResponse>>
}

