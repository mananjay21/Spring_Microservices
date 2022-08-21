package io.javaworkshop.moviecatalogservices.resources;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.javaworkshop.moviecatalogservices.models.CatalogItem;
import io.javaworkshop.moviecatalogservices.models.Movie;
import io.javaworkshop.moviecatalogservices.models.UserRating;
import io.javaworkshop.moviecatalogservices.services.MovieInfo;
import io.javaworkshop.moviecatalogservices.services.UserRatingInfo;


@RestController
@RequestMapping("/catalog")


public class MovieCatalogResource {

    private final RestTemplate restTemplate;
    private final UserRatingInfo userRatingInfo;
    private final MovieInfo movieInfo;

    public MovieCatalogResource(RestTemplate restTemplate, UserRatingInfo userRatingInfo, MovieInfo movieInfo) {
        this.restTemplate = restTemplate;
        this.userRatingInfo = userRatingInfo;
        this.movieInfo = movieInfo;
    }

     @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = userRatingInfo.getUserRating(userId);

        return userRating.getUserRatings().stream().map(movieInfo::getCatalogItem).
                collect(Collectors.toList());
    }

    /* Alternative way to call api
Movie movie = webClientBuilder.build()
                        .get()
                        .uri("localhost:8082/movies/" + rating.getMovieId())
                        .retrieve()
                        .bodyToMono(Movie.class)
                        .block();
                        */
}