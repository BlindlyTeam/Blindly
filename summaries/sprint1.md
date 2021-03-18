# Summary for Week 1

## Capucine
This week, I created the artistic direction of our app (the logo, colour palettes, etc.). 

While I didn't spend much time writing code, it allowed us to have a clearer idea of what our app and its UI would look like.

Next week, I'll try to pick a more coding intensive task such as helping with the UI implementation of the profile, or starting to implement the chat feature.
 

## Richard
This week I implemented the Map UI to see where the user would be. And tried to implement the location service.

This took me way more time than expected because the location service doesn’t work for now and I didn't have time to test it. The task I took should have been split among multiple smaller tasks and that would make it easier.

I’ll try to finish it for next time, just some permission issues to deal with and testing to do.


## Kilian
I implemented an activity to record an audio file from the device’s microphone. For convenience, I saved it in an external cache and added a way to listen to it. It seems to work for now. The UI is basic, but it will surely evolve.

Testing remains unclear for me, for two main reasons: it is impossible to record audio with the Android Emulator, and the libraries that I used are hard to test. However, it seems that my basic UI tests have 84% coverage.

For next week, I need to test everything in detail so that it works correctly. I’ll try to write less code before starting to test it, as I didn’t have time to test everything I wrote this week.


## Melissa
I added ideas in the product backlog, updated the project board and tested the audio recording / playing on my phone to help Kilian. 

I couldn’t work on my task this week because we underestimated the time needed to implement the different tasks, so I couldn’t link all the corresponding activities together.

Next week I’ll make sure to assign myself to a task that does not depend from other tasks so that I’m not held back.


## Tibor
I implemented sign-up and log in with Google Firebase, it includes authentication using either a phone number, a password and an email or a Google account.

I still couldn't test the process as it needs to either emulate Google Firebase, which is feasible but cumbersome to set up or to use a mock helper class, leaving some rather important code untested. Due to this I couldn’t push to main.

For next week, I will write comprehensive tests of the authentication process.


## Çağın (Scrum Master)
I've implemented the user interface for profile settings. I've also added some tests in order to test the firing of intents. 

These took much more than I anticipated, mostly because I wasn't accustomed to Android Studio interface or testing in Android. Hopefully this acclimatization phase will take less time in future sprints.

Next week, I need to fine tune checks on user inputs, write more tests. We need to think about how to bind user inputs to our database as well.



## Overall Team
We made good progress in the first week, even though not all work is merged to main branch due to some testing concerns, they are mostly correct and complete in terms of functioning. Our scheduled two standup meetings were a success as we updated nicely what we have done and what we intend to do in our allocated time slot. 

For future sprints we need to better estimate workload and balance the work in the team. The tasks could have been divided to smaller tasks not to overcharge work but these were totally understandable issues of first sprint.


