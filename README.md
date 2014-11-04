I went through Hello, Android book

Unfortunately in Prefs class, the method onCreate() is calling addPreferencesFromResource(), which do not work 
as PreferenceActivity do not support this method any more. Instead must be extends(ed) to PreferenceFragment,
but I have not found out how it works yet. I may come back to this issue later on.

David 

