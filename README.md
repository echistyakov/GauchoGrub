# GauchoGrub
#### The Original GauchoGrub
##### *UCSB | CS48 | Winter 2015*
##### *G10: Melvin Nguyen, Zak Blake, Victor Porter, Evgeny Chistyakov, Eric Swenson*

### Android Application

To run the Android Application:

1. Clone the repo
2. Open GauchoGrub/GauchoGrubAndroid/ directory in Android Studio
3. Create an AVD (with level 16 or above API).
4. Launch the application in the emulator.

Special information for testing:

* The only seeded data for the Menu is for the 'Ortega' dining common.
* The app is only designed to be used in portrait mode, so far.
* The app has only been tested at a single DPI on Android Phones. 
* There are formatting and layout changes left to be made for the Menu and Schedule pages, but they contain the necessary content.
  
All pages can be viewed via the sidebar, accessed by swiping from the left edge of the screen or pressing the button at the top left corner of the screen. Select a dining common to view information relevant to that dining common, depending on the page you are at within the application.

### ASP.NET

To deploy locally:

1. Open GauchoGrub/GauchoGrubAzure/GauchoGrub.sln in Visual Studio (requires Azure SDK)
2. Launch locally.

The web application is deployed to http://gauchogrub.azurewebsites.net/. Public API documentation can be found under the API tab.
