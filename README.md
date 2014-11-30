I went through Hello, Android book

Unfortunately in Prefs class, the method onCreate() is calling addPreferencesFromResource(), which do not work 
as PreferenceActivity do not support this method any more. Instead must be extends(ed) to PreferenceFragment,
but I have not found out how it works yet. I may come back to this issue later on.

Afer a while I was able to add Preference Activity, However the issue that I am having is to go back to main activity.
When app is on Preference activity setting screen (setting.xml) it does not go back to main Activity (activity_sudoku.xml)

David 

