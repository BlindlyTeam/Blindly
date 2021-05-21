# Week 10:

## Capucine:
This sprint went very well, until I made the decision to update Android Studio… And then everything went down in flames… While I was able to remove most of the @RequireAndroidAPI N and all of the one that required Android O, Cirrus fails all the previous tests that used to work. I also refactored the UI so that everything is coherent.

Next week, I’d like to be able to work again… And I still need to merge my pull requests but for some unknown reasons the coverage is really low, when all I did was remove @RequireAndroidAPI...


## Çağın:
This week I’ve continued on Tibor’s work, which was for the destabilization sprint. The location for the weather is now correctly set and I’ve added some UI elements to highlight the chosen day and the user can choose a day to add an event to the Google Calendar app.

The estimated time was accurate for this week until I had problems with testing on Cirrus. This is why the PR wasn’t ready in time.

Next week, I can work on some polishing part of the app, because I believe the main tasks are almost finished.


## Tibor:
This week I began to refactor our code depending on Firebase, into custom interfaces so that we minimize our dependencies on other libraries and have clean interfaces for the functionalities of our app. I however couldn't manage to finish as I had some problems with modules that depend on other modules.

For now I worked around the time I was supposed to (8hours), however I couldn't manage to finish so my estimation was a bit off.

Next week I will finish this task and can also take another medium task.


## Kilian (Scrum master): 
This week, I finished the match profile activity. You can now access your match’s information from “my matches” UI and from the chat. I had some trouble testing it and I still don’t understand why tests fail, but I know it works fine. Then, I updated Android Studio and a lot of tests started to fail for no reasons available.

I think I managed my time pretty well this week, but Android Studio and Cirrus decided to not cooperate. That’s why my task (and other ones) for the last sprint has not been merged yet.

Next week, I hope that we can fix everything so that it works fine again. Then, we’ll need to polish some features.


## Melissa:
This week I finished what I started last week. I linked the swiping with the likes in the database, and turned the likes into matches when they are mutual. I also added messages to warn the user when profiles are loading and when there is no more profile to show. Finally I found a way to load the recordings on the swipe cards correctly and added the possibility to play-pause the recordings. Finally, I made sure that the audio of the next card starts when you swipe the previous card, to create a more dynamic experience.

Testing my tasks was a problem, as I couldn’t find working ways to mock the other user on the card to test it efficiently, so my PRs made the coverage drop a little. I spent a little over 8 hours this week, but I compensated for last week.

For next week, I think I might try to test a bit further some things to make the coverage go up a bit.


## Richard:
This week, I tried to bind the Room local database to the rest of the app. I first created the modules for Hilt so that I can inject the local DB later in the code. I then tried to inject the DB in the activities but had some issues with the threads and the suspended function. I concluded that the injection of the local DB was at the wrong place and only injected it in the userRepo so that every modification on the user can be seen in the local DB too. I have some more problems with queries executing on the main thread but I will fix that for next week.

I had some troubles with android studio (because of the new update I guess) because he didn’t recognize some functions (that we didn’t write). 


## Overall:
This sprint, a part of us had to face problems, probably because of the Android Studio update. We had to deal with tests that didn’t pass anymore and it took a lot of time to figure out what we can do to fix this. We had good communication this week and were able to talk about it at the end of the sprint.

We still need to estimate time more precisely, but I think that we improved a little bit.

Next week, if we find a way to fix Cirrus, I think that we won’t have a lot of tasks left and it will mainly be about polishing the features that are already implemented.