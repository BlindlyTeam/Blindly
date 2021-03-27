# SDP Team 3

[![Build Status](https://api.cirrus-ci.com/github/BlindlyTeam/Blindly.svg)](https://cirrus-ci.com/github/BlindlyTeam/Blindly)
[![Maintainability](https://api.codeclimate.com/v1/badges/a99a88d28ad37a79dbf6/maintainability)](https://codeclimate.com/github/codeclimate/codeclimate/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/a99a88d28ad37a79dbf6/test_coverage)](https://codeclimate.com/github/codeclimate/codeclimate/test_coverage)



## Team / App name:
Blindly

## Description:
Blindly is a dating app designed to meet new people. No profile pictures are allowed here, the app relies solely on voice. After all, love is blind! Once you’ve matched with someone,  you can go ahead and text them.        

## Requirements:
### Split app model:
For our app we will use Google Firebase. The main usages will be to store audio recordings of users and the metadata of users themselves.

### Sensor usage:
We will use the GPS to search for people around the user, so that they can match if they both like each other. Given the concept of our app, we will use the microphone to record audio files that can be listened to by the others.

### User support:
Users will have to login with the Google built-in authentication in order to use the app. Once they’re logged in they can fill their profile with basic information about themselves, interests and voice memos to present themselves.

### Local cache:
The messages the user sends to other users are cached in the app.
A list of matched profiles is also stored and a limited number of non matched profiles for offline use too.

### Offline mode:
While offline, the user will be able to match the profiles in cache, view previously matched profiles and see their conversations. They will also be able to write new messages that will be sent once a network connection is available.

