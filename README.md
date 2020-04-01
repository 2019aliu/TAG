# TAG
A tracking device that helps you keep track of your items, no matter where you are.

## How is this Android app structured?
*As of 04/01/20* (i.e. some activities that should be included aren't included yet), 10:10 AM, the app is structured as follows:
* Main Activity: You can think of this as the home screen, or the screen that shows up when you press the app
* Activities related to finding a device:
  * ListItemsActivity: Lists all devices the user owns
  * GPSFindActivity: Pulls up a Google Maps and locates you and the device selected
  * BTWifiActivity: Helps users turn on Bluetooth and Wifi
  * CloseRangeActivity: Finds the device using Bluetooth and Wifi signals
* Activities related to registering the device:
  * RegisterActivity: Allows user to fill out name and description of device, then registers the device using bluetooth and Wifi.

Future activities to include:
- [ ] EditItemActivity: Activity to edit the item properties, can be similar to RegisterActivity
- [ ] MyItemActivity: Has a page that links to EditItemActivity as well as GPSFindActivity

Going forward, however, we do want a different structure. A lot of it will be borrowed from our old user interface (UI). This is as follows:
- [ ] MainActivity: this should be a list of all items. Lazy loading (which only loads a few elements at a time) is something for the future
- [ ] SettingsActivity: change what the app should use to register the app
- [ ] Activities for find (this should be mostly the same as above)
  - [ ] GPSFindActivity: Finds using GPS
  - [ ] BTWifiActivity: Helps user turn on Bluetooth/Wifi
  - [ ] CloseRangeActivity: Finds the device using Bluetooth/Wifi signals (whatever the user chooses)
- [ ] MyItemActivity: Exactly the MyItemActivity listed above (Has a page that links to EditItemActivity as well as GPSFindActivity)
- [ ] EditItemActivity: Exactly the EditItemACtivity list above (Activity to edit the item properties, can be similar to RegisterActivity)
- [ ] LoginActivity
- [ ] SignupActivity

For signup and login, Android does have the feature of using the phone's built-in password (so like eye scanning, fingerprint, etc.) to login, but that requires a recent version of Android (Android API Level 27), so we will probably be ignoring that feature for now

(There are no pictures at the moment to help describe things, so if you need help, give the group a heads up with any questions you have!)

## Basics on Android Development

### Fundamentals
From Alex's understanding, Android apps consist of a bunch of screens, known as activities, which are displayed using Extensible Markup Language (XML), and some code to help the activities to do cool things. That code is usually written in Java or Kotlin, and will be Java for this app because we all know Java.

### Activities
Activities in Android apps are written using XML. Take a look at some sample XML code (this is from our close-range detection activity at them moment):
```XML
<LinearLayout
    android:id="@+id/powerCircle"
    android:layout_width="200dp"
    android:layout_height="200dp"
    android:layout_gravity="center"
    android:layout_marginTop="144dp"
    android:background="@drawable/circle"
    android:orientation="vertical"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fontFamily="sans-serif-light"
        android:gravity="center"
        android:text="@string/closerange_cold"
        android:textColor="@color/offwhite"
        android:textSize="24sp" />

</LinearLayout>

```

The syntax for putting any element in XML in Android is the following:
```XML
<ELEMENT
         android:PROPERTY_UNO="VALUE_UNO"
         android:PROPERTY_DOS="VALUE_DOS"
         android:PROPERTY_TRES="VALUE_TRES">
  <SUBELEMENT_UNO
         android:PROPERTY_UNO="VALUE_UNO"
         android:PROPERTY_DOS="VALUE_DOS"
         android:PROPERTY_TRES="VALUE_TRES">
<!--   Here's a comment, but keep in mind adding comments for properties doesn't work  -->
<!--   You can add stuff inside subelements too and so on, you get the point   -->
  </SUBELEMENT_UNO>
<!--   Some elements don't have anything inside them, so you can format them as the follows: -->
  <SUBELEMNT_DOS
         android:PROPERTY_UNO="VALUE_UNO"
         android:PROPERTY_DOS="VALUE_DOS"
         android:PROPERTY_TRES="VALUE_TRES"/>
</ELEMNENT>
```

There are many different elemnents you can use, but the fundamental idea is:
* Each activity in Android has a layout, whether that would be a LinearLayout or ConstraintLayout
  * *LinearLayout*: Layout that lists things in a linear order, so everything is neatly stacks on top of one another. Can be oriented horizontally or vertically. Easy to use but not a whole lot of customizability
  * *ConstraintLayout*: Layout that defined based on how far apart things should be. This layout is a bit harder to use, but Android Studio does a decent job with the graphical interface. You can find this by going to your design, clicking the DESIGN tab (if it isn't selected already), changing the layout to a ConstraintLayout, and then clicking on any element. There will be a grid with four directions representing the constraints for the corresponding bounds.
* Layouts are used to contain other elements
  * *Views*: views allow you to structure elements in a certain way, kinda like layouts but a bit different in nature. Some examples are RecyclerView (which gives you a list), CardView (gives you cards, deprecated as of the latest version of Android though :disappointed:). One view that'll be useful is TextView, which is basically a textbox.
  * *Buttons*: exactly what it sounds like; its a button. It can be pressed, and you can change what happens when you press the button by setting an OnClickListener (more on that later). Some special buttons I want to point out are: FloatingActionButtons, which are the little buttons in the bottom right corner in our app. Those are just nicely formatted buttons. Also, ImageButtons allow you to put images inside the buttons, but be careful with the sizing of the imaging. Alex used 'fitCenter' for the `scaleType` option.
  
**A few notes**: 
* In order to reference any element, you can add the `id` option to the element (anything in Android will have an `id` option, so you should be able to change it). If you choose to type it out, be sure to add `@+id` so you can reference it in your code. For example, if I wanted to name a button "finishButton", I would type; `android:id="@+id/finishButton"`
* Text should be put into strings.xml. Android Studio helps you with this, but in general, you should keep all text you're going to put into TextViews, Buttons, etc. into strings.xml. That file can be found under: *res > values > strings/xml*. The format for adding a string to strings.xml is: `<string name="action_settings">Settings</string>`

### Adding magic (pizazz, whatever) to your activities
Adding cool features to your elements requires some code, whether it'd be as simple as printing out a message to the console log or displaying a message, to changing colors based on RSSI. We are using Java, specifically Java 8. IMPORTANT: please install at least Java 8, if not a later version of Java. We're using Java 8-specific features, so its important that you install Java 8

If you right click in the project menu and choose *"New > Activity"*, Android Studio should give you a list of activities to choose from. Select the one you want to start with, go through the popup window (NAME YOUR ACTIVITY SomethingSomethingActivity, BECAUSE THAT MAKES LIFE EASIER) and Android Studio should generate the XML and Java for you.

Inside the auto-generated Java file, you should find something like this:

```java

// some import statements
public class YourActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourActivity);
    }
}
```

In order to refer to an element, you must first inflate it in your code (so Java code), which is a fancy name for instantiating the element. For example, for an activity called BTWifiActivity, is partly shown below:

```XML
<!-- Some code here not shown that sets up a ConstraintLayout -->

<LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center">

    <Button
        android:id="@+id/button_BTWifiSettings"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="20dp"
        android:text="@string/btwifi_settings"
        android:textSize="12sp"
        android:textStyle="bold"
        android:theme="@style/AppTheme.Button" />

    <Button
        android:id="@+id/button_BTWifiContinue"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="20dp"
        android:text="@string/btwifi_checked"
        android:textSize="12sp"
        android:textStyle="bold"
        android:theme="@style/AppTheme.Button" />

</LinearLayout>

```

If I wanted to make the buttons do something, for the corresponding code in Java, I would inflate the buttons as the following:

```java
// import statements here, let Android Studio handle that

public class BTWifiActivity extends AppCompatActivity {

    // it is good practice to make the elements private to the activity that it is inside
    private Button mSettingsButton;
    private Button mContinueButton;
    
    // the following four lines are automatically set up when you create a new Activity using the right-click menu that Android Studio provides.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btwifi);
        
        // Here is the code to inflate the two buttons declared above.
        // To get an element by its id, use the "findViewById" method.
        // R.id contains all ids that your project has (see example below)
        // (kinda) fun fact: R stands for resources
        mSettingsButton = (Button) findViewById(R.id.button_BTWifiSettings);
        mContinueButton = (Button) findViewById(R.id.button_BTWifiContinue);
    }
}
```

In order to make buttons do something, you need to add a View.OnClickListener. This is done as follows:
```java

// this is put inside the onCreate method of Android.
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                //pass any variables in here using .putExtra(), most likely user information
                startActivity(settingsIntent);
            }
        });

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager mWifiManager = (WifiManager)
                        getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mWifiManager.isWifiEnabled() && mBluetoothAdapter == null) {
                    Snackbar.make(v, "Device does not support bluetooth. Please turn on Wifi", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (mWifiManager.isWifiEnabled() || mBluetoothAdapter.isEnabled()) {
                    Snackbar.make(v, "Signal enabled. Searching...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent closeRangeIntent = new Intent(BTWifiActivity.this, CloseRangeActivity.class);
                    startActivity(closeRangeIntent);
                } else {
                    Snackbar.make(v, "Please turn on bluetooth or Wifi, or both", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
```

This document will describe other features as needed.

IMPORTANT: if something isn't imported, click the red lightbulb, and then click "Import class". Alternatively, on a Mac, you can press OPTION + RETURN to bring up the menu. If there are multiple options, look up which one is the one you want, or shoot the group chat a message.

Well that's all! This was a bit lengthy, but hopefully this clears up a few things.

If you have any questions, feel free to ask!
