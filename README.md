# Nanodegree: Stock Hawk
Part of Google's Nanodegree course: A stock reader application. Data are retrieved by querying Yahoo's Finance api, then kept locally using content provider. 
* Add new stock symbols to track
* Contains optional `Widget` that can used in your android device, showing the list of stocks you are tracking
* Contents are kept up to date using `GCM` running periodically (every hour). `Widget` gets refreshed as data comes in.
* Stock details also shows a simple charting graph (default to 10 days of data)
* RTL layout ready for translation work (i.e. Arabic)
* `Accessibility` is built into the application. For example, by enabling *Talkback* accessibility feature on your android phone and navigating through the application's charting graph, it will narrate a short summary on stock performance.

# Running The Application
Clone repo and setup your `GCM` [Google Developers](https://developers.google.com/mobile/add?platform=android). Ensure you have `google-services.json` on the same level as your `app/` folder.

# Libraries Used
* [Butterknife](http://jakewharton.github.io/butterknife/) for field and method binding for Android views.
* [okHttp](https://github.com/square/okhttp), HTTP client 
* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart), charting / graphing purpose

# Screenshots

