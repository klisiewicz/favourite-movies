# Popular Movies

Application allowing users to discover the most popular movies playing. It was made as a part of [Udacity Nanodegree](https://eu.udacity.com/course/android-developer-nanodegree-by-google--nd801) program.

Code quality: [![Codacy Badge](https://api.codacy.com/project/badge/Grade/55ff05d2d70f465b973c2c91de37ef27)](https://www.codacy.com/app/klisiewicz/popular-movies?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=klisiewicz/popular-movies&amp;utm_campaign=Badge_Grade)

### Prerequisites

Application utilizes [The Movie DB](https://www.themoviedb.org) API. You must provide your own API key in the `build.gradle` `file:

```
buildConfigField "String", "API_KEY", '"@TODO: enter your api key here"'
```

## Built With

* [Glide](https://github.com/bumptech/glide) - An image loading and caching library for Android.
* [Butterknife](https://github.com/JakeWharton/butterknife) - Bind Android views and callbacks to fields and methods.
* [Retrofit](https://github.com/square/retrofit) - Type-safe HTTP client for Android and Java by Square, Inc.
* [Dagger](https://github.com/google/dagger) - A fast dependency injector for Android and Java.
* [ReactiveX](https://github.com/ReactiveX) - Reactive Extensions for Async Programming

## Authors

* [Karol Lisiewicz](https://github.com/klisiewicz)
