# Summary for Week 6

## Capucine (Scrum Master) 
This week I made it so that the user has to login before continuing to the other parts of the app. 
But also if the current user doesn’t log out and closes the app, it will remember them the next time they launch it.

Since I spent a lot of time on the app last sprint, this one was lighter.

Next week, it would be great if we started linking the other parts of our app with the database.

## Çağın
This week I advanced on the implementation of chat features. Now we can send and also receive the messages to and from Realtime Database. 
We had separated the task for chat as backend and UI however, for chat it was more coupled than I thought. So I had to work on the UI as well.

I also added documentation to my code.

For next week I’ll try to finish the chat in a way we can use it with matches however it will require further changes as it depends on the matching implementation.
We need to figure out how to test all of these as many of us depend on Firebase for the latest tasks.

## Tibor
I continued to work on the user map, however I was very busy and couldn't complete it and merge it. Notably given the changes on the main screen. 
I also continued to look for a better CI, I tried to use Google Computing Engine free tier to run cirrus on. However it doesn't give a lot of speed up 
(around 1 minute less out of 15). So I didn't merge that.

I think I spent around 6 hours in total.

Next week, I really must be able to merge the user map and to comment my previous code, as it has been a long time since I haven't merged anything.

## Kilian
This week, I documented all the code I’ve written so far and started working on the matching algorithm. 
I’ve written a class that uses user profiles in the database to find other users that can be shown in the swiping interface.

My task will definitely take longer than expected because I already spent 8 hours on it and I couldn’t test my code. 
It requires me to mock our firebase firestore database, and I honestly couldn’t find a working way to do this for the moment. 

Next week, I’ll figure out how to mock our database (which will be really useful for others too) and test my code.

## Melissa
For this sprint, I had two tasks. I started by fixing remaining bugs in the recording activity. The bug correction didn’t take me long at all, 
maybe 1h for the 3 fixes, but finding a way to test the fixes was quite painful. I tried dozens of things and it took me ~4h to finally find something working 
and write the tests. I also started working on adding the recording file to the firestore, and I encountered a few problems, so I haven’t got the time to finish it.

I still have trouble estimating the time each task will take, especially the time it takes to test, which eventually means I can’t finish my task for the end of the sprint. 

For next sprint, I’ll obviously finish incorporating the recording into firestore and maybe I’ll try linking the settings with the profile and the firebase.

## Richard
This week I continued to work on the march UI. The match activity works fine. I tested all what I did and it is all ok. I had some troubles with the RecyclerView 
I use because I wanted to test views in it but didn’t find how to get it. Thanks to Mélissa I found a way and could write tests.

I documented all the code I wrote from the last 5 weeks.

For the next sprint, I can pick a new task to work on it. 

## Overall Team
We might have seen a bit too big when we assigned the tasks last week, so while we made a lot of progress, we weren't able to finish everything.
We also set out time aside to document our code, which was definitely a good thing to do!

The communication was good and we had two stand up meetings of about 20-30 minutes each.

For the next sprint it would be really great if we emptied the sprint backlog, so that only one requirement for the app would be missing 
(the local cache for the offline mode).
