# Summary for Week 7

## Capucine 
This week I started by adding an ageRange field to the User so that it could be used for the matching algorithm. I also did a bit of refactoring for the tests. 
And then I’ve created the UI to play audio records in the profile page.

It took me a bit of time to work out how all the audio and record parts worked, but thankfully the code was very well documented!

Next sprint, I’ll probably work on linking some parts of the app with the database. 

## Çağın (Scrum Master) 
This week, I completed the chat backend and the UI. It took a lot because it kind of combined work of two weeks. Unfortunately I couldn’t add tests as we are still not sure how to test Realtime Database usages. However we tested between team members and everything went smoothly.

For next week, I don’t have anything in mind to continue but we sure need to find a way to test Firebase usages so I might work on that.

## Tibor
I cleaned and finished testing for the user map and the permissions.

I think I worked around 8 hours, however I had to wait for the Location service pull request to be merged before I could merge. I think we started this task too early for its dependencies on other tasks.

Next sprint, I will add the live position of other users on the map.

## Kilian
This week, I looked for ways to mock the database for my tests and future ones. I mostly read web pages, trying to find something that worked, but I couldn’t find it. We might try something using Cirrus, but the Firebase emulator seems really complex to deal with.

I managed my time pretty well, but didn’t code that much. I spent some hours trying to find how the Firebase emulator works, but didn’t find anything that helped me. After having discussed with the team and with the scrum master (Çağın), we decided it would be better for me to continue working on the matching algorithm, as I felt frustrated at not being able to deliver anything for this sprint.

Next sprint, I will finish my work on the matching algorithm (and hopefully test it enough without using Firebase emulator). I think that we have some work to do on linking the UI elements to our database.

## Melissa
This week, I finished what I started last week, namely putting the recordings into the Firebase storage. I was also able to find a way to refactor the swiping activity into the corresponding fragment, but it isn’t merged yet

It took me a looooong time because I couldn’t figure out how to add an audio file into the Firestore, and when I finally realised I couldn’t do this and that I had to use the storage instead, then it took me another looong time to discover that the .3gp extension we were using couldn’t be read on web navigator. So I had to change the extension and find another one that kept a good enough quality. But then everything worked out fine and I was able to merge it. Finding a way to refactor the swiping activity was pretty easy compared to the first task, and it only took me 1 hour to do it

For next week, I will begin another task keeping in mind that I shouldn’t be working 16 hours on the project.

## Richard
This week I added the location to the database. Some prototype was already there but I had to find a way to make it serializable. The easiest way was to juste store latitude and longitude. I adjusted the type of the matches from users to strings. When these tasks were done I had to modify some tests to make them all pass. 

Merging my part was a bit tricky because the order in which we merge our parts was not the better one. 

Next week I can pick a new task that will probably be to connect the database to parts of our app that are not already connected.

## Overall Team
We might have been a bit ambitious with the tasks, even though a lot of work is done and merged we still have many pull requests waiting for reviews. They should be merged pretty quickly though. The communication were great at out meetings. For next weeks, we should concentrate on completing the requirements, link UI and the database and find a good way to test Firebase usage.
