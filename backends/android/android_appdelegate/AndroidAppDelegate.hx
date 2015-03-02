/*
 * Copyright (c) 2003-2015 GameDuell GmbH, All Rights Reserved
 * This document is strictly confidential and sole property of GameDuell GmbH, Berlin, Germany
 */
package android_appdelegate;

import hxjni.JNI;
import msignal.Signal;

/**
    @author jxav
 */
class AndroidAppDelegate
{
    private static var initializeNative = JNI.createStaticMethod("org/haxe/duell/appdelegate/AppDelegate",
        "initialize", "(Lorg/haxe/duell/hxjni/HaxeObject;)V");

    // in reality this is a "forced" singleton, since we need to have the static initialization and not the lazy loading
    private static var appDelegateInstance: AndroidAppDelegate = new AndroidAppDelegate();

    public var onCreate(default, null): Signal0;
    public var onDestroy(default, null): Signal0;
    public var onPause(default, null): Signal0;
    public var onRestart(default, null): Signal0;
    public var onResume(default, null): Signal0;
    public var onStart(default, null): Signal0;
    public var onStop(default, null): Signal0;
    public var onLowMemory(default, null): Signal0;
	public var onTrimMemory(default, null): Signal1<Int>;

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
	}

	//
    // Java callbacks
    //

    public function create(): Void
    {
        onCreate.dispatch();
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
}