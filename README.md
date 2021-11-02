# PPM_app_online

Demo: https://tinyurl.com/kz3a9fd5

Latest Dev Branch: https://github.com/ZhianMai/AgileintPPMTool/tree/branch55

Heroku may hibernate the app if no request for several days, so the web page loading time may be longer than usual.

 - Frontend: React;
 - Backend: Spring Framework (Boot, Security with JWT, JPA), MySQL.

Original source: https://github.com/AgileIntelligence/AgileIntPPMTool

### My Contributions:
- Refactored some stateless classes to util (static) class to avoid unnecessary bean creations. I personally don't like autowiring everything, because an autowired bean has unclear semantic: is it singleton or prototype?
- Added format check onproject task due date to avoid date conflict when scheduling.
  - The backend would check the task due day if it conflicts to any existing project due day;
  - If a conflict is found, the backend would throw an exception and return 400 with error message to the frontend;
  - The frontend would read the 400 and indicates the user to schedule another day.
- Added JSON format restrictions on Project class (@Entity).
- The project task board would display the project name on the top.
- The project board would display the due day on project information widget.
- Some improvements on UI design.

### Some Design Highlights:
- One project can have multiple project tasks (one-to-many), but project table and project task table are fully decoupled by using another table ("backlog") to link them. If in the future their relationship change to many-to-many, then it just needs to re-write backlog table logic instead of re-writing many logics.
- The controller model has no logic code, like input validation. Leave all logic to the service part to fully decouple modules.
- The server does not hold user logged-in session thanks to JWT.
