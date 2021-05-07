# Summary for Week 8

## Kilian
This sprint, I finished the Matching Algorithm by adding reverse checks and testing it as much as I could. I also added a likes field in the database, which stores the UIDs of the liked users. Finally, I managed directories, formatted our code and added some missing documentation.

I worked slightly less on coding new features this week, but focused more on reviewing code and refactoring. I managed my time pretty well, and could even reformat the code and add some documentation (which wasn’t stated in my task).

Next week, I think we need to work on the destabilization task. Moreover, we still need to link our UI to the database, so that we can start chatting with matches and see them on the map.


## Capucine
During this sprint, I linked the settings to the database. While the retrieving the information part was pretty quick, as a lot was already in place, the updating on the database part was a bit longer to implement and to test. I also managed to refactor the RecordingActivity so that it could be called everywhere but did not have the time to test it thoroughly (that's why it’s still a draft).

While I did manage to divide the task a bit better, I should have created one more pull request to divide it a bit more equally. 

Next week, I’ll need to finish the tests for the RecordingActivity, and then I’ll probably work on linking other parts of the app to the database, or the destabilising task.


## Tibor
At the beginning of the spring I had to go around a bug in one of the libraries we use, it took me a lot of time. The bug was introduced by my changes of last week, however it occurred rarely which let us merge with the tests passing. Afterward, I added the possibility to see another user position live on the maps, I took the opportunity to refactor the code for the live database. I made it more testable, so we could go back again above the 80% bar for the test coverage.

I think I managed to work sufficiently in advance so that the process of merging went smoothly for me and my teammates. I also think I improved on my communication issues. I however should maybe have split my pull request in two.

For next week, I am free 


## Çağın
This week I started off by fixing a small bug that caused repeating the profile setup. Now it prompts a message to quit the app or not. Otherwise I spent a lot of time on finding a way to integrate Android Emulator. Unfortunately, my numerous attempts were not successful. 

For next week, I can start a new task to complete a missing part of our app or the destabilising task, we’ll see.


## Richard
This week I worked on the local database implementation. I will use room to create a local database for our app. I read a lot of documentation and began to implement it. I created all the classes needed for the database but had some troubles with the entities (user in our case) that must have a public constructor. We have a builder instead so I have to find another way to store the user.

I didn't have much time at the beginning of the week and I only worked on Wednesday and Thursday.

Next week I will continue to work on the local database, test it and finish it. We just need to bind it to all the activities that need the local database.


## Melissa (Scrum Master):
This week I replaced the hardcoded profiles from the swiping activity with actual profiles from the database, using the algorithm Kilian created. I also changed the interface to display more informations about the user and the possibility to listen to the audio on the card directly. I was able to separate my work into 2 differents tasks, fetching user and then adapt the interface, and the first one is already implemented and merged with the main branch.

I had a few problems with coroutines as it was a new concept for me, understanding how to launch them and how to get back to the main coroutine, but with some documentation I understood and was able to do the work. I couldn’t merge my second task (the adaptation of the user interface) because of lack of coverage and bad time management. I couldn’t find a way to get more than 21% coverage so I didn’t merge it with the main branch.

For next week, I’ll find a way to test, and I’ll also make sure to correct any bug that is left with the audio player on the card.


## Overall Team
We had our 2 meetings like every week, they went very well and didn't go over the 15 minutes mark. We always improve communication-wise, helping each other individually and globally when problems arise.
We still struggle to manage our time, especially with the testing parts. This week, we still have some PRs pending. However, the app made really good progress overall and we feel like we are getting somewhere !