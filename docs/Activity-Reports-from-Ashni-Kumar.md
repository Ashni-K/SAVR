#### May 26 - June 1, 2025

#### Issues
There are no issues to report.

#### **Activity Report**
Profile - Crop profile pictures - Review  
5 min  
I checked MeActivity.kt to see that there was the correct implementation to be able to crop the user's profile image and then save this new profile image. The onActivityResult method allows for the new image to be saved. All acceptance criteria met.  
5/29 @ 10:58 am moved to Done

Documentation - Update README weeks 8 & 9 - In-Progress  
20 min  
I worked on the README.md in the repository to provide instructions for working with the codebase, most importantly setting it up and getting the app running. I also described dependencies and integrations with other systems, like the Pexels API and the Gemini API so that those who might work on this project in the future are aware of the tools this project uses.  
5/29 @ 1:55 am moved to Review

Documentation - Completed System Manual - In-Progress  
35 min  
I worked on the system manual to assist IT professionals who will be supporting the users. I ensured that all relevant information was included, such as hardware requirements, error messages, and contact information. I inspected our homescreen.xml to determine if this manual should be in our project, but I determined that it would take away from the current design of our home screen. But it will still be helpful to IT professionals since it is contained within the project's documentation, and we can publish the manual alongside the app if we do choose to publicly release the app.  
5/29 @ 1:28 am moved to Review

Documentation - Completed User Guide - In-Progress  
60 min  
I worked on the user guide to orient the user to the structure of the app, their options, and the limitations of the app. This involved first evaluating the homescreen.xml file in our app to determine if the user guide should be contained within the app or only the wiki. I determined it would be best to only include it in the wiki because the app only has 4 main menu buttons at the bottom of the home screen, so the user interface is pretty intuitive. So in the team wiki, I added a page titled "User Documentation" with information on how to start the system, how to use the system features, screenshots of the main features, and example inputs and outputs.  
5/29 @ 12:45 am moved to Review

Profile - Create profile name - Review  
5 min  
I checked that the user could edit their user name on the Me page and that it saves even after clicking off the page. It was clear from MeActivity.kt and me.xml that the EditText was properly implemented with a setOnEditorActionListener. Thus, all acceptance criteria were met.  
5/26 @ 2:07 pm moved to Done

Signed: Spoorthi Pyarilal, Anya Patel, Dinajda Dollani

#### May 19 - May 25, 2025

#### Issues
There are no issues to report.

#### **Activity Report**
Profile - Create account - In-Progress  
280 min  
I created the user authentication backend using Node.js and Express to interact with the MySQL database securely. I created a new folder called auth-app-backend and added an index.js file, which connects to MySQL, defines the Registration API, defines the Login API, and starts the server. I added Retrofit dependencies to build.gradle so that we can use Retrofit to make HTTP requests to the Node.js backend. I created LoginService.kt to define the Retrofit interface with register and login requests. I created RetrofitClient.kt to add an initialization for Retrofit. I used the loopback IP address 10.0.2.2 to access my local machine from the emulator. I also updated LoginActivity.kt to handle the login logic by redirecting to the MainActivity if the login credentials are valid. If not, the app uses Toast to create a popup notification that lets the user know if all the fields have not been filled in or are invalid. I created activity_register.xml with EditText fields for the email and password entries as well as a "Register" button. The onCreate method in LoginActivity.kt contains a setOnClickListener for the signUpText so that the app navigates to the RegisterActivity when the user wants to create a new account. RegisterActivity.kt handles the button click logic for registering a user and makes an API call using Retrofit. In AndroidManifest.xml, I registered the RegisterActivity. In the same file, I also had to allow cleartext (non-HTTPS) traffic.  
5/25 @ 2:11 pm moved to Review

Profile - Link ME page to every other page - Review  
5 min  
I checked FavoritesActivity.kt, MainActivity.kt, PantryActivity.kt, and Recipes.kt to see that there is a setOnClickListener for the Me button. In the app, the user is able to click on the Me button from any of the other pages to be able to access their profile. So all acceptance criteria met.  
5/22 @ 1:10 pm moved to Done

Signed: Spoorthi Pyarilal, Dinajda Dollani, Anya Patel

#### May 12 - May 18, 2025

#### Issues
There are no issues to report.

#### **Activity Report**
Profile - Create login page - In-Progress  
135 min  
I created and worked in activity_login.xml to implement what the login page for the app should look like. I added TextViews for the SAVR app name and sign up field, EditText tags for the email and password fields, and Button field for the login button. I ensured the colors matched the colors we are using throughout the rest of the app. I also created LoginActivity.kt to manage the email field, password field, login button, and sign up button. In this file, I initialized the Intent class to navigate to MainActivity.kt so that the user is directed to the home page when they log in. I updated AndroidManifest.xml so that Android knows that LoginActivity exists, and I set it to be the launcher activity so that the home page appears first when running the app.  
5/18 @ 10:14 pm moved to Review

Signed: Spoorthi Pyarilal, Dinajda Dollani, Anya Patel

#### May 5 - May 11, 2025

#### Issues
There are no issues to report.

#### **Activity Report**
Filters - Buttons for filtering - Review  
5 min  
I checked recipes.xml to see that there were CheckBox views for the filtering options. They can be checked and unchecked, and there are different boxes for the different filters. All acceptance criteria met.  
5/7 @ 11:00 am moved to Done

Filters - Scroll widget - In-Progress  
30 min  
In Recipes.kt, I implemented the RecipeFilterAdapter's notifyFilterChanges() method to create a list of active filter descriptions, and then the FilterChangeListener interface is used to update the TextView in recipes.xml with these filters. This allows the user to be able to view the active filters while they are on the recipes page.  
5/7 @ 1:24 am moved to Review

Filters - Filters work - In-Progress  
140 min  
I worked in Recipes.kt to update the Recipe class with a filter change listener, an updateActiveFiltersText function, and a showFilterDialog function to appropriately apply the user's selected filters. I updated the RecipeAdapter to be RecipeFilterAdapter such that it uses the applyFilters function to perform the filtering of the recipes by looping through each recipe object and checking for the criteria. In recipes.xml, I added the "Filters" button and a TextView for the active filters. In recipe_item.xml, I added TextViews for both sides of the recipe cards to inform the user to tap the card to be able to interact with it. I created the filter_dialog.xml to format what the pop-up looks like when the user clicks the "Filters" button. Our team should consider improving the design of this pop-up to better fit the UI of the rest of the app. I also created StringExtensions.kt to hold a basic function to streamline the capitalization of various text displays in our app.  
5/7 @ 12:52 am moved to Review

Signed: Dinajda Dollani, Anya Patel, Spoorthi Pyarilal

#### April 28 - May 4, 2025

#### Issues
There are no issues to report.

#### **Activity Report**
Filters - Change Front End - Review  
5 min  
I checked activity_pantry.xml, homescreen.xml, recipe_item.xml, and recipes.xml to see that the colors and fonts have been updated for all the pages in our app. I tested the app from a user's perspective, and the color scheme is consistent. So all acceptance criteria met.  
5/3 @ 11:09 pm moved to Done

Favorites - Center menu buttons - In-Progress  
115 min  
I worked on centering the menu buttons at the bottom of the pantry page and recipes page. I deleted the ImageButton in homescreen.xml because there was no need for it. In recipes.xml, I changed android:layout_width to be match_parent. For both homescreen.xml and recipes.xml, I removed app:layout_constraintStart_toEndOf for each of the buttons, and I changed app:layout_constraintVertical_bias to be "0.979" from "0.98". In activity_pantry.xml, I changed the TextView for the "My Pantry" text in activity_pantry.xml to be white for better visibility, and I added android:textColor="#f4f3e6" to the AutoCompleteTextView so that the ingredients the user types in the search bar are white. I also changed the "Search ingredients" text in the search bar to be white by adding android:textColorHint="#f4f3e6" to the same AutoCompleteTextView.  
5/1 @ 2:49 pm moved to Review

Favorites - Create a favorites page - Review  
5 min  
I checked the layout folder to see if there was a favorites.xml, and I checked com.example.savr to see that there was a FavoritesActivity.kt. These files allow us to set up the favorites page and display the user's favorite recipes. All acceptance criteria were met.  
5/1 @ 1:26 pm moved to Done

Signed: Spoorthi Pyarilal, Dinajda Dollani, Anya Patel

#### April 21 - April 27, 2025

#### Issues
There are no issues to report.

#### **Activity Report**
Filters - Change max recipes - In-Progress  
85 min  
In Recipes.kt, I changed the Gemini prompt to have 12 recipes rather than 5. We had initially planned to have 20 recipes appear, but I was getting an error that the max tokens were exceeded, so I determined 12 recipes was a good amount to appear without causing issues. But this did cause the app to take even longer in order to generate this larger number of recipes, so I looked into how I could improve the speed. I worked in Recipes.kt to parallelize the image fetching using Kotlin coroutines' async builder that fetches images in parallel and the awaitAll() function that waits for all the parallel fetches to complete. If the team has more time, we can look into other methods of increasing the speed. I also worked in homescreen.xml and recipes.xml to fix the positioning of the menu buttons by removing conflicting constraints, removing the redundant inner ConstraintLayout, making the vertical bias values consistent, and making the FrameLayout a direct child of the main ConstraintLayout.  
4/24 @ 3:58 pm moved to Review

Filters - Accommodate filtering buttons - In-Progress  
45 min  
I worked in app/src/main/res/layout/recipes.xml to change the transparent View's height from 100dp to 200dp. I also added a 200dp paddingTop to the RecyclerView so that the RecyclerView acknowledges this spacing with padding. Since clipToPadding is False, the scrolling still works properly. We can adjust the 200dp value as we need to add more or less space at the top.  
4/24 @ 9:51 am moved to Review

Signed: Dinajda Dollani, Spoorthi Pyarilal, Anya Patel

#### April 14 - April 20, 2025

#### Issues
There are no issues to report.

#### **Activity Report**
Descriptions - Format background color - Review  
5 min  
I checked recipe_item.xml to see that the background color for the recipe cards had been changed and matched the aesthetic of our app. The color does not reduce the readability of the recipe cards, so I can still easily read the information. All acceptance criteria met.  
4/17 @ 11:01 pm moved to Done

Descriptions - Add nutrition information - In-Progress  
65 min  
In Recipes.kt, I added a new field in the Recipe data class for the nutrition information to be a String. I updated the buildPrompt function so that the AI also outputs nutrition information in the JSON response. I then updated the recipe_item.xml to have a TextView for the nutrition information and bound the data to UI elements by updating ViewHolder and onBindViewHolder in Recipes.kt.  
4/14 @ 8:46 pm moved to Review

Signed: Spoorthi Pyarilal, Anya Patel, Dinajda Dollani

#### April 7 - April 13, 2025

#### Issues
There are no issues to report.

#### **Activity Report**
Descriptions - Get pictures for the recipes - Review  
5 min  
I checked Recipes.kt to ensure that an API for the recipe pictures has been identified and an API key has been successfully obtained. All acceptance criteria met.  
4/13 @ 2:32 pm moved to Done

Descriptions - Add cuisine type - In-Progress   
55 min  
I updated the Gemini prompt to include cuisine type by editing the buildPrompt function in Recipes.kt. In the same file, I updated the onBindViewHolder method to include the cuisine. To finish updating the UI, I added a new TextView in recipe_item.xml and then referenced this new TextView in the ViewHolder class, which is in Recipes.kt. I now needed to format how the prep time, cook time, difficulty, and cuisine would all display on the front of the recipe cards since they were getting cramped. So in recipe_item.xml, I grouped the cook time and prep time in one LinearLayout, and I grouped the difficulty level and cuisine type in another LinearLayout.  
4/13 @ 2:24 pm moved to Review

Descriptions - Add cuisine type - In-Progress   
80 min  
In Recipes.kt, I added a cuisine data field to the Recipe data class. After this, I tried to change the buildPrompt function to also include a cuisine type for each object in the JSON array, but I messed up code that was previously working. I spent the remaining time in lab researching how to best add the cuisine type without messing up existing code. I was able to revert any issues I caused in the Recipes.kt file, and everything is working normally now. I now think I know how to add the cuisine type when I work on this project later.  
4/10 @ 2:47 pm remained In-Progress

Signed: Spoorthi Pyarilal, Dinajda Dollani, Anya Patel
#### March 31 - April 6, 2025

#### Issues
I have not updated my Android Studio to the latest version, so there might be some issues when team members pull my code. I am happy to update though if others have already updated their versions.

#### **Activity Report**
Descriptions - Add difficulty rating - In-Progress  
50 min  
In Recipes.kt, I updated the Recipe data class to include the difficulty field, the buildPrompt function to ensure each object in the JSON array also has a difficulty level, the RecipeAdapter's onBindViewHolder method to add the difficulty text, and the ViewHolder class to include a reference for the difficulty text view. Then, I added a TextView for the difficulty field in recipe_item.xml to display the recipe's difficulty level in the UI.  
4/6 @ 11:02 pm moved to Review

Signed: Dinajda Dollani, Spoorthi Pyarilal, Anya Patel

#### March 3 - March 9, 2025

##### Issues
There are no issues to report.

##### **Activity Report**
Design the Recipes page and implement a square, scrollable grid - Recommend Recipes - Review  
5 min  
I reviewed recipes.xml to make sure that the recipes on the recipes page are scrollable and organized properly. We might change some design elements in the future, but all acceptance criteria met right now.  
3/6 @ 1:59 pm moved to Done

Display recommendations on screen - Recommend Recipes - Review  
5 min  
I checked recipes.xml to make sure that the recipes are actually displaying, and they are. All acceptance criteria met.  
3/6 @ 1:52 pm moved to Done

Delete ingredients from search bar - Search Ingredients - Review  
5 min  
I checked PantryActivity.kt to see that the changes were implemented appropriately to be able to delete ingredients from the pantry list. All acceptance criteria are met because users can swipe right to delete items.  
3/6 @ 12:51 pm moved to Done

Position all buttons in pantry page correctly - Search Ingredients - Review  
5 min  
I checked activity_pantry.xml to make sure all the buttons on the pantry page are visible and appropriately formatted. All acceptance criteria met.  
3/6 @ 10:30 am moved to Done

Secure Gemini API key - Recommend Recipes - In-Progress  
50 min  
I edited build.gradle.kts to add a buildConfigField to reference the Gemini API key. In Recipes.kt, I changed the apiKey private value from the hardcoded API key to a reference to the BuildConfig. In gradle.properties, I created GEMINI_API_KEY so that the gradle loads the API key. I stored the key in local.properties, which should not be committed to version control. So I shared the API key with my group members so that they can update their personal local.properties files.  
3/5 @ 12:36 pm moved to Review

Signed: Dinajda Dollani, Spoorthi Pyarilal, Anya Patel
#### February 24 - March 2, 2025

##### Issues
There are no issues to report.

##### **Activity Report**
Design pantry page - Input Ingredients - Review  
5 min  
I made sure that all acceptance criteria were met by checking that the activity_pantry.xml had design elements like our Canva blueprint design.  
3/2 @ 8:53 am moved to Done

AI outputs recommendations - Recommend Recipes - In-Progress  
30 min  
I realized I should have been using the "gemini-1.5-flash" as the model in Recipes.kt. So now the recipe recommendations output on the Recipes page. Further design of how the recipes display is necessary.    
3/1 @ 1:00 am moved to Review

AI takes user's ingredients as inputs - Search Ingredients - In-Progress  
20 min  
I created recipe_item.xml to roughly determine how the recipes should display on the Recipes page, but this design should be revisited for the best user interaction experience. If the team decides to not display images with the recipe descriptions, I should remove the ImageView. I added some log statements in Recipes.kt to understand how the Gemini API was handling requests, and I determined that it was receiving the user's stored ingredients list.  
3/1 @ 12:28 am moved to Review

AI takes user's ingredients as inputs - Search Ingredients - In-Progress  
80 min  
I added some dependencies in build.gradle.kts to be able to use the Gemini API. I worked in Recipes.kt to add my Gemini API key as a private value, wrote the getRecipeRecommendations function to perform the API call, wrote the extractJsonFromResponse to get the JSON array in the response, wrote the buildPrompt function to tell the AI what prompt to use, wrote the parseRecipeResponse function to parse the JSON object, and created the RecipeAdapter class to properly display the recipes. In PantryActivity.kt, I initialized the "See All Recipes" button to redirect to the Recipes page, but I still need to confirm that the AI model is receiving the user's stored ingredients.  
2/27 @ 2:50 pm remained In-Progress

Signed: Spoorthi Pyarilal, Anya Patel, Dinajda Dollani

#### February 17 - February 23, 2025

##### Issues
There are no issues to report.

##### **Activity Report**
Create a recipe page - Recommend Recipes - Review  
3 min  
I checked Recipes.kt to ensure it was separate from all the other Kotlin files and that it initiated a new Kotlin class. All acceptance criteria were met.  
2/23 @ 11:27 pm moved to Done

Search bar linked to ingredients - Search Ingredients - In-Progress  
80 min  
I worked in PantryActivity.kt to implement the API calls and handle the responses. I edited activity_pantry.xml to have an AutoCompleteTextView that allows for the autocompleted ingredients to display. I deleted SpoonacularAPI.java, APIResponse.java, IngredientAdapter.java, and Ingredient.java since I realized I should have been working in Kotlin rather than Java.  
2/20 @ 2:41 pm moved to Review  

Search bar linked to ingredients - Search Ingredients - In-Progress  
95 min  
I edited build.grade.kts to add the proper dependencies to connect to the Spoonacular API and to have a recycler view on the activity_pantry.xml file. I worked in Pantry.java to implement the main Pantry class that fetches and receives the API communications. SpoonacularAPI.java and APIResponse.java handle the response contents. activity_pantry.xml has a RecyclerView widget to continuously update the autocomplete suggestions for the search bar. I tried to use IngredientAdapter.java, Ingredient.java, and ingredient_item.xml to update the ingredient drop-down list, but I think these might not actually be necessary. I added a permission in AndroidManifest.xml to allow the app to connect to the Internet in the hopes that this might fix some issues.  
2/19 @ 12:48 am remained In-Progress  

Signed: Spoorthi Pyarilal, Anya Patel, Dinajda Dollani

#### February 10 - February 16, 2025

##### Issues
There are no issues to report.

##### **Activity Report**
Create pantry page - Input Ingredients - Review  
5 min  
I confirmed that homescreen.xml exists and passed all acceptance criteria. Since our project is using Kotlin in Android Studio rather than using Flutter, the home page is no longer main.dart.  
2/16 @ 11:20 pm moved to Done

Implement an ingredient API - Input Ingredients - In-Progress  
105 min  
I implemented the Spoonacular API in lib/pantry_page.dart by adding the API key so that we can use this ingredients API. I added code to lib/pantry_page.dart so that the search bar recognizes this API. I realized we no longer need the search_bar_widget.dart file. I pushed my changes to the search-bar branch.     
2/13 @ 12:52 pm moved to Review

Signed: Spoorthi Pyarilal, Dinajda Dollani, Anya Patel

#### February 3 - February 9, 2025

##### Issues

I need to understand Java better so that I can debug more effectively and integrate my changes with team members' changes seamlessly.

##### **Activity Report**

Create search bar - Input Ingredients - In-Progress  
40 min  
I researched how to connect the GitLab repository with my Android Studio project and set up SSH key in order to do this. I then researched how to create a search bar in Android Studio and worked on some code in the search_bar_widget.dart file. I pushed changes to the search-bar branch.  
2/9 @ 5:40 pm remained In-Progress

Create search bar - Input Ingredients - To-Do  
110 min  
I experimented with setting up various development environments, including React, Android Studio, Virtual Studio Code, and Flutter. I eventually got Flutter working in Android Studio.  
2/6 @ 2:42 pm moved to In-Progress

Signed: Spoorthi Pyarilal, Anya Patel, Dinajda Dollani

#### January 27 - February 2, 2025

##### Issues

There are no issues to report.

##### **Activity Report**

Signed: Dinajda Dollani, Spoorthi Pyarilal, Anya Patel