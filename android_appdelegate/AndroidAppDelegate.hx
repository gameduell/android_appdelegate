/*
 * Copyright (c) 2003-2015, GameDuell GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package android_appdelegate;

import hxjni.JNI;
import msignal.Signal;

/**
    Wraps the logic of the Android activity lifecycle callbacks with signals that the user can bind listeners to.
 */
@:keep
class AndroidAppDelegate
{
    private static var initializeNative = JNI.createStaticMethod("org/haxe/duell/appdelegate/AppDelegate",
    "initialize", "(Lorg/haxe/duell/hxjni/HaxeObject;)V");

    private static var openURLNative = JNI.createStaticMethod("org/haxe/duell/appdelegate/AppDelegate",
    "openURLNative", "(Ljava/lang/String;)Z");


    // in reality this is a "forced" singleton, since we need to have the static initialization and not the lazy loading
    private static var appDelegateInstance: AndroidAppDelegate = new AndroidAppDelegate();

    /**
        Called when the activity is starting.
     */
    public var onCreate(default, null): Signal0;

    /**
        Perform any final cleanup before an activity is destroyed.
     */
    public var onDestroy(default, null): Signal0;

    /**
        Called as part of the activity lifecycle when an activity is going into the background, but has not (yet) been
        killed.
     */
    public var onPause(default, null): Signal0;

    /**
        Called after `onStop` when the current activity is being re-displayed to the user (the user has navigated back
        to it).
     */
    public var onRestart(default, null): Signal0;

    /**
        Called after `onRestart`, or `onPause`, for your activity to start interacting with the user.
     */
    public var onResume(default, null): Signal0;

    /**
        Called after `onCreate` or after `onRestart` when the activity had been stopped, but is now again being
        displayed to the user.
     */
    public var onStart(default, null): Signal0;

    /**
        Called when the activity is no longer visible to the user, because another activity has been resumed and is
        covering this one.
     */
    public var onStop(default, null): Signal0;

    /**
        This is called when the overall system is running low on memory, and actively running processes should trim
        their memory usage. While the exact point at which this will be called is not defined, generally it will happen
        when all background process have been killed. That is, before reaching the point of killing processes hosting
        service and foreground UI that we would like to avoid killing.
     */
    public var onLowMemory(default, null): Signal0;

    /**
        Called when the operating system has determined that it is a good time for a process to trim unneeded memory
        from its process. This will happen for example when it goes in the background and there is not enough memory to
        keep as many background processes running as desired. You should never compare to exact values of the level,
        since new intermediate values may be added -- you will typically want to compare if the value is greater or
        equal to a level you are interested in.

        Refer to: http://developer.android.com/reference/android/content/ComponentCallbacks2.html#onTrimMemory(int).
     */
    public var onTrimMemory(default, null): Signal1<Int>;

    /**
        Called when the activity has detected the user's press of the back key.
     */
    public var onBackPressed(default, null): Signal0;

    /**
      * If the app was opened from another app, this property stores the url used to open this app.
      * The field is null if the app was opened normally.
      * Put this into the android platform config of your duell_project.xml
      *
      *       <manifest-main-activity-intent-filter-section>

                    <action android:name="android.intent.action.VIEW" />
                    <category android:name="android.intent.category.DEFAULT" />
                    <category android:name="android.intent.category.BROWSABLE" />
                    <!-- Accepts URIs that begin with "http://www.example.com/gizmos” -->
                    <data android:scheme="http"
                          android:host="www.example.com"
                          android:pathPrefix="/haxe" />
                    <!-- note that the leading "/" is required for pathPrefix-->
                    <!-- Accepts URIs that begin with "example://gizmos” -->
                    <data android:scheme="example"
                          android:host="haxe" />

            </manifest-main-activity-intent-filter-section>
      *
    */
    public var lastURLFromApplicationOpening(default, null): String = null;

    /**
      * Dispatched when the app is opened from another app.
    **/
    public var onApplicationDidOpenWithURL(default, null): Signal0;

    /**
        Retrieves the instance of the app delegate.
     */
    public static inline function instance(): AndroidAppDelegate
    {
        return appDelegateInstance;
    }

    private function new(): Void
    {
        initializeNative(this);

        onCreate = new Signal0();
        onDestroy = new Signal0();
        onPause = new Signal0();
        onRestart = new Signal0();
        onResume = new Signal0();
        onStart = new Signal0();
        onStop = new Signal0();
        onTrimMemory = new Signal1<Int>();
        onLowMemory = new Signal0();
        onBackPressed = new Signal0();
        onApplicationDidOpenWithURL = new Signal0();
    }
    //
    // native calls
    //
    public function openURL(url:String): Bool
    {
        return openURLNative(url);
    }

    //
    // Java callbacks
    //

    public function create(): Void
    {
        onCreate.dispatch();
    }

    private function assign_applicationOpeningURL(url: String): Void
    {
        lastURLFromApplicationOpening = url;
        onApplicationDidOpenWithURL.dispatch();
    }

    public function destroy(): Void
    {
        onDestroy.dispatch();
    }

    public function pause(): Void
    {
        onPause.dispatch();
    }

    public function restart(): Void
    {
        onRestart.dispatch();
    }

    public function resume(): Void
    {
        onResume.dispatch();
    }

    public function start(): Void
    {
        onStart.dispatch();
    }

    public function stop(): Void
    {
        onStop.dispatch();
    }

    public function lowMemory(): Void
    {
        onLowMemory.dispatch();
    }

    public function trimMemory(level: Int): Void
    {
        onTrimMemory.dispatch(level);
    }

    public function backPressed(): Void
    {
        onBackPressed.dispatch();
    }

    public function getBackNumListeners(): Int
    {
        return onBackPressed.numListeners;
    }
}
