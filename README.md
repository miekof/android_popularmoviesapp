# Popular Movies App
This is my P1 project, Popular Movies, for [Udacity's Android Developer Nanodegree](https://www.udacity.com/course/android-developer-nanodegree--nd80) program. This application reads movie information from  [themoviedb.org](themoviedb.org) and displays movie posters in either the most popular or the top rated order. When user clicks each movie poster images, it diplays detail information (Original title, poster, rating, release date and synopsis) of the movie selected.

# Obtaining and Adding API Key
This application does not compile nor run without API Key.
1. To fetch popular movies, you will use the API from [themoviedb.org](themoviedb.org).
  * If you don't have an account, you need to create one to obtain an API key, go to 
[Sign up for an accout](https://www.themoviedb.org/account/signup).
  * Once you obtain your Key, you need to set the `MOVIE_API_KEY` enviornment variable with your API Key.
    * For Windows, please refer [To add or change the values of environment variables](https://www.microsoft.com/resources/documentation/windows/xp/all/proddocs/en-us/sysdm_advancd_environmnt_addchange_variable.mspx?mfr=true)
    * For Unix, execute the following command.
      * Korn and bash shells:
        ```export MOVIE_API_KEY = <Your API Key>```
      * C shell
        ```setenv MOVIE_API_KEY <Your API Key>``` 

