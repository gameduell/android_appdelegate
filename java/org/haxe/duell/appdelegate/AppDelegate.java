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

package org.haxe.duell.appdelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import java.lang.Exception;

import org.haxe.duell.DuellActivity;
import org.haxe.duell.Extension;
import org.haxe.duell.hxjni.HaxeObject;

public class AppDelegate extends Extension
{
    public static HaxeObject haxeAppDelegate = null;

    public static void initialize(HaxeObject obj)
    {
        haxeAppDelegate = obj;
    }

    public static boolean openURLNative(String url)
    {
        try
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            DuellActivity.getInstance().startActivity(browserIntent);
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * Switches the sleep mode on/off for the active window on the current UI Thread.
     *
     * @param disabled Sleep mode enabled with true or disabled with false
     * @return Bool {@code disabled} on success, otherwise false
     */
    public static boolean setScreenIdleTimerDisabledNative(final boolean disabled)
    {
        final DuellActivity activity = DuellActivity.getInstance();

        if (activity == null)
        {
            return false;
        }

        try
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    if (disabled)
                    {
                        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                    else
                    {
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
            });
        }
        catch(Exception e)
        {
            return false;
        }
        return disabled;
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

        handleNewIntent(DuellActivity.getInstance().getIntent());
    }

    protected void handleNewIntent(Intent intent)
    {
        String action = intent.getAction();
        String data = intent.getDataString();

        if (Intent.ACTION_VIEW.equals(action) && data != null)
        {
            if (haxeAppDelegate != null)
            {
                haxeAppDelegate.call1("assign_applicationOpeningURL", data);
            }
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

    @Override
    public void onBackPressed()
    {
        if (haxeAppDelegate != null)
        {
            if (haxeAppDelegate.callD0("getBackNumListeners") == 0)
            {
                DuellActivity.getInstance().defaultOnBack = true;
            }
            else
            {
                DuellActivity.getInstance().defaultOnBack = false;
                haxeAppDelegate.call0("backPressed");
            }
        }
    }
}
