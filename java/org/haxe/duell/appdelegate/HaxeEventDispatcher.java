/*
 * Copyright (c) 2003-2016 GameDuell GmbH, All Rights Reserved
 * This document is strictly confidential and sole property of GameDuell GmbH, Berlin, Germany
 */

package org.haxe.duell.appdelegate;

import android.annotation.TargetApi;
import android.os.Build;

import org.haxe.duell.DuellActivity;
import org.haxe.duell.hxjni.HaxeObject;

import java.lang.ref.WeakReference;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.EnumMap;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class HaxeEventDispatcher implements Runnable
{
    public enum EventType
    {
        CREATE,
        DESTROY,
        ASSIGN_APPLICATION_OPENING_URL,
        PAUSE,
        RESTART,
        RESUME,
        START,
        STOP,
        LOW_MEMORY,
        TRIM_MEMORY,
        BACK_PRESSED
    }

    static class HaxeEvent
    {
        public EventType type;
        public Object data[];

        public HaxeEvent(EventType type, Object data[])
        {
            this.type = type;
            this.data = data;
        }
    }

    private final WeakReference<DuellActivity> activity;
    private final Deque<HaxeEvent> haxeEvents;
    private final HaxeObject haxeObject;

    private volatile boolean pendingQueueProcessing = false;

    private Map<EventType, String> haxeEventMap;

    public HaxeEventDispatcher(HaxeObject haxeObject)
    {
        this.haxeObject = haxeObject;

        haxeEventMap = new EnumMap<EventType, String>(EventType.class);

        haxeEventMap.put(EventType.CREATE, "create");
        haxeEventMap.put(EventType.DESTROY, "destroy");
        haxeEventMap.put(EventType.ASSIGN_APPLICATION_OPENING_URL, "assign_applicationOpeningURL");
        haxeEventMap.put(EventType.PAUSE, "pause");
        haxeEventMap.put(EventType.RESTART, "restart");
        haxeEventMap.put(EventType.RESUME, "resume");
        haxeEventMap.put(EventType.START, "start");
        haxeEventMap.put(EventType.STOP, "stop");
        haxeEventMap.put(EventType.LOW_MEMORY, "lowMemory");
        haxeEventMap.put(EventType.TRIM_MEMORY, "trimMemory");
        haxeEventMap.put(EventType.BACK_PRESSED, "backPressed");

        activity = new WeakReference<DuellActivity>(DuellActivity.getInstance());
        haxeEvents = new ArrayDeque<HaxeEvent>();
    }

    public void dispatchEvent(EventType type)
    {
        dispatchEvent(type, new Object[]{});
    }

    public void dispatchEvent(EventType type, Object[] data)
    {
        synchronized (haxeEvents)
        {
            haxeEvents.add(new HaxeEvent(type, data));
        }

        if (!pendingQueueProcessing)
        {
            pendingQueueProcessing = true;

            DuellActivity activityLocal = activity.get();
            if (activityLocal != null)
            {
                activityLocal.queueOnHaxeThread(this);
            }
        }
    }

    @Override
    public void run()
    {
        // dispatching events
        synchronized (haxeEvents)
        {
            while (!haxeEvents.isEmpty())
            {
                HaxeEvent event = haxeEvents.removeFirst();

                if (event.data == null || event.data.length == 0)
                {
                    haxeObject.call0(haxeEventMap.get(event.type));
                }
                else
                {
                    switch (event.data.length)
                    {
                        case 1:
                            haxeObject.call1(haxeEventMap.get(event.type), event.data[0]);
                            break;
                        case 2:
                            haxeObject.call2(haxeEventMap.get(event.type), event.data[0], event.data[1]);
                            break;
                        case 3:
                            haxeObject.call3(haxeEventMap.get(event.type), event.data[0], event.data[1], event.data[2]);
                            break;
                        case 4:
                            haxeObject.call4(haxeEventMap.get(event.type), event.data[0], event.data[1], event.data[2], event.data[3]);
                            break;
                        default:
                            android.util.Log.e("duell", "AppDelegate - unexpected number of arguments " + event.data.length);
                    }
                }
            }

            pendingQueueProcessing = false;
        }
    }
}
