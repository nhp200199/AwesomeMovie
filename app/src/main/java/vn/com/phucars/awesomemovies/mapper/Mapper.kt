package vn.com.phucars.awesomemovies.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}