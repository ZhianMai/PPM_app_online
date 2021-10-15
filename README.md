# PPM_app_online

Demo: https://tinyurl.com/kz3a9fd5

Heroku may hibernate the app if no request for several days, so the web page loading time may be longer than usual.

 - Frontend: React;
 - Backend: Spring Framework (Boot, Security with JWT, JPA), MySQL.

Original source: https://github.com/AgileIntelligence/AgileIntPPMTool

My contributions:
- Refactor some stateless classes to util (static) class to avoid unnecessary bean creations.
- Add project task due data format check to avoid date conflict when scheduling.
  - The backend would check the task due day if it conflicts to any existing project due day;
  - If a conflict is found, the backend would throw an exception and return 400 with error message to the frontend;
  - The frontend would read the 400 and indicates the user to schedule another day.
- Added JSON format restrictions on Project class (@Entity).
- The project task board would display the project name on the top.
- The project board would display the due day on project information widget.
- Some improvements on UI design.
