package vn.com.phucars.awesomemovies.data.title

import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.ResultData

class TitleRepositoryImpl(private val titleDataSource: TitleDataSource) : TitleRepository {
    override suspend fun getTitlesByGenre(genre: String): ResultData<List<TitleData>> {
        val titlesByGenre = titleDataSource.getTitlesByGenre(genre)
        if (titlesByGenre is ResultData.Success) {
            return ResultData.Success(titlesByGenre.data.results)
        } else throw Exception()
    }

    override suspend fun getTitleById(id: String): ResultData<TitleData> {
        val titleById = titleDataSource.getTitleById(id)
        if (titleById is ResultData.Success) {
            return ResultData.Success(titleById.data.results)
        } else throw Exception()
    }

    override suspend fun getTitleRating(titleId: String): ResultData<TitleData.Rating> {
        val titleRating = titleDataSource.getTitleRating(titleId)
        if (titleRating is ResultData.Success) {
            return ResultData.Success(titleRating.data.results)
        } else throw Exception()
    }

    override suspend fun getGenres(): ResultData<List<String?>> {
        val genres = titleDataSource.getGenres()
        return if (genres is ResultData.Success) {
            ResultData.Success(genres.data.results.filterNotNull())
        } else {
            ResultData.Error((genres as ResultData.Error).exception)
        }
    }
}