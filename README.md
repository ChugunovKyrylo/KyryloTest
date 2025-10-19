# Gifs Searcher (Android App)
The app selects GIF animations based on your text input. Describe an emotion, situation, or thought — and get a matching GIF in return.

![](https://github.com/ChugunovKyrylo/KyryloTest/blob/master/screenshots/search_text_view.png)
![](https://github.com/ChugunovKyrylo/KyryloTest/blob/master/screenshots/grid_items_success.png)
![](https://github.com/ChugunovKyrylo/KyryloTest/blob/master/screenshots/grid_items_error.png)
![](https://github.com/ChugunovKyrylo/KyryloTest/blob/master/screenshots/details_screen.png)

## API
This app uses the [Giphy API](https://developers.giphy.com/docs/api/), which allows searching for and retrieving GIFs based on various queries.

## Launch
### Requirements
  - Min SDK - 26
  - Target SDK - 36
### Downloading
  - You can launch the project by the [link](https://drive.google.com/file/d/1SfyS-miUQr3bvrh-_tHiDMYc6afT05Ny/view?usp=sharing).
### Clone the repository.
  Set up your API key:
   - Open the file `app/src/main/java/com/kyrylo/gifs/data/remote/ApiKeyInterceptor.kt`.
   - Replace the `YOUR_KEY` field with your API key.

## Libs
- **Hilt** — Dependency Injection
- **Coil** — Image Loading
- **JUnit4** — Unit Testing
- **Retrofit** — Networking
- **Kotlin Coroutines & Flows** — Asynchronous programming

## Features
- Support for **Dark Theme**
- Pagination for smooth data loading
- Shimmer effect during image loading
- Splash screen on app launch
- Support for **vertical & horizontal** orientations
- Error handling
- Grid and detail screens
