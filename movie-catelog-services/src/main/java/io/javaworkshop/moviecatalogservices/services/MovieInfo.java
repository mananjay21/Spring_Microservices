package io.javaworkshop.moviecatalogservices.services;
 
    

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.javaworkshop.moviecatalogservices.models.CatalogItem;
import io.javaworkshop.moviecatalogservices.models.Movie;
import io.javaworkshop.moviecatalogservices.models.Rating;
import io.javaworkshop.moviecatalogservices.models.UserRating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;


    @HystrixCommand(
        fallbackMethod = "getFallbackCatalogItem",
        threadPoolKey = "movieInfoPool",
        threadPoolProperties = {
                @HystrixProperty(name = "coreSize", value = "20"),
                @HystrixProperty(name = "maxQueueSize", value = "10")
        })
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class,UserRating.class);
        return new CatalogItem(movie.getTitle(), movie.getOverview(), rating.getRating());
    }

    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "Description not found", rating.getRating());
    }
} 