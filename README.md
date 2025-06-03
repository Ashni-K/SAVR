## Name
SAVR - A Recipe Generator
## Description
SAVR is a recipe management Android app designed to help users easily search, filter, and save meals based on their ingredients and cook time. Whether you're a beginner cook or a seasoned foodie, SAVR makes it simple to find inspiration and keep your favorite recipes in one place.

**Features**
- Search for ingredients to include in pantry (can also remove items from pantry)
- Generate recipes that only use ingredients user has included in pantry
- Filter recipes by preparation time, cook time, difficulty level, cuisine type, and calories (can also reset filters)
- Save and favorite recipes for easy access (can also remove from favorites)
- Automatically fetch recipe images using the Pexels API
- Personal profile to maintain username and profile picture
- Clean, user-friendly interface built with Android Studio and Kotlin

**Background**

This app was built as part of a team project to explore real-world Android app development. We focused on creating a clean UI, integrating a third-party image API, and solving common UX problems like ingredient filtering.

**Why SAVR?**

Unlike other basic recipe apps, SAVR emphasizes personalization. By letting users filter based on what they already have, it reduces food waste and makes cooking way more convenient.

## Installation
Requirements:
- Android Studio Electric Eel or newer
- Kotlin 1.8+
- Gradle 8.x
- Emulator or physical Android device
- Internet connection (for fetching images via Pexels API)
- Request our API keys from us

Setup steps:
- Clone the repo
- Open the project in Android Studio
- Add the API keys to local.properties
- Run the app

## Usage
Once the app is installed and running, here’s how to start using SAVR:

**Searching Recipes**
- Use the search bar at the top to find recipes by name or main ingredient.

**Tap "Apply Filters" to filter by:**
- Maximum preparation time (using the slider)
- Maximum cooking time (using the slider)
- Difficulty level (using the multiple choice options)
- Cusine type (using the dropdown menu)
- Maximum calories (using the slider)

**Favoriting Recipes**
- Tap the "Add to Favorites" button on a recipe card to save it to favorites
- Access your saved recipes via the Favorites menu button
- Tap the "Remove from Favorites" button on a recipe card to remove the recipe from favorites

**Image Integration**
- Images are automatically pulled from the Pexels API based on recipe keywords
- If no image is found, a placeholder will be shown

## Support
Feel free to reach out to us through our email!

Dinajda Dollani - dd3298@drexel.edu

Spoorthi Pyarilal - sp3932@drexel.edu

Ashni Kumar - apk66@drexel.edu

Anya Patel - amp638@drexel.edu

## Roadmap
Here are a few features and improvements we’re planning for future versions of SAVR:
- Allow users to upload their own recipes
- Enable image uploads from device instead of just using Pexels API
- Add grocery list generation based on selected recipes
- Improve UI/UX for filter selection
- Dark mode support
- Push notification reminders for saved meals

## Contributing
At this time, we are not accepting external contributions to this project. If you have suggestions or feedback, feel free to contact us. We’re always open to ideas and improvements.

## Authors and acknowledgment
This project was built collaboratively by a team of students, combining our skills in design, development, and problem-solving.

**Team Members**
- Dinajda Dollani
- Spoorthi Pyarilal
- Ashni Kumar
- Anya Patel

**Special Thanks**
- Our instructor and classmates for their support and feedback
- Image support powered by the Pexels API
- Text generation and assistance provided by the Gemini API

## License
This project is for educational purposes only and is not currently licensed for open-source distribution.

Feel free to explore the code and learn from it, but please don’t reuse or distribute without permission.

## Project status
This project is currently complete for academic purposes and not under active development.

While we don’t plan to continue adding new features at the moment, feel free to explore, learn from the code, or fork it for your own use. If you'd like to build on it or adapt it, go for it. Just give credit where it’s due!