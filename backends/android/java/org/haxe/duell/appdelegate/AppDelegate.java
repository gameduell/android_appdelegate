/*
 * Copyright (c) 2003-2015 GameDuell GmbH, All Rights Reserved
 * This document is strictly confidential and sole property of GameDuell GmbH, Berlin, Germany
 */
package org.haxe.duell.appdelegate;

import android.content.Intent;
import android.os.Bundle;

import org.haxe.duell.hxjni.HaxeObject;
import org.haxe.duell.Extension;

/**
 * @author jxav
 */
public class AppDelegate extends Extension
{
    public static HaxeObject haxeAppDelegate = null;

    public static void initialize(HaxeObject obj)
    {
        haxeAppDelegate = obj;
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional data
     * from it.
     */
    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data)
    {
        return false;
    }

    /**
     * Called when the activity is starting.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        if (haxeAppDelegate != null)
        {
            haxeAppDelegate.call0("create");
        }
    }

    /**
     * Perform any final cleanup before an activity is destroyed.
     */
    @Override
    public void onDestroy()
    {
        if (haxeAppDelegate != null)
        {
            haxeAppDelegate.call0("destroy");
        }
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into
     * the background, but has not (yet) been killed.
     */
    @Override
    public void onPause()
    {
        if (haxeAppDelegate != null)
        {
            haxeAppDelegate.call0("pause");
        }
    }

    /**
     * Called after {@link #onStop} when the current activity is being
     * re-displayed to the user (the user has navigated back to it).
     */
    @Override
    public void onRestart()
    {
        if (haxeAppDelegate != null)
        {
            haxeAppDelegate.call0("restart");
        }
    }

    /**
     * Called after {@link #onRestart}, or {@link #onPause}, for your activity
     * to start interacting with the user.
     */
    @Override
    public void onResume()
    {
        if (haxeAppDelegate != null)
        {
            haxeAppDelegate.call0("resume");
        }
    }

    /**
     * Called after {@link #onCreate} &mdash; or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.
     */
    @Override
    public void onStart()
    {
        if (haxeAppDelegate != null)
        {
            haxeAppDelegate.call0("start");
        }
    }

    /**
     * Called when the activity is no longer visible to the user, because
     * another activity has been resumed and is covering this one.
     */
    @Override
    public void onStop()
    {
        if (haxeAppDelegate != null)
        {
            haxeAppDelegate.call0("stop");
        }
    }

    @Override
    public void onLowMemory()
    {
        if (haxeAppDelegate != null)
        {
            haxeAppDelegate.call0("lowMemory");
        }
    }

    @Override
    public void onTrimMemory(int level)
    {
        if (haxeAppDelegate != null)
        {
            haxeAppDelegate.call1("trimMemory", level);
        }
    }
}
