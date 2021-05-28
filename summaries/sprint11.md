# Week 11

## Çağın
This week I had two PRs. One was a very small refactoring of Fragment titles. I’ve changed them to icons which give a better look and feel. Another one was the possibility to remove a user from MyMatches. I think they went fine generally and time estimations were good as well. 

There is a small design choice to whether a removed match should reappear in cards as a possible match. We should discuss this as a team and make the adjustment accordingly, this should be very fast. Apart from those I did reviews and also checked Tibor’s PR which gave nice possibilities to test our code that Kilian is working on. Kudos!

For next week, I believe we should really stop with feature addition because that would risk slipping of bugs into the final snapshot. We should focus on refactorings, bug fixes and making sure our coverage is above 80%.

## Capucine
This week I added a feature that enabled users to delete their account (once they’ve found their other half of course). We chose to remove the user from FirebaseAuth and his audio recording from Firebase Storage but leave them in Firestore and RealtimeDatabase. This way users don’t suddenly disappear from your matches, so you still have access to your chat history but not to their profile and map, you can also delete them manually thanks to Çağın. 

I unfortunately was not able to finish my other task which was to enable users to report each other on bad behaviour. But I was instead able to test more my first task, with the refactoring and the new interfaces Tibor created this week.

Next week, I believe we all have a lot of testing to do to try to increase our code coverage and also fix the last bugs.

## Kilian
This sprint, with the work of Tibor, I was finally able to test my part on the match profile. I could merge it and start working on adding more tests for MyMatch activity/class and User class. 

I was able to finish what I wanted in the estimated time, even if I didn’t find a way to test everything planned.

For the last sprint, I think we might need to try to find more tests to improve our coverage and fix the last bugs so that we are ready to release the app.

## Tibor (Scrum master)
This week I finished abstracting uses of Firebase away from our UI code, it leads us to a more modular and testsable codebase. I also fixed a bug with google authentication and reinforced our app sign-up process.

I was able to finish all the things I planned in approximately the right amount of time, except that I would have liked to benefit a bit more from the changes I made to add more tests.

For next week, I think it would be nice if I could work adding tests for the now-testable part of our app, especially since our coverage dropped a bit this week.

## Melissa
This week I fixed yet another bug we had on the cards and that I missed last week. Then I started to check how to implement the notification. I couldn’t finish them because I had problems with my computer, and also because a lot of the documentation about notifications is deprecated.

I wasn’t able to finish what I had to do, even though I spent more than 8 hours on this, but I made good progress and I’m confident I found a way to finish it.

For next week, I’ll finish the notifications.

## Richard
This week I finished connecting the local database to the app. It was a bit tricky after we merged Tibor’s work (abstraction of the userrepository) and had to redo what I’ve done. I had some troubles with the module to be injected but in the end it’s all good.

 I spent 8 hours this week working on my part but had some internet connection problems  that delayed my merging process. 

For the last week of the semester I have to find a fix some bugs for the demo.


## Overall
Everything went godd and our time estimations begin to be more accurate.
We also begin to see our whole project take form as one coherent and plesant app, which is very nice.
