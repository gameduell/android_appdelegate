package android_appdelegate;

import hxjni.JNI;

import msignal.Signal;

class AndroidAppDelegate {
	
	/// SIGNALS
	public var onLowMemory (default, null) : Signal0;
	public var onTrimMemory (default, null) : Signal1<Int>;

	/// INIT
	private function new() : Void {

		appdelegate_initialize_jni(this);
		onTrimMemory = new Signal1<Int>();
		onLowMemory = new Signal0();
	}

	/// Methods called by Java
	public function lowMemory() : Void {
		onLowMemory.dispatch();
	}	

	public function trimMemory(level : Int) : Void {
		onTrimMemory.dispatch(level);
	}	
	
	/// JAVA ACCESS
	private static var appdelegate_initialize_jni = JNI.createStaticMethod ("org/haxe/extension/AppDelegate", "initialize", "(Lorg/haxe/hxjni/HaxeObject;)V");

	/// SINGLETON	
	static var appDelegateInstance : AndroidAppDelegate;
	static public inline function instance() : AndroidAppDelegate
	{
		if(appDelegateInstance == null)
		{
			appDelegateInstance = new AndroidAppDelegate();
		}
		return appDelegateInstance;
	}
}