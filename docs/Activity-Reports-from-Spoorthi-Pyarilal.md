#### **May 19 - May 25, 2025**

##### **Issues**  

There are no issues to report.  

##### **Activity Report** 

Visuals - Button Color update - Review  
7 min  
I checked if the buttons had a uniform color across all pages, and ensured that the color was more monotone, as specified in the acceptance criteria. All buttons looked as expected, and satisfied the task criteria.  
5/22 @ 2:01PM moved to Done  

Signed: Dinajda Dollani, Ashni Kumar

#### **May 12 - May 18, 2025**

##### **Issues**  

There are no issues to report.  

##### **Activity Report**  

Visuals - add logo onto home page - In-Progress  
420 min  
I began by finding suitable icons from Google to represent our app's cooking/recipe generation theme, specifically a fork, spoon, and a bowl of food. After selecting appropriate images, I ensured they were in .png format and color-corrected them to match the visual theme of our app. The finalized icons were added to the app/res/drawable directory. I then updated homescreen.xml by creating ImageView elements for each image and adjusting their position and size to fit the layout of the home page. I initially attempted to enhance the "SAVR" title using a larger size and custom font. However, due to Android Studio’s limitations with certain font types and text transformations (such as curved text), I instead designed the title in Canva and imported it as an image, just like the other icons.
I also added a TextView to display our app’s slogan, positioning it near the bottom of the home screen above the navigation bar. With the layout complete, I focused on animating UI elements to enhance user experience. My goal was to have the fork and spoon slide in from the left and right, respectively and the title, bowl icon, and slogan fade and slide up from below. To accomplish this, I created a new folder 'anim' under app/res. I added three animation XML files: fade_slide_up.xml (for fade-in and upward motion) , fork_slide.xml (for sliding in from the right) and spoon_slide.xml (for sliding in from the left) into the anim folder. I defined the appropriate animation behavior in each file, such as the start position, end position, and animation duration, etc. Once these animation files were functioning as intended, I applied them to the respective UI elements programmatically using MainActivity.kt, which controls the home screen’s behavior. Using Android’s AnimationUtils class, I assigned the fade_slide_up.xml animation to the title, bowl, and slogan elements. The fork icon was linked to fork_slide.xml, and the spoon icon to spoon_slide.xml, ensuring each element animated according to the design specifications. After confirming that all animations and visual elements were behaving correctly within the app, I deleted some unused resources. This involved few image and font files from the drawable and font directories. Additionally, I removed the contents of preloaded_fonts.xml located in app/res/values, as it was no longer serving any purpose, and I also deleted its corresponding reference in AndroidManifest.xml.  
5/19 @ 2:39PM moved to Review  

Signed: Ashni Kumar, Dinajda Dollani, 

#### **May 5 - May 11, 2025**

##### **Issues**  

There are no issues to report.  

##### **Activity Report**  

Filters - Filters work - Review  
5 min  
I reviewed to see if the filters work as expected. They did, and all acceptance criteria was met.  
5/7 @ 11:36PM moved to Done

Favorites - Get rid of "See All Recipes" button - In-Progress  
120 min  
I first edited the homescreen.xml to remove the "See All Recipes" button by deleting its whole button tag, and then also removed the part of the code in MainActivity.kt that would redirect to the recipes page when the "See All Recipes" button was clicked. All parts of the code that concerned the "See All Recipes" button in the home page has been removed.  
5/6 @ 10:24PM moved to Review

Signed: Dinajda Dollani, Ashni Kumar, Anya Patel

#### **April 28 - May 4, 2025**

##### **Issues**  

There are no issues to report.  

##### **Activity Report**  

Favorites - Link favorites button to home page - In-Progress  
150 min  
I edited AndroidManifest.xml to include another activity tag for FavoritesActivity.kt. I also created a FavoritesActivity class in FavoritesActivity.kt that links it to the appropriate favorites.xml page. Additionally, in MainActivity.kt (the home page), I added a section to link the favorites button to the favorites page. Lastly, I edited favorites.xml to include all the navigation buttons as other pages, so that something could be viewed on that page.   
5/5 @ 10:10PM moved to Review

Signed: Dinajda Dollani, Ashni Kumar, Anya Patel

#### **April 21 - April 27, 2025**

##### **Issues**

There are no issues to report.  

##### **Activity Report**

Filters - Change max recipes - Review  
5 min  
I checked to see if the max number of outputted recipes increased from 5 to 12. It correctly outputted 20 recipes, and were still correctly formatted. It passed all acceptance criteria and works as expected.  
4/27 @ 11:58PM moved to Done

Favorites - Link 'Favorites' button to home page - In-Progress  
50 min  
I am currently working on linking the 'Favorites' button between the favorites page I created, and the home page. This is not complete, so I am leaving it in In-Progress.  
4/27 @ 11:53PM kept in In-Progress

Favorites - Create a favorites page - In-Progress  
80 min  
I created a favorites page, 'favorites.xml', under the layout folder where all other .xml files are located for the pages. I also made a corresponding FavoritesActivity.kt file under the kotlin+java folder.  
4/24 @ 2:10PM moved to Review

Signed: Dinajda Dollani, Ashni Kumar, Anya Patel

#### **April 14 - April 20, 2025**

##### **Issues**

There are no issues to report.  

##### **Activity Report**

Descriptions - Formatting font (Titles, bolding, etc) - Review  
10 min  
I ensured that the titles on the cards were modified accordingly, bolded when necessary. I also checked if the font style and colors were changed, and if they matched the overall aesthetic of the app. The recipe cards looks as expected, and all the acceptance criteria was met.  
4/17 @ 1:50PM moved to Done

Descriptions - Formatting Recipe Cards - Review  
24 min  
The colors and fonts on the recipe cards were updated, and had a decorative border as mentioned in the tasks' acceptance criteria. The recipe cards were modified as expected, and passed all the requirements.  
4/17 @ 1:15PM moved to Done  

Descriptions - Put pictures in recipe cards - Review  
10 min  
I ensured that all recipe cards had their respective images displayed and were formatted correctly. The pictures were relevant to the recipe cards, and passed all the acceptance criteria.  
4/17 @ 12:43PM moved to Done  

Descriptions - Format background color - In-Progress  
150 min  
Worked on adjusting the card background color on recipe_item.xml to match the app’s theme and improve the color in the flip animation. Tested transparent and semi-transparent options but decided to keep a solid #fceeee background for the card's front and back side for a consistent look.  
4/14 @ 10:10PM moved to Review

Signed: Dinajda Dollani, Anya Patel, Ashni Kumar

#### **April 7 - April 13, 2025**

##### **Issues**

There are no issues to report.  

##### **Activity Report**

Descriptions - Add difficulty rating - Review  
10 min  
I checked Recipes.kt and recipe_item.xml to see if the Gemini prompt was changed to include difficulty rating. The difficulty rating appeared on the recipe card as expected, and all acceptance criteria were met.  
4/10 @ 1:42PM moved to Done

Signed: Ashni Kumar, Dinajda Dollani, Anya Patel

#### **March 31 - April 6, 2025**

##### **Issues**

There are no issues to report.  

##### **Activity Report**

Format font color - Descriptions - In-Progress  
125 min  
I edited activity.xml to change the font colors on the recipe cards so they better match the overall look of the app. I looked into which colors would go well with the rest of the design and make the text easier to read. For the recipe title, I chose a dark red color (#a03b3c) to make it stand out. Right below it, the "Cook Time" and "Prep Time" labels use a lighter shade (#c57262) that still fits with the theme. On the back of the card, I used the same idea. The "Ingredients" heading uses the darker red, and the list under it uses the lighter one. The same goes for "Instructions" and the steps that follow. I decided to use these specific color schemes because, even though our app mainly uses tan tones, these pinkish colors work well and still fits the style.  
4/7 @ 9:40PM moved to Review

Signed: Dinajda Dollani, Ashni Kumar, Anya Patel

#### **March 3 - March 9, 2025**

##### **Issues**

There are no issues to report.  

##### **Activity Report**

Make a 'Clear' button on Pantry Page - Search Ingredients - In-Progress  
90 min  
I added a 'Clear' button on activity_pantry.xml and edited PantryActivity.kt accordingly such that it clears all items under 'My Pantry' when clicked. I also worked on ensuring that 'My Pantry' became invisible upon clicking this button, since there would be no items in the pantry once the 'Clear All' button is pressed. This was done by setting appropriate conditions for the respective UI elements.  
3/9 @ 11:39PM moved to Review

Make sure items in 'My Pantry' are still stored even after switching pages - Search Ingredients - In-Progress  
150 min  
I added PantryAdapter.kt, and made the saveIngredients and loadIngredients functions to retain the ingredients under 'My Pantry'. This was done using the sharedPreferences storage system of Andriod that helps simple data (like the selectedInrgedients list) persist on the page between app launches or when the page is switched. I made the saveIngredients and loadIngredients to convert between Gson and Json types since sharedPreferences cannot store list items directly.  
3/9 @ 11:39PM moved to Review  

Signed: Ashni Kumar, 

#### **February 24 - March 2, 2025**

##### **Issues**

There are no issues to report.  

##### **Activity Report**

Position all buttons in pantry page correctly - Search Ingredients - In-Progress  
120 min  
I edited activity_pantry.xml to reposition the existing buttons in such a way that they were not overlapping or hiding each other. Each buttons is now clearly visible and redirect to the correct pages when clicked.  
3/3 @ 9:50PM moved to Review  

All available buttons work - Recommend Recipes - Review  
20 min  
I checked if all the navigation buttons, such as 'Home,' 'Pantry,' and 'See all recipes,' were linked to their respective pages. They worked as expected and met the acceptance criteria.  
3/2 @ 10:42PM moved to Done

Signed: Ashni Kumar, Anya Patel, Dinajda Dollani

#### **February 17 - February 23, 2025** 

##### **Issues**

There are no issues to report.  

##### **Activity Report**

Create a grid where ingredients can be stored - Search Ingredients - In-Progress  
40 min  
I modified activity_pantry.xml to include a rectangular region where the selected ingredients are outputted and printed on the screen. I also edited ingredient_item.xml accordingly to format how the selected ingredient items appear on the screen. I made their text size big enough and formatted them to the center. I also added a vertical scroll bar to the rectangular region so the items can be viewed even when they exceed the length of the screen.  
2/24 @ 12:57AM moved to Review 

Refine search bar for user - Search Ingredients - In-Progress  
20 min  
The search bar looked a bit wide, so I worked on activity_pantry.xml to rescale it to a smaller size to fit the screen better.  
2/22 @ 11:53PM moved to Review

Store Ingredients from search bar - Search Ingredients - In-Progress  
100 min  
I created an empty mutable list called selectedIngredients in ActivityPantry.kt. I also made a function called selectIngredient that adds every ingredient that was clicked into the mutable list (selectedIngredients). I made sure that the code listens for any clicked items and calls the selectIngredient function when it does so.   
2/22 @ 11:49PM moved to Review 

Select ingredients from search bar - Search Ingredients - In-Progress  
30 min  
I edited the Pantry Activity class in PantryActivity.kt such that it listens for when an item is clicked from the ingredients drop-down in the search bar.    
2/22 @ 11:45PM moved to Review 

Search bar linked to ingredients - Seach Ingredients - Review  
5 min  
I looked at the search bar and checked if it was correctly linked to the ingredients API. It worked as expected, and met all the acceptance criteria.  
2/20 @ 3:45PM moved to Done  

Signed: Anya Patel, Ashni Kumar, Dinajda Dollani

#### **February 10 - February 16, 2025**

##### **Issues**

There are no issues to report.

##### **Activity Report**


Link Home Page & Pantry Page - Input Ingredients - To-Do  
150 min  
I linked the Home Page and Pantry Page by creating PantryActivity.kt and edited the activity tag under AndroidManifest.xml to include another section for the Pantry Page so that they are linked. When the "Pantry" button is pressed from the Home Page, it will now display the Pantry Page.  
2/17 @ 10:00PM moved to Review


Design Home Page - Input Ingredients - Review  
8 min  
I looked at the home page and its design. It looked exactly how we designed it on Canva and had buttons for every page. It met all the requirements and acceptance criteria.  
2/17 @ 5:55PM moved to Done


Signed: Ashni Kumar, Dinajda Dollani, Anya Patel

#### **February 3 - February 9, 2025**

##### **Issues**

There are no issues to report.

##### **Activity Report**


Signed: Ashni Kumar, Anya Patel, Dinajda Dollani


#### **January 27 - February 2, 2025**

##### **Issues**

There are no issues to report.

##### **Activity Report**

Signed: Dinajda Dollani, Anya Patel, Ashni Kumar