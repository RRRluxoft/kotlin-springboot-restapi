KTL01 rlytvyshko

>  gradlew clean build
>  gradlew bootRun
or just run class MoviesCatalogueApplication

and goto http://localhost:8091/swagger-ui.html

Enjoy

*The REST API endpoints structure could be implemented like this:
** api/movies
** api/movies/add
** api/movies/{id}  ->  /movies/{title}
** api/users/
** api/users/{id}/profile
** api/users/{id}/orders
** api/users/{id}/orders/{id}
** api/users/{id}/balance


Develop a movies catalogue REST API. Treat it as a backend part of streaming service UI. The API should provide the functionality to cover the following scenarios:

**Return the movies (lightweight version of data) in catalogue page by page
**Search movies by name
**Support sorting by different fields
**View movie’s detailed information
**Add new movie
**View user’s profile
**View user’s balance in USD
**Add some value to the balance (we do not require it to be secure, just make an ability to top up the balance)
**View user’s orders
*Order the movie (make sure that user’s balance is decreased by the defined amount)