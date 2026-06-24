package com.bash4qi.mytest

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup

class MyPlugin : MainAPI() {
    override var mainUrl = "https://example.com"
    override var name = "ExAr"
    override val hasMainPage = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries
    )

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        val doc = app.get("$mainUrl/").document
        val home = ArrayList<HomePageList>()
        
        // Example: Scrape trending movies
        val trending = doc.select(".trending-item").mapNotNull {
            val title = it.select(".title").text()
            val url = it.select("a").attr("href")
            val img = it.select("img").attr("src")
            
            if (title.isNotBlank() && url.isNotBlank()) {
                newMovieSearchResponse(title, url, TvType.Movie) {
                    this.posterUrl = img
                }
            } else null
        }
        
        if (trending.isNotEmpty()) {
            home.add(HomePageList("Trending", trending))
        }
        
        return HomePageResponse(home)
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url).document
        
        // Extract title
        val title = doc.select("h1.title").text()
        
        // Extract description
        val description = doc.select(".description").text()
        
        // Extract poster
        val poster = doc.select(".poster img").attr("src")
        
        // Extract year
        val year = doc.select(".year").text().toIntOrNull()
        
        // Extract episodes/sources
        val episodes = doc.select(".episode-link").mapNotNull { 
            val epUrl = it.attr("href")
            val epName = it.text()
            if (epUrl.isNotBlank()) {
                Episode(epUrl, epName)
            } else null
        }
        
        return if (episodes.isNotEmpty()) {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.plot = description
                this.year = year
            }
        } else {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = description
                this.year = year
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/search?q=$query").document
        return doc.select(".search-result").mapNotNull {
            val title = it.select(".title").text()
            val url = it.select("a").attr("href")
            val img = it.select("img").attr("src")
            
            if (title.isNotBlank() && url.isNotBlank()) {
                newMovieSearchResponse(title, url, TvType.Movie) {
                    this.posterUrl = img
                }
            } else null
        }
    }
}
