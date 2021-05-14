#Weak 9:

##Capucine:
This sprint I did the UI for the EditProfile page and then linked it to the database. I also refactored the RecordingActivity so that it could be called in the ProfilePage without needing a userBuilder.

As always the UI took a bit more time than estimated. But I was able to reuse a lot of code for the linking part so that went pretty quickly.

I’m not sure what I’ll do next week. We started on the destabilizing task, but we want to take it a bit further so maybe I’ll work on that.


##Çağın:
This sprint I worked on My Matches page which shows the user mutually liked other users. From there, a user can chat or see on map their match. For the adapter I could reuse some code that Kilian wrote which was pretty nicely integrated. Getting the matches from DB and passing them to the adapter was trickier as it required work with coroutines.

Again I had to spend too much time on testing but it was not successful. We had another PR where we didn’t focus too much on testing as it contained parts with DB but we started to risk again getting below coverage limit.

For the next couple of weeks, we might need to adapt parts of our code as interfaces to be able to test better, as TAs suggested, though full functionality of the app is always the priority.


##Tibor:
This sprint I hid the different secrets (open weather app and google services) from our git repo. I however discovered it was not necessary for google. I also did the weather service and activity for the upcoming date planner.

I spent too much time on this, especially given a completely unrelated test made our tests fail only on cirrus. As it was only intended to test code that will eventually be deleted, I just removed the test.

For the next week, I would be happy to work on fixing some linting warnings we have and improving our test coverage. For instance the tests of the user class.

##Kilian: 
This week, I first merged my work of last week (which took a long time because of conflicts). Then, I was asked to do another task: updating the user in the database. I added a UID field and removed the description. After this, I worked on my task of this sprint, which was adding UI to see the profile of a match.

I managed my time pretty well, but since I had to do other things this week, I couldn’t finish my part on profiles. I still need to test the viewmodel that I’m using for it.

Next sprint, I’ll finish my part on the profiles and I’ll link it with the work of Çağın on the chat.

##Melissa:
This week, I redesigned the swipe card to add the gender and the distance. Then I started to fix the audio recordings but I encountered more problems than I initially thought so I couldn’t finish fixing it. Also I got sick and I was unable to finish my tasks.

With my sick-day and other things I didn’t manage my time well this week, I will definitely work a bit more next week to make up for it

##Richard:
This week, I finished to setup room, the local database. I added some queries and tested it. All we have to do now is to store and retrieve the information at the right place. I worked early in the week so that I could take into accounts the comments of my teammates.

My time management was good. I worked at the beginning of the week and a little bit every day, it was better.

For next week I can do another task or refactor our code with the comments the TA’s made on our code review

##Overall:
This week we all tried to work pretty early, therefore we already had some advancements for the first standup meeting. The communication was very good and we could take into account the others’ comments. 
We had our 2 standup meetings like every week. 
