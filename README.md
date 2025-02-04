# nitpick2

WeThinkCode's grading support system for students and staff.

To get to an installed version of the code using a local box.

1. Install conveyor.
2. ./gradlew clean jar
3. cd vnitpick
4. conveyor run # to see it run w/o installing it.
5. conveyor make site
6. use the ServerKt run configuration
7. hit the site "http://localhost:3000/download.html"

Todos
-----

[ ] Try the 7 steps above on a Pop box.
[ ] Try the 7 steps above on a Mac box.
[ ] Jiggle conveyor settings to release as a github release.
[X] Make the nitpick project do a fat jar so we can move it to a server.
[X] Install a jltk sample in the testData folder.
[ ] Add MRU support and standard folders for student and author projects.
[ ] Use native file dialogs, or customize native file dialogs?
[X] flow isn't using the same test folder arrangement as the others.

